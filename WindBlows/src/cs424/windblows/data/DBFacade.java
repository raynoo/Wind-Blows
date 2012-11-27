package cs424.windblows.data;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import processing.core.PApplet;
import cs424.windblows.application.Constants;
import cs424.windblows.application.Filter;
import cs424.windblows.application.Utils;
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
		db = new SQLite(parent, "data/db_windblows.sqlite");
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
	 * @return ArrayList<Tweet>
	 */
	public ArrayList<Tweet> getTweets(Filter filter){
		ArrayList<Tweet> list = new ArrayList<Tweet>();
		
		
		// convert the date to format 
		StringBuffer sql = new StringBuffer();
		sql.append("Select lat, long from Microblogs where ");
		sql.append("date == '");
		sql.append(Utils.getFormattedDate(filter.getDate()));
		sql.append("' ");
	
		// add categories
		if(filter.getCategories().size() > 0){
			sql.append(" and categoryId in (");
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
		if(filter.getTopLeftLat() != null) {
			sql.append(" and lat < " + filter.getTopLeftLat());
			sql.append(" and lat > " + filter.getBottomRightLat());
			sql.append(" and long < " + filter.getTopLeftLong());
			sql.append(" and long > " + filter.getBottomRightLong());
		}
		
		//System.out.println(this.getClass() + " <DEBUG>" + sql.toString());
		if(db.connect()){
			 db.query(sql.toString());
			 while(db.next()) {
				 Tweet t = new Tweet();
				 t.setLat(db.getDouble("lat"));
				 t.setLon(db.getDouble("long"));
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

	public HashMap<String,Integer> getCategoryCounts(Filter filter) {
		// TODO Auto-generated method stub

		StringBuilder sql = new StringBuilder();
		HashMap<String,Integer> counts = new HashMap<String,Integer>();
		Date minDate = Utils.getDate(Constants.minDate);
		Date maxDate = Utils.getDate(Constants.maxDate);		
		int i = 1;
		
		for(Date d = minDate; !d.after(maxDate); d = Utils.addDays(d, 1), i++){
			counts.put(Utils.getFormattedDateMonth(d), 0);
		}
		
		//sql.append("select A.date,count(A.tweet_id) as count from Microblogs A inner join " +
		//		"TweetCategory B on A.tweet_id = B.tweet_id where ");
		
		sql.append("select A.date,count(A.tweet_id) as count from Microblogs A inner join " +
				"TweetCategory B on A.tweet_id = B.tweet_id where keyword_id = 2 group by A.date ");
		
		// add categories
		/*if(filter.getCategories().size() > 0){
			sql.append(" B.Keyword_id in (");
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
		if(filter.getTopLeftLat() != null) {
			sql.append(" and A.lat < " + filter.getTopLeftLat());
			sql.append(" and A.lat > " + filter.getBottomRightLat());
			sql.append(" and A.long < " + filter.getTopLeftLong());
			sql.append(" and A.long > " + filter.getBottomRightLong());
		}*/
		
		System.out.println(this.getClass() + " <DEBUG>" + sql.toString());
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
}
