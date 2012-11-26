package cs424.windblows.application;

import java.util.ArrayList;
import java.util.Date;

import cs424.windblows.gui.Location;

/**
 * Used to filter tweet's when they are plotted on the map.
 * Instance of this object is passed to DBFacade.
 * @author root
 *
 */
public class Filter {
	protected ArrayList<Integer> categories;
	protected Date date;
	protected Float topLeftLat, topLeftLong, bottomRightLat, bottomRightLong;
	
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
	
	public void setBoundary(Location[] boundary) {
		this.topLeftLat = boundary[0].getLat();
		this.topLeftLong = boundary[0].getLon();
		this.bottomRightLat = boundary[1].getLat();
		this.bottomRightLong = boundary[1].getLon();
	}

	public Float getTopLeftLat() {
		return topLeftLat;
	}

	public Float getTopLeftLong() {
		return topLeftLong;
	}

	public Float getBottomRightLat() {
		return bottomRightLat;
	}

	public Float getBottomRightLong() {
		return bottomRightLong;
	}
}
