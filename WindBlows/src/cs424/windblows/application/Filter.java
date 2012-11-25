package cs424.windblows.application;

import java.util.ArrayList;
import java.util.Date;

/**
 * Used to filter tweet's when they are plotted on the map.
 * Instance of this object is passed to DBFacade.
 * @author root
 *
 */
public class Filter {
	protected ArrayList<Integer> categories;
	protected Date date;
	
	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Filter() {
		categories = new ArrayList<Integer>();
	}
	
	public void addCategory(int categoryId){
		categories.add(categoryId);
	}
	
	public ArrayList<Integer> getCategories(){
		return categories;
	}
	
	public void removeCategory(int categoryId){
		this.categories.remove(categories.indexOf(categoryId));
	}
}
