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

	protected ArrayList<CheckBox> checks;
	protected float xBuff = Utils.scale(20);
	protected float yBuff = Utils.scale(20);
	
	protected HashMap<Integer, String> keyMap;
	
	protected FilterListener listener;
	
	public KeywordsSketch(Variable data) {
		super(data);
		
		keyMap = DBFacade.getInstance().getKeywords();
		
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
				x += 100;
				y = plotY1 + yBuff;
			}
			
			count++;
		}
		
		for(CheckBox box : checks){
			box.setActive(true);
		}
	}
	
	@Override
	protected void draw() {
		parent.pushStyle();
		parent.fill(EnumColor.OFFWHITE.getValue());
		parent.rect(plotX1, plotY1, plotX2, plotY2, Utils.scale(10));
		
		for(CheckBox box : checks){
			box.draw();
		}
		parent.popStyle();
	}
	
	@Override
	public void touchDown(int ID, float xPos, float yPos, float xWidth, float yWidth){
		
		for(CheckBox box : checks){
			box.touchDown(ID, xPos, yPos, xWidth, yWidth);
		}
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
		listener.categoryRemoved(id);
	}
	
	@Override
	public void checkboxEnabled(int id) {
		listener.categoryAdded(id);
	}

	public FilterListener getListener() {
		return listener;
	}

	public void setListener(FilterListener listener) {
		this.listener = listener;
	}
	
}
