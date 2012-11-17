package cs424.windblows.data;



/**
 * Singleton, contains methods to interact with data.
 * 
 * @author root
 *
 */
public class DBFacade {
	
	private static DBFacade instance;
	
	private DBFacade(){
		
		initData();
	}
	
	protected void initData(){
		
	}
	
	public static DBFacade getInstance(){
		if(instance == null){
			instance = new DBFacade();
		}
		return instance;
	}
	
}
