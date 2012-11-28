package cs424.windblows.data;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import processing.core.PApplet;
import cs424.windblows.application.Constants;
import cs424.windblows.application.Filter;
import cs424.windblows.application.Utils;
import cs424.windblows.gui.KeywordsSketch;
import de.bezier.data.sql.SQLite;



/**
 * Singleton, contains methods to interact with data.
 * 
 * @author 
 */
public class DBFacade {

	private static DBFacade instance;
	private static PApplet parent;
	
	protected static SQLite db;
	
	
	private DBFacade(){
		db = new SQLite(parent, Utils.getProjectPath() + File.separator + "data/db_windblows.sqlite");
	}
	
	/**
	 * needs to be set before getInstance
	 * @param p
	 */
	public static void setPApplet(PApplet p){
		parent = p;
	}
	
	public static DBFacade getInstance(){
		
		if(parent == null){
			System.err.println("Initialize DBFacade with PApplet object, call setPApplet method first.");
			System.exit(0);
		}
		
		if(instance == null){
			instance = new DBFacade();
		}
		
		return instance;
	}
	
	/**
	 * Returns the keywords and its corresponding id
	 * @return
	 */
	public HashMap<Integer, String> getKeywords(){
		HashMap<Integer, String> map = new HashMap<Integer, String>();
		if(db.connect()){
			 db.query("select categoryId, category from Microblogs group by category order by categoryId");
			 while(db.next()) {
				 map.put(db.getInt("categoryId"), db.getString("category"));
			 }
		}
		return map;
	}
	
	/**
	 * Returns the Tweet's for a given date
	 * TODO 1. modify this function so that it also takes in to account time.
	 * 		2. also category id
	 * 		3. identify the data which need to be fetched
	 * 
	 * 
	 * 	AND - 	select lat, long from Microblogs inner join TweetCategory on TweetCategory.tweet_id = Microblogs.tweet_id 
	 *			where keyword_id IN (21,12)
	 *			group by TweetCategory.tweet_id
     *			having COUNT(distinct TweetCategory.keyword_id)=2  
     *
     *	OR - 	select lat, long from Microblogs inner join TweetCategory on TweetCategory.tweet_id = Microblogs.tweet_id 
	 *			where keyword_id IN (21,12)
	 * @return ArrayList<Tweet>
	 */
	public ArrayList<Tweet> getTweets(Filter filter){
		ArrayList<Tweet> list = new ArrayList<Tweet>();
		
		
		// convert the date to format 
		StringBuffer sql = new StringBuffer();
		sql.append("select lat, long, Microblogs.tweet_id ");
		sql.append(" from Microblogs inner join TweetCategory on TweetCategory.tweet_id = Microblogs.tweet_id ");
		sql.append("where date == '");
		sql.append(Utils.getFormattedDate(filter.getDate()));
		sql.append("' ");
	
		// add categories
		// if condition is OR
		if(filter.getCategories().size() > 0){
			sql.append(" and keyword_id IN (");
			boolean flag = false;
			for(Integer cat : filter.getCategories()){
				if(!flag) sql.append(cat);
				else{
					sql.append(", ");
					sql.append(cat);
				}
				flag = true;
			}
			sql.append(")");
		}
		else {
			return list; // returns if no category was selected
		}
		
		
		// if condition is and
		if(filter.getCondition() == KeywordsSketch.AND){
			sql.append(" group by TweetCategory.tweet_id ");
			sql.append(" having COUNT(distinct TweetCategory.keyword_id)=");
			sql.append(filter.getCategories().size());
		}
		
		if(filter.getTopLeftLat() != null) {
			sql.append(" and lat < " + filter.getTopLeftLat());
			sql.append(" and lat > " + filter.getBottomRightLat());
			sql.append(" and long < " + filter.getTopLeftLong());
			sql.append(" and long > " + filter.getBottomRightLong());
		}
		
		System.out.println("<DEBUG>" + sql.toString());
		
		
		if(db.connect()){
			 db.query(sql.toString());
			 while(db.next()) {
				 Tweet t = new Tweet();
				 t.setLat(db.getDouble("lat"));
				 t.setLon(db.getDouble("long"));
				 t.setTweetID(db.getInt("tweet_id"));
				 t.setUserID(db.getInt("person_id"));
				 list.add(t);
			 }
		}
		return list;
	}
	
	
	/**
	 * Returns all tweets - for data processing
	 * @return HashMap<Integer,String>
	 */
	public HashMap<Integer,String> getAllTweets(){
		
		String sql = "Select tweet_id,tweet from Microblogs;";			
		
		HashMap<Integer,String> tweetMap = new HashMap<Integer,String>();
		if(db.connect()){
			 db.query(sql.toString());
			 while(db.next()) {
				 Integer tweet_id = db.getInt("tweet_id");	
				 String tweet = db.getString("tweet");				 
				 tweetMap.put(tweet_id,tweet);
			 }
		}
		return tweetMap;
	}
	
