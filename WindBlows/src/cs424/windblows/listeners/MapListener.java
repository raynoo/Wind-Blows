package cs424.windblows.listeners;

import cs424.windblows.application.Main;
import cs424.windblows.gui.Button;
import cs424.windblows.gui.Map;
import omicronAPI.OmicronTouchListener;

public class MapListener implements OmicronTouchListener {
	
	Map map;
	
	public MapListener(Main main) {
		map = main.map;
	}

	@Override
	public void touchDown(int ID, float xPos, float yPos, float xWidth, float yWidth) {
		
		if(map.getZoomIn().containsPoint(xPos, yPos))
			map.zoomIn();
		else if(map.getZoomOut().containsPoint(xPos, yPos))
			map.zoomOut();
		else if(map.getPanUp().containsPoint(xPos, yPos)) {
			
			map.panUp();
		}
		else if(map.getPanDown().containsPoint(xPos, yPos))
			map.panDown();
		else if(map.getPanLeft().containsPoint(xPos, yPos))
			map.panLeft();
		else if(map.getPanRight().containsPoint(xPos, yPos))
			map.panRight();
		
	}

	@Override
	public void touchMove(int ID, float xPos, float yPos, float xWidth, float yWidth) {
		
	}
	
	@Override
	public void touchUp(int ID, float xPos, float yPos, float xWidth, float yWidth) {
		
	}
}
