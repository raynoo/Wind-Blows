package cs424.windblows.gui;

import static cs424.windblows.application.Constants.*;

import java.util.ArrayList;

import processing.core.PImage;
import cs424.windblows.application.EnumColor;
import cs424.windblows.application.Variable;

public class Map extends Sketch {

	PImage mapImage;
	
	int updown = 0;
	int leftright = 0;
	int zoom = 1, noMoreZoomIn = 1, noMoreZoomOut = 5;
	
	ArrayList<Button> mapButtons = new ArrayList<Button>();
	Button zoomIn, zoomOut, panLeft, panUp, panRight, panDown;
	
	public Map(Variable data) {
		super(data);
		this.mapImage = parent.loadImage("../images/Vastopolis_Map.png");
		initButtons();
	}
	
	void initButtons() {
		Variable data = new Variable();
		data.setParent(this.parent);
		
		data.setPlot(zoomInButtonX, zoomInButtonY-zoomButtonHeight-scale(3), 
				zoomInButtonX+zoomButtonWidth, 
				zoomInButtonY-scale(3));
		panUp = new Button(data, "^");
		
		data.setPlot(zoomInButtonX, zoomInButtonY, 
				zoomInButtonX+zoomButtonWidth, 
				zoomInButtonY+zoomButtonHeight);
		zoomIn = new Button(data, "+");
		
		data.setPlot(zoomInButtonX, zoomInButtonY+zoomButtonHeight, 
				zoomInButtonX+zoomButtonWidth, 
				zoomInButtonY+zoomButtonHeight*2);
		zoomOut = new Button(data, "-");
		
		data.setPlot(zoomInButtonX, zoomInButtonY+zoomButtonHeight*2+scale(3), 
				zoomInButtonX+zoomButtonWidth, 
				zoomInButtonY+zoomButtonHeight*3+scale(3));
		panDown = new Button(data, "v");
		
		data.setPlot(panLeftButtonX, panLeftButtonY, 
				panLeftButtonX+zoomButtonWidth, 
				panLeftButtonY+zoomButtonHeight);
		panLeft = new Button(data, "<");
		
		data.setPlot(panRightButtonX, panRightButtonY, 
				panRightButtonX+zoomButtonWidth, 
				panRightButtonY+zoomButtonHeight);
		panRight = new Button(data, ">");
		
		mapButtons.add(zoomIn);
		mapButtons.add(zoomOut);
		mapButtons.add(panLeft);
		mapButtons.add(panUp);
		mapButtons.add(panRight);
		mapButtons.add(panDown);
		
	}
	
	@Override
	protected void draw() {
		parent.pushStyle();
		parent.pushMatrix();
		parent.fill(EnumColor.OFFWHITE.getValue());
		parent.rect(plotX1, plotY1, plotX2, plotY2);
		parent.scale(zoom, zoom);
		parent.translate(leftright, updown);
		parent.image(mapImage, this.plotX1, this.plotY1, this.plotWidth, this.plotHeight);
		parent.popMatrix();
		parent.popStyle();
		
		for(Button b:mapButtons)
			b.draw();
	}
	
	public void zoomIn() {
		if(zoom < noMoreZoomOut)
			zoom++;
	}
	
	public void zoomOut() {
		if(zoom > noMoreZoomIn)
			zoom--;
	}

	public void panUp() {
		if(updown > 0)
			updown -= 30;
	}
	
	public void panDown() {
//		if(updown > 0 && )
			updown += 30;
	}
	
	public void panLeft() {
		if(leftright > 0)
			leftright -= 30;
	}
	
	public void panRight() {
		if(leftright > 0)
			leftright += 30;
	}
	
	public Button getZoomIn() {
		return zoomIn;
	}

	public Button getZoomOut() {
		return zoomOut;
	}

	public Button getPanLeft() {
		return panLeft;
	}

	public Button getPanUp() {
		return panUp;
	}

	public Button getPanRight() {
		return panRight;
	}

	public Button getPanDown() {
		return panDown;
	}
	
}
