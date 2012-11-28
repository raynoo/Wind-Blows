package cs424.windblows.gui;

import omicronAPI.OmicronTouchListener;
import static cs424.windblows.application.Constants.infoX;
import static cs424.windblows.application.Constants.infoY;
import static cs424.windblows.application.Constants.infoWidth;
import static cs424.windblows.application.Constants.infoHeight;
import cs424.windblows.application.EnumColor;
import cs424.windblows.application.Utils;
import cs424.windblows.application.Variable;
import cs424.windblows.data.DBFacade;
import cs424.windblows.data.Tweet;
import cs424.windblows.listeners.MarkerListener;
import processing.core.PApplet;

public class Marker implements OmicronTouchListener {
	
	Location location;
	int tweetID, userID, categoryID;
	Tweet tweetInfo; String infoText;
	MarkerInfoPanel infoPanel;
	
	float centerX, centerY;
	float radius;
	int color;
	
	MarkerListener listener;
	boolean selected = false;
	
	PApplet p;
	
	//coordinates are already scaled while creating markers.
	//ie pass in scaled coordinates.
	public Marker(float centerX, float centerY, float radius, PApplet papplet, int tweetid, int catId) {
		this.centerX = centerX;
		this.centerY = centerY;
		this.radius = Utils.scale(radius);
		this.p = papplet;
		this.tweetID = tweetid;
		this.categoryID = catId;
	}
	
	public int getCategoryID() {
		return categoryID;
	}

	public void setCategoryID(int categoryID) {
		this.categoryID = categoryID;
	}

	public void setColor(int color) {
		this.color = color;
	}
	
	public void draw() {
		p.pushStyle();
		p.stroke(EnumColor.DARK_RED.getValue());
		
		p.strokeWeight(Utils.scale(1f));
		if(selected)
			p.strokeWeight(Utils.scale(3f));

		p.fill(color);
		p.ellipse(centerX, centerY, 2*radius, 2*radius);
		p.popStyle();
	}
	
	public void displayInfo() {
		tweetInfo = getData();
		infoText = "User ID: " + tweetInfo.getUserID() + "\nMessage: " + tweetInfo.getTweet();
		infoPanel = new MarkerInfoPanel(this, p);
		infoPanel.draw();
	}
	
	public boolean containsPoint(float x, float y) {
		if(Math.pow(centerX - x, 2) + Math.pow(centerY - y, 2) <= Math.pow(radius, 2))
			return true;
		
		return false;
	}
	
	public String getInfoText() {
		return infoText;
	}
	
	public Tweet getData() {
		return DBFacade.getInstance().getTweetInfo(tweetID);
	}

	@Override
	public void touchDown(int arg0, float arg1, float arg2, float arg3,
			float arg4) {
		//display info
		System.out.println("Marker.touchDown()");
		this.listener.markerSelected(this);
//		this.selected = true;
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
		this.tweetInfo.setUserID(id);
	}
	
	public int getUserID() {
		return this.getTweetInfo().getUserID();
	}
	
	public void setTweetInfo(Tweet info) {
		this.tweetInfo = info;
	}
	
	public Tweet getTweetInfo() {
		return tweetInfo;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(this.tweetID == ((Tweet)obj).getTweetID())
			return true;
		return false;
	}
}

class MarkerInfoPanel {

	PApplet p;
	Marker parent;
	Button addUser;
	
	float x1, x2, y1, y2;
	
	public MarkerInfoPanel(Marker parent, PApplet papplet) {
		this.parent = parent;
		this.p = papplet;
		
		x1 = Utils.scale(infoX); y1 = Utils.scale(infoY);
		x2 = Utils.scale(infoX+infoWidth); y2 = Utils.scale(infoY+infoHeight);
		
		Variable buttonVariable = new Variable();
		buttonVariable.setPlot(Utils.scale(infoX + infoWidth/2 - 30), 
				Utils.scale(infoY + infoHeight - 15), 
				Utils.scale(infoX + infoWidth/2 + 30), 
				Utils.scale(infoY + infoHeight - 3));
		buttonVariable.setParent(p);
		
		addUser = new Button(buttonVariable, "Add Person");
		addUser.setTextSize((int)Utils.scale(8));
	}
	
	public void draw() {
		p.pushStyle();
		p.fill(EnumColor.GRAY.getValue());
		p.rectMode(PApplet.CORNERS);
		p.rect(x1, y1, x2, y2, Utils.scale(5));
		p.fill(EnumColor.BLACK.getValue());
		p.textSize(Utils.scale(8));
		p.text(parent.getInfoText(), x1+Utils.scale(5), y1+Utils.scale(5), 
				x2-Utils.scale(5), y2-Utils.scale(5));
		p.popStyle();
		
		addUser.draw();
	}
	
	public void touchDown(int id, float x1, float y1, float x2, float y2) {
		
		if(this.addUser.containsPoint(x1, y1)) {
//			if(Map.getSavedPeople() != null) {
			if(NewMap.getSavedPeople() != null) {
				parent.listener.markerUserSelected(parent);
//				this.addUser.setPressed(true);
			}
		}
	}
	
	public boolean containsPoint(float x, float y) {
		if(x > x1 && x < x2 && y > y1 && y < y2)
			return true;
		
		return false;
	}
	
}
