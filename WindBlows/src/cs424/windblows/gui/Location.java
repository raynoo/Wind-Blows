package cs424.windblows.gui;

import processing.core.PVector;

public class Location {

	PVector location;
	
	public Location(float lat, float lon) {
		this.location = new PVector(lon, lat);
	}
	
	public float getLat() {
		return this.location.y;
	}
	
	public float getLon() {
		return this.location.x;
	}
	
	@Override
	public String toString() {
		return "Lat=" + this.location.y + ", Lon=" + this.location.x;
	}
}
