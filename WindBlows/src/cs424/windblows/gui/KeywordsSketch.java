package cs424.windblows.gui;

import java.util.ArrayList;
import java.util.HashMap;

import omicronAPI.OmicronTouchListener;
import cs424.windblows.application.EnumColor;
import cs424.windblows.application.Utils;
import cs424.windblows.application.Variable;
import cs424.windblows.data.DBFacade;
import cs424.windblows.listeners.CheckBoxEventListener;
import cs424.windblows.listeners.FilterListener;

public class KeywordsSketch extends Sketch implements OmicronTouchListener, CheckBoxEventListener{
	public static final int AND = 99;
	public static final int OR = 98;
	
	protected ArrayList<CheckBox> checks;
	protected ArrayList<CheckBox> condition;
	
 	protected float xBuff = Utils.scale(20);
	protected float yBuff = Utils.scale(20);
	
	protected HashMap<Integer, String> keyMap;
	protected CheckBox and, or;
	
	protected ArrayList<FilterListener> listeners = new ArrayList<FilterListener>();
	
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
		dat.setLabel("AND");
		dat.setPlot(plotX1 + xBuff, plotY1 + (yBuff * 11), 0, 0);
		and = new CheckBox(dat);
		and.setActive(true);
		and.setId(AND);
		and.setListener(this);
		
		dat.setLabel("OR");
		dat.setPlot(plotX1 + xBuff + Utils.scale(70), plotY1 + (yBuff * 11), 0, 0);
		or = new CheckBox(dat);
		or.setActive(true);
		or.setId(OR);
		or.setListener(this);
		or.setSelected(true);
		
		for(CheckBox box : checks){
			box.setActive(true);
		}
	}
	
	@Override
	protected void draw() {
		parent.pushStyle();
		parent.fill(EnumColor.OFFWHITE.getValue());
		//parent.rect(plotX1, plotY1, plotX2, plotY2, Utils.scale(10));
		
		parent.fill(EnumColor.OFFWHITE.getValue());
		//parent.rect(plotX1, plotY2 + Utils.scale(20), plotX2, Utils.scale(40), Utils.scale(10));
		
		for(CheckBox box : checks){
			box.draw();
		}
		and.draw();
		or.draw();
		parent.popStyle();
	}
	
	@Override
	public void touchDown(int ID, float xPos, float yPos, float xWidth, float yWidth){
		
		for(CheckBox box : checks){
			box.touchDown(ID, xPos, yPos, xWidth, yWidth);
		}
		and.touchDown(ID, xPos, yPos, xWidth, yWidth);
		or.touchDown(ID, xPos, yPos, xWidth, yWidth);
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
					listenersConditionChanged(OR);
			break;
		case OR: and.setSelected(true);
					listenersConditionChanged(AND);
			break;
		default: listenersCategoryRemoved(id);
			break;
		}
	}
	
	@Override
	public void checkboxEnabled(int id) {
		switch(id){
		case AND: 	or.setSelected(false);
					listenersConditionChanged(AND);
			break;
		case OR: 	and.setSelected(false);
					listenersConditionChanged(OR);
			break;
		default: 	listenersCategoryAdded(id);
			break;
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