	/**
	 * Returns counts of tweets for selected criteira grouped by the date
	 * @return HashMap<String,Integer>
	 */
	public HashMap<String,Integer> getCategoryCounts(Filter filter) {
        

        StringBuilder sql = new StringBuilder();
        HashMap<String,Integer> counts = new HashMap<String,Integer>();
        Date minDate = Utils.getDate(Constants.minDate);
        Date maxDate = Utils.getDate(Constants.maxDate);                
        int i = 1;
        
        for(Date d = minDate; !d.after(maxDate); d = Utils.addDays(d, 1), i++){
                counts.put(Utils.getFormattedDateMonth(d), 0);
        }
        
        sql.append("select A.date,count(A.tweet_id) as count from Microblogs A inner join " +
                      "TweetCategory B on A.tweet_id = B.tweet_id where ");
        
        /*sql.append("select A.date,count(A.tweet_id) as count from Microblogs A inner join " +
                        "TweetCategory B on A.tweet_id = B.tweet_id where keyword_id = 6 group by A.date ");*/
        
        // add categories
     	// if condition is OR
     	if(filter.getCategories().size() > 0){
     		sql.append(" keyword_id IN (");
     		boolean flag = false;
     		for(Integer cat : filter.getCategories()){
     			if(!flag) sql.append(cat);
     			else{
     				sql.append(", ");
     				sql.append(cat);
     			}
     			flag = true;
     		}
     		sql.append(")");
     	}
     	else {
     		return counts; // returns if no category was selected
     	}
     		
     	// if condition is and
     	if(filter.getCondition() == KeywordsSketch.AND){
     		sql.append(" group by TweetCategory.tweet_id ");
     		sql.append(" having COUNT(distinct TweetCategory.keyword_id)=");
     		sql.append(filter.getCategories().size());
     	}
     		
     	if(filter.getTopLeftLat() != null) {
     		sql.append(" and lat < " + filter.getTopLeftLat());
     		sql.append(" and lat > " + filter.getBottomRightLat());
     		sql.append(" and long < " + filter.getTopLeftLong());
     		sql.append(" and long > " + filter.getBottomRightLong());
     	}
     		
     	System.out.println("<DEBUG>" + sql.toString());
     		
        if(db.connect()){
                 db.query(sql.toString());
                 while(db.next()) {                              
                         Iterator it = counts.entrySet().iterator();
                            while (it.hasNext()) {
                                Map.Entry pairs = (Map.Entry)it.next();                         

                                if(pairs.getKey().equals(Utils.getFormattedDateMonth(Utils.getDate(db.getString("date")))))
                                        pairs.setValue(db.getInt("count"));                                                                     
                            }
                 }
        }
        
        return counts;
	}
	
	public HashMap<String, Integer> getKeywordCount(Filter filter) {
		HashMap<String, Integer> tweetCount = new HashMap<String, Integer>();
		
		StringBuffer sql = new StringBuffer();
		sql.append("select category, tweetcount from category");
		
//		if(filter.getDate() != null) {
//			sql.append(" and m.date == '");
//			sql.append(Utils.getFormattedDate(filter.getDate()));
//			sql.append("' ");
//		}
		
		// add categories
		if(filter.getCategories().size() > 0) {
			sql.append(" where categoryid in (");
			boolean flag = false;
			for(Integer cat : filter.getCategories()) {
				if(!flag) sql.append(cat);
				else {
					sql.append(", ");
					sql.append(cat);
				}
				flag = true;
			}
			sql.append(")");
		}
		
		System.out.println(sql.toString());
		
		if(db.connect()) {
			 db.query(sql.toString());
			 
			 while(db.next()) {
				 String category = db.getString("category");	
				 Integer count = db.getInt("tweetcount");				 
				 tweetCount.put(category, count);
			 }
		}
		return tweetCount;
	}
	
	public List<Tweet> getTweetsByUser(int userID) {
		ArrayList<Tweet> tweets = new ArrayList<Tweet>();
		
		StringBuffer sql = new StringBuffer();
		sql.append("select tweet_id, date, time, lat, long, tweet, category from microblogs where person_id = " + userID);
		
		if(db.connect()) {
			 db.query(sql.toString());
			 
			 while(db.next()) {
				 tweets.add(new Tweet(db.getInt("tweet_id"), userID, db.getString("tweet"),
						 db.getFloat("lat"), db.getFloat("long")));
			 }
		}
		return tweets;
	}
	
	public Tweet getTweetInfo(int tweetID) {
		Tweet tweet = new Tweet();
		
		StringBuffer sql = new StringBuffer();
		sql.append("select person_id, date, time, lat, long, tweet, category from microblogs where tweet_id = " + tweetID);
		
		if(db.connect()) {
			 db.query(sql.toString());
			 tweet.setUserID(db.getInt("person_id"));
			 tweet.setLat(db.getFloat("lat"));
			 tweet.setLon(db.getFloat("long"));
			 tweet.setTweet(db.getString("tweet"));
		}
		return tweet;
	}
}
