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
	
	protected CustomButton play, pause, next, prev, first, last;
	
	protected boolean playFlag = false;
	
	
	protected float triWidth = Utils.scale(7), triHeight = Utils.scale(15);
	
	public SliderSketch(Variable data) {
		super(data);
		minDate = Utils.getDate(Constants.minDate);
		maxDate = Utils.getDate(Constants.maxDate);
		
		yVal = plotY1 + Utils.scale(30);
		curDate = minDate;
		curr = 1;
		lastEvent = 0;
		listeners = new ArrayList<FilterListener>();
		
		
		initPlaybackPanel();
	}
	
	@Override
	protected void draw() {
		parent.pushStyle();
		parent.fill(EnumColor.WHITE.getValue());
		
		displayButtons();
		
		drawXLabels();
		drawCurrentPointer();
		
		if(playFlag)
			updatePointer();
		
		
		parent.line(plotX1, yVal, plotX1 + plotX2, yVal);
		parent.popStyle();
	}
	
	public void displayButtons(){
		play.display();
		pause.display();
		first.display();
		last.display();
		next.display();
		prev.display();
	}
	
	public void drawXLabels(){
		parent.stroke(255);
		parent.strokeWeight(1);
		int i = 1;
		// loop throught the days mark them
		for(Date d = minDate; !d.after(maxDate); d = Utils.addDays(d, 1), i++){
			float x = PApplet.map(i, 1, 21, plotX1, plotX1 + plotX2);
			parent.line(x, yVal, x, yVal - dMark);
			parent.textAlign(PApplet.CENTER);
			parent.text(Utils.getFormattedDateMonth(d), x, yVal - dMark * 2);
		}
	}
	
	
	void initPlaybackPanel() {
		//TODO: create a sketch for the entire panel

		float centerX = plotX1 + plotX2/2;
		float xBuff = Utils.scale(5);
		
		float centerY = plotY1 + Utils.scale(47);
		float buttonWidth = Utils.scale(15);
		float buttonHeight = Utils.scale(15);
		
		Variable dat = new Variable();
		dat.setParent(parent);
		dat.setLabel("P");
		dat.setPlot(centerX, centerY, buttonWidth, buttonHeight);
		play = new CustomButton(parent, "P", centerX, centerY, buttonWidth, buttonHeight, true);
		play.active = true;
		
		pause = new CustomButton(parent, "||", centerX + buttonWidth + xBuff, centerY, buttonWidth, buttonHeight, true);
		pause.active = true;
		
		next = new CustomButton(parent, ">", centerX + (buttonWidth + xBuff)*2, centerY, buttonWidth, buttonHeight, true);
		next.active = true;
		
		last = new CustomButton(parent, ">>", centerX + (buttonWidth + xBuff)*3, centerY, buttonWidth + Utils.scale(7), buttonHeight, true);
		last.active = true;

		prev = new CustomButton(parent, "<", centerX - buttonWidth - xBuff, centerY, buttonWidth, buttonHeight, true);
		prev.active = true;

		first = new CustomButton(parent, "<<", centerX - (buttonWidth + xBuff)*2 - + Utils.scale(7), centerY, buttonWidth + + Utils.scale(7), buttonHeight, true);
		first.active = true;

		
	}
	
	public void drawCurrentPointer(){
		
		float x = PApplet.map(curr,1, 21, plotX1, plotX1 + plotX2);
		parent.fill(EnumColor.WHITE.getValue());
		parent.triangle(x, yVal, x - triWidth, yVal + triHeight, x + triWidth, yVal + triHeight);
		
	}
	
	public void updatePointer(){
		if(curr <= 21)
			curr += 0.1f;
		updateListeners();
		int val = PApplet.floor(curr);
		if(playFlag && val == 21){
			playFlag = false;
		}
	}
	
	public void updateListeners(){
		int dat = PApplet.floor(curr);
		if(dat != lastEvent){
			lastEvent = dat;
			// update listeners
			
			Date d = Utils.getDate(minDate, maxDate, (int)lastEvent);
			
			for(FilterListener listener : listeners)
				listener.dateChanged(d);
		}
	}

	
	@Override
	public void touchDown(int ID, float mX, float mY, float xWidth, float yWidth){
		if(play.isFocus(mX, mY)){
			playFlag = true;
			if(curr >= 21) curr = 1;
		}
		else if(pause.isFocus(mX, mY)){
			playFlag = false;
		}
		else if(next.isFocus(mX, mY)){
			if(curr < 21){
				curr = PApplet.floor(curr);
				curr++;
				updateListeners();
			}
		}
		else if(last.isFocus(mX, mY)){
			if(curr < 21){
				curr = 21;
				updateListeners();
			}
		}
		else if(first.isFocus(mX, mY)){
			if(curr > 1){
				curr = 1;
				updateListeners();
			}
		}
		else if(prev.isFocus(mX, mY)){
			if(curr > 1){
				curr = PApplet.floor(curr);
				curr--;
				updateListeners();
			}
		}
		//curr = 1;
	}

	@Override
	public void touchMove(int ID, float xPos, float yPos, float xWidth, float yWidth){
		
	}

	@Override
	public void touchUp(int ID, float xPos, float yPos, float xWidth, float yWidth){
		
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