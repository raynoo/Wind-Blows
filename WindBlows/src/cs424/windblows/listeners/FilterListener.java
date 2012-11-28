package cs424.windblows.listeners;

import java.util.Date;

public interface FilterListener {
	public void categoryAdded(int categoryId);
	public void categoryRemoved(int categoryId);
	
	/**
	 * need to change this, fetch all the data from the database for all the dates.
	 * @param date
	 */
	
	public void dateChanged(Date date);
	
	public void conditionChanged(int condition);
}
