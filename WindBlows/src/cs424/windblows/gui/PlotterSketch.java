package cs424.windblows.gui;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Date;

import omicronAPI.OmicronTouchListener;
import processing.core.PApplet;
import processing.core.PImage;
import cs424.windblows.application.Filter;
import cs424.windblows.application.Utils;
import cs424.windblows.application.Variable;
import cs424.windblows.data.DBFacade;
import cs424.windblows.data.Tweet;
import cs424.windblows.listeners.FilterListener;

public class PlotterSketch extends Sketch implements OmicronTouchListener, FilterListener{
	
	protected PImage img;

	protected Date min, max, cur;
	
	protected float lat1, lat2, lon1, lon2;
	protected float width, height;
	protected Color dotColor = new Color(0xFF99FF);
	protected float dotSize = Utils.scale(10);
	protected Filter curFilter;
	protected ArrayList<Tweet> data;
	
	public PlotterSketch(Variable data) {
		super(data);
		img = parent.loadImage("../images/Vastopolis_Map_greyscale.png");
		
		min = Utils.getDate("4/30/2011");
		max = Utils.getDate("5/20/2011");
		cur = min;
		
		width = plotX2;
		height = plotY2;
		
		lat1 = 42.3017f;
		lat2 = 42.1609f;
		lon1 = 93.5673f;
		lon2 = 93.1923f;
		
		
		curFilter = new Filter();
		curFilter.setDate(min);
		this.data = DBFacade.getInstance().getTweets(curFilter);
	}
	
	
	@Override
	protected void draw() {
		// for testing purpose
		parent.image(img, this.plotX1, this.plotY1, width, height);
		
		
		// loop through the tweet's in the list
		parent.fill(this.dotColor.getRGB());
		for(Tweet t : data){
			float y = PApplet.map((float)t.getLat(), lat1, lat2, plotY1, plotY1 + height);
			float x = PApplet.map((float)t.getLon(), lon1, lon2, plotX1, plotX1 + width);
			//System.out.println((float)t.getLat() + "- " + x + " - " + y);
			parent.ellipse(x, y, dotSize, dotSize);
		}
	}
	
	
	@Override
	public void categoryAdded(int categoryId) {
		curFilter.addCategory(categoryId);
		this.data = DBFacade.getInstance().getTweets(curFilter);
		
	}
	
	@Override
	public void categoryRemoved(int categoryId) {
		curFilter.removeCategory(categoryId);
		this.data = DBFacade.getInstance().getTweets(curFilter);
	}
	
	
	@Override
	public void touchDown(int ID, float xPos, float yPos, float xWidth, float yWidth){
			if(cur.equals(max)){
				cur = min;
				curFilter.setDate(min);
			}
			else{
				cur = Utils.addDays(cur, 1);
				curFilter.setDate(cur);
			}
			this.data = DBFacade.getInstance().getTweets(curFilter);
			System.out.println(cur);
	}

	@Override
	public void touchMove(int ID, float xPos, float yPos, float xWidth, float yWidth){
	}

	@Override
	public void touchUp(int ID, float xPos, float yPos, float xWidth, float yWidth){
	}
	
	@Override
	public boolean isTouchValid(float xPos, float yPos) {
		if(Utils.isPresent(plotX1, plotX1 + plotX2, xPos)
				&& Utils.isPresent(plotY1, plotY1 + plotY2, yPos))
			return true;
		else return false;
	}
	
}
