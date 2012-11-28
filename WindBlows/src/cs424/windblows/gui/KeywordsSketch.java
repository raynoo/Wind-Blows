package cs424.windblows.gui;

import java.util.ArrayList;
import java.util.HashMap;

import omicronAPI.OmicronTouchListener;
import cs424.windblows.application.ColorCodes;
import cs424.windblows.application.EnumColor;
import cs424.windblows.application.Utils;
import cs424.windblows.application.Variable;
import cs424.windblows.data.DBFacade;
import cs424.windblows.listeners.CheckBoxEventListener;
import cs424.windblows.listeners.FilterListener;
import cs424.windblows.listeners.TimeChanged;

public class KeywordsSketch extends Sketch implements OmicronTouchListener, CheckBoxEventListener{
	public static final int AND = 99;
	public static final int OR = 98;
	
	public static final int DAY = 100;
	public static final int NIGHT = 101;
	public static final int NONE = 102;

	
	protected ArrayList<CheckBox> checks;
	protected ArrayList<CheckBox> condition;
	
 	protected float xBuff = Utils.scale(10);
	protected float yBuff = Utils.scale(20);
	
	protected HashMap<Integer, String> keyMap;
	protected CheckBox and, or, day, night, none;
	
	protected ArrayList<FilterListener> listeners = new ArrayList<FilterListener>();
	protected ArrayList<TimeChanged> timeListeners;

	public void addTimeListener(TimeChanged timeListener) {
		this.timeListeners.add(timeListener);
	}


	public KeywordsSketch(Variable data) {
		super(data);
		
		keyMap = DBFacade.getInstance().getKeywords();
		condition = new ArrayList<CheckBox>();
		
		checks = new ArrayList<CheckBox>();
		
		Variable dat = new Variable();
		dat.setParent(parent);
		
		float x = plotX1 + xBuff;
		float y = plotY1 + yBuff;
		int count = 1;
		for(Integer key : keyMap.keySet()){
			dat.setLabel(keyMap.get(key));
			dat.setPlot(x, y, 0, 0);
			CheckBox c = new CheckBox(dat);
			c.setId(key);
			c.setListener(this);
			checks.add(c);

			y += yBuff;
			if(count % 9 == 0){
				x += Utils.scale(100);
				y = plotY1 + yBuff;
			}
			
			count++;
		}
		
		
		for(CheckBox box : checks){
			box.setActive(true);
		}
		
		initAndOr();
		initDayNight();
		
		timeListeners = new ArrayList<TimeChanged>();
	}
	
	
	protected void initAndOr(){
		Variable dat = new Variable();
		dat.setParent(parent);
		dat.setLabel("AND");
		dat.setPlot(plotX1 + Utils.scale(50) + xBuff, plotY1 + (yBuff * 11), 0, 0);
		and = new CheckBox(dat);
		and.setActive(true);
		and.setId(AND);
		and.setListener(this);
		
		dat.setLabel("OR");
		dat.setPlot(plotX1 + Utils.scale(50) + xBuff, plotY1 + (yBuff * 12), 0, 0);
		or = new CheckBox(dat);
		or.setActive(true);
		or.setId(OR);
		or.setListener(this);
		or.setSelected(true);
	}
	
	protected void initDayNight(){
		Variable dat = new Variable();
		dat.setParent(parent);
		dat.setLabel("Day (06:00 to 17:59)");
		dat.setPlot(plotX1 + Utils.scale(150) + xBuff, plotY1 + (yBuff * 11), 0, 0);
		day = new CheckBox(dat);
		day.setActive(true);
		day.setId(DAY);
		day.setListener(this);
		
		dat.setLabel("Night (18:00 to 5:59)");
		dat.setPlot(plotX1 + Utils.scale(150) + xBuff, plotY1 + (yBuff * 12), 0, 0);
		night = new CheckBox(dat);
		night.setActive(true);
		night.setId(NIGHT);
		night.setListener(this);
		
		dat.setLabel("All Day");
		dat.setPlot(plotX1 + Utils.scale(150) + xBuff, plotY1 + (yBuff * 13), 0, 0);
		none = new CheckBox(dat);
		none.setActive(true);
		none.setId(NONE);
		none.setListener(this);
		none.setSelected(true);
	}
	@Override
	protected void draw() {
		parent.pushStyle();
		parent.fill(EnumColor.OFFWHITE.getValue());
		
		parent.rect(plotX1, plotY1, Utils.scale(100) * 3, (float) (yBuff * 10.5), Utils.scale(10));
		
		parent.fill(EnumColor.OFFWHITE.getValue());
		parent.rect(plotX1 + Utils.scale(50), plotY1 + (float) (yBuff * 10.7),  Utils.scale(70), Utils.scale(55), Utils.scale(5));
		parent.rect(plotX1 + Utils.scale(150), plotY1 + (float) (yBuff * 10.7),  Utils.scale(75) * 2, Utils.scale(75), Utils.scale(5));
		for(CheckBox box : checks){
			box.draw();
		}
		and.draw();
		or.draw();
		day.draw();
		night.draw();
		none.draw();
		
		parent.popStyle();
	}
	
