package cs424.windblows.data;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import processing.core.PApplet;
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
}
