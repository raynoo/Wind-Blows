package cs424.windblows.gui;

import omicronAPI.OmicronTouchListener;
import static cs424.windblows.application.Constants.infoX;
import static cs424.windblows.application.Constants.infoY;
import static cs424.windblows.application.Constants.infoWidth;
import static cs424.windblows.application.Constants.infoHeight;
import cs424.windblows.application.EnumColor;
import cs424.windblows.application.Utils;
import cs424.windblows.data.DBFacade;
import cs424.windblows.data.Tweet;
import cs424.windblows.listeners.MarkerListener;
import processing.core.PApplet;

public class Marker implements OmicronTouchListener {
	
	Location location;
	int tweetID, userID;
	Tweet tweetInfo;
	
	float centerX, centerY;
	float radius;
	int color;
	
	MarkerListener listener;
	
	PApplet p;
	
	//coordinates are already scaled while creating markers.
	//ie pass in scaled coordinates.
	public Marker(float centerX, float centerY, float radius, PApplet papplet, int tweetid) {
		this.centerX = centerX;
		this.centerY = centerY;
		this.radius = radius;
		this.p = papplet;
		this.tweetID = tweetid;
	}
	
	public void setColor(int color) {
		this.color = color;
	}
	
	public void draw() {
		p.pushStyle();
		p.stroke(EnumColor.DARK_RED.getValue());
		p.strokeWeight(Utils.scale(1f));
		p.fill(color);
		p.ellipse(centerX, centerY, 2*radius, 2*radius);
		p.popStyle();
	}
	
	public void displayInfo() {
		tweetInfo = getTweetData();
		String info = "User ID: " + tweetInfo.getUserID() + "\nMessage: " + tweetInfo.getTweet();
		
		p.pushStyle();
		p.fill(EnumColor.GRAY.getValue());
		p.rect(infoX, infoY, infoWidth, infoHeight, Utils.scale(5));
		p.fill(EnumColor.BLACK.getValue());
		p.textSize(Utils.scale(8));
		p.text(info, infoX+5, infoY+5, infoWidth-5, infoHeight-5);
		
		p.popStyle();
	}
	
	public boolean containsPoint(float x, float y) {
		if(Math.pow(centerX - x, 2) + Math.pow(centerY - y, 2) <= Math.pow(radius, 2))
			return true;
		
		return false;
	}
	
	public Tweet getTweetData() {
		return DBFacade.getInstance().getTweetInfo(tweetID);
	}

	@Override
	public void touchDown(int arg0, float arg1, float arg2, float arg3,
			float arg4) {
		//display info
		System.out.println("Marker.touchDown()");
		this.listener.markerSelected(this);
	}

	@Override
	public void touchMove(int arg0, float arg1, float arg2, float arg3,
			float arg4) {
		// TODO Auto-generated method stub
	}

	@Override
	public void touchUp(int arg0, float arg1, float arg2, float arg3, float arg4) {
		// TODO Auto-generated method stub
	}
	
	public void setListener(MarkerListener m) {
		this.listener = m;
	}
	
	public void setTweetID(int id) {
		this.tweetID = id;
	}
	
	public int getTweetID() {
		return tweetID;
	}
	
	public void setUserID(int id) {
		this.userID = id;
	}
	
	public int getUserID() {
		return userID;
	}
	
	public void setTweetInfo(Tweet info) {
		this.tweetInfo = info;
	}
	
	public Tweet getTweetInfo() {
		return tweetInfo;
	}
}