	@Override
	public void touchDown(int ID, float xPos, float yPos, float xWidth, float yWidth){
		
		for(CheckBox box : checks){
			box.touchDown(ID, xPos, yPos, xWidth, yWidth);
		}
		and.touchDown(ID, xPos, yPos, xWidth, yWidth);
		or.touchDown(ID, xPos, yPos, xWidth, yWidth);
		day.touchDown(ID, xPos, yPos, xWidth, yWidth);
		night.touchDown(ID, xPos, yPos, xWidth, yWidth);
		none.touchDown(ID, xPos, yPos, xWidth, yWidth);
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
	
	@Override
	public void checkboxDisabled(int id) {
		switch(id){
		case AND: or.setSelected(true);
					resetCheckboxes();
					listenersConditionChanged(OR);
					ColorCodes.setOR(true);
			break;
		case OR: and.setSelected(true);
					listenersConditionChanged(AND);
					ColorCodes.setOR(false);
			break;
			
		case DAY: 	for(TimeChanged tc : this.timeListeners)
						tc.timeChanged(NONE);
					none.setSelected(true);
		break;

		case NIGHT:	for(TimeChanged tc : this.timeListeners)
						tc.timeChanged(NONE);
					none.setSelected(true);
			break;

		case NONE:	none.setSelected(true);
					day.setSelected(false);
					break;
		default: listenersCategoryRemoved(id);
					ColorCodes.removeMapping(id);
			break;
		}
	}
	
	@Override
	public void checkboxEnabled(int id) {
		switch(id){
		case AND: 	or.setSelected(false);
					listenersConditionChanged(AND);
					ColorCodes.setOR(false);
			break;
		case OR: 	
					and.setSelected(false);
					resetCheckboxes();
					listenersConditionChanged(OR);
					ColorCodes.setOR(true);
			break;
		
		case DAY: 	for(TimeChanged tc : this.timeListeners)
			tc.timeChanged(DAY);
					night.setSelected(false);
					none.setSelected(false);
					// listeners
				break;
				
		case NIGHT: for(TimeChanged tc : this.timeListeners)
			tc.timeChanged(NIGHT);
					day.setSelected(false);
					none.setSelected(false);
			break;
	
		case NONE:	for(TimeChanged tc : this.timeListeners)
			tc.timeChanged(NONE);
					night.setSelected(false);
		day.setSelected(false);
			break;
	
		default: 	listenersCategoryAdded(id);
					ColorCodes.genMappings(id);
			break;
		}
	}
	
	public void resetCheckboxes(){
		for(CheckBox cb : this.checks){
			if(cb.isSelected){
				cb.isSelected = false;
				listenersCategoryRemoved(cb.id);
			}
			ColorCodes.reset();
		}
	}
	public void listenersConditionChanged(int id){
		for(FilterListener fl : listeners){
			fl.conditionChanged(id);
		}
	}
	
	public void listenersCategoryAdded(int id){
		for(FilterListener fl : listeners){
			fl.categoryAdded(id);
		}
	}
	
	public void listenersCategoryRemoved(int id){
		for(FilterListener fl : listeners){
			fl.categoryRemoved(id);
		}
	}

	public void setListener(FilterListener listener) {
		this.listeners.add(listener);
	}

}
