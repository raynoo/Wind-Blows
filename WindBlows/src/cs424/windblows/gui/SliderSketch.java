package cs424.windblows.gui;

import java.util.ArrayList;
import java.util.Date;

import omicronAPI.OmicronTouchListener;
import processing.core.PApplet;
import cs424.windblows.application.Constants;
import cs424.windblows.application.EnumColor;
import cs424.windblows.application.Utils;
import cs424.windblows.application.Variable;
import cs424.windblows.listeners.FilterListener;

public class SliderSketch extends Sketch implements OmicronTouchListener {
	
	protected Date minDate, maxDate, curDate;
	protected float yVal;
	protected float dMark = Utils.scale(5);
	protected float curr, lastEvent;
	protected ArrayList<FilterListener> listeners;
	
	protected float triWidth = Utils.scale(7), triHeight = Utils.scale(15);
	
	public SliderSketch(Variable data) {
		super(data);
		minDate = Utils.getDate(Constants.minDate);
		maxDate = Utils.getDate(Constants.maxDate);
		System.out.println(minDate + " - " + maxDate);
		yVal = plotY2;
		curDate = minDate;
		curr = 21;
		lastEvent = 0;
		listeners = new ArrayList<FilterListener>();
	}
	
	@Override
	protected void draw() {
		parent.pushStyle();
		parent.fill(EnumColor.GRAY.getValue());
		parent.rectMode(PApplet.CORNERS);
		//parent.rect(plotX1, plotY1, plotX2, plotY2);
		drawXLabels();
		drawCurrentPointer();
		updatePointer();
		parent.line(plotX1, yVal, plotX2, yVal);
		parent.popStyle();
	
	}
	
	public void drawXLabels(){
		parent.stroke(255);
		parent.strokeWeight(1);
		int i = 1;
		// loop throught the days mark them
		for(Date d = minDate; !d.after(maxDate); d = Utils.addDays(d, 1), i++){
			float x = PApplet.map(i, 1, 21, plotX1, plotX2);
			parent.line(x, yVal, x, yVal - dMark);
			parent.textAlign(PApplet.CENTER);
			parent.text(Utils.getFormattedDateMonth(d), x, yVal - dMark * 2);
		}
	}
	
	public void drawCurrentPointer(){
		
		float x = PApplet.map(curr,1, 21, plotX1, plotX2);
		parent.fill(EnumColor.WHITE.getValue());
		parent.triangle(x, yVal, x - triWidth, yVal + triHeight, x + triWidth, yVal + triHeight);
		
	}
	
	public void updatePointer(){
		if(curr <= 21)
			curr += 0.1f;
		updateListeners();
	}
	
	public void updateListeners(){
		int dat = PApplet.floor(curr);
		if(dat != lastEvent){
			lastEvent = dat;
			// update listeners
			//System.out.println("updating listeners!" + lastEvent);
			Date d = Utils.getDate(minDate, maxDate, (int)lastEvent);
			//System.out.println(d);
			for(FilterListener listener : listeners)
				listener.dateChanged(d);
		}
	}
	
	@Override
	public void touchDown(int arg0, float arg1, float arg2, float arg3,
			float arg4) {
			curr = 1;
	}

	@Override
	public void touchMove(int arg0, float arg1, float arg2, float arg3,
			float arg4) {

	}

	@Override
	public void touchUp(int arg0, float arg1, float arg2, float arg3, float arg4) {

	}
	
	public void addListener(FilterListener listener){
		this.listeners.add(listener);
	}
	
	@Override
	public boolean isTouchValid(float xPos, float yPos) {
		if(Utils.isPresent(plotX1, plotX2, xPos)
				&& Utils.isPresent(plotY1, plotY1 + plotY2, yPos))
			return true;
		else return false;
	}
}