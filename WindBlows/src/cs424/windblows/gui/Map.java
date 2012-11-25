package cs424.windblows.gui;

import static cs424.windblows.application.Constants.*;

import java.util.ArrayList;

import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PVector;
import cs424.windblows.application.EnumColor;
import cs424.windblows.application.Variable;

public class Map extends Sketch {

	PImage mapImage;
	
	int translateY = 10, translateX = 30;
	float centerX, centerY, zoomHeight, zoomWidth, currentCenterX, currentCenterY;
	float currentTopLeftX, currentBottomRightX, currentTopLeftY, currentBottomRightY;
	
	int zoomLevel = 1, noMoreZoomIn = 5, noMoreZoomOut = 1;
	
	Location[] boundaryLocations = new Location[2];
	
	ArrayList<Button> mapButtons = new ArrayList<Button>();
	Button zoomIn, zoomOut, panLeft, panUp, panRight, panDown;
	
	public Map(Variable data) {
		super(data);
		this.mapImage = parent.loadImage("../images/Vastopolis_Map.png");
		this.centerX = mapWidth/2;
		this.centerY = mapHeight/2;
		this.zoomWidth = mapWidth;
		this.zoomHeight = mapHeight;
		
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
		parent.rectMode(PApplet.CORNERS);
		parent.fill(EnumColor.DARK_GRAY.getValue());
		parent.noStroke();
		parent.rect(plotX1, plotY1, plotX2, plotY2);
		
		parent.imageMode(PApplet.CENTER);
		parent.image(mapImage, centerX, centerY, zoomWidth, zoomHeight);
		parent.popStyle();
		
		for(Button b:mapButtons)
			b.draw();
		
		parent.pushStyle();
		parent.stroke(EnumColor.DARK_GRAY.getValue());
		parent.fill(EnumColor.RED.getValue());
		
		//draw truck crash location
		updateBoundaries();
		drawPoint(42.22655f, 93.42752f);

		parent.fill(EnumColor.DARK_GRAY.getValue());
		parent.rect(mapPanelX, mapHeight, plotX2, plotY2);
		parent.rect(mapWidth, plotY1, plotX2, plotY2);
		parent.popStyle();
	}
	
	public Location[] getBoundaryLatLong() {
		return boundaryLocations;
	}
	
	void updateBoundaries() {
//		if(zoomLevel>1)
			updateCenter();
		
		currentTopLeftX = centerX - zoomWidth/2; currentBottomRightX = centerX + zoomWidth/2;;
		currentTopLeftY = centerY - zoomHeight/2; currentBottomRightY = centerY + zoomWidth/2;
		
		float currentTopLeftLong = parent.map(currentTopLeftX, 0, zoomWidth, topLeftLon, bottomRightLon);
		float currentTopLeftLat = parent.map(currentTopLeftY, 0, zoomHeight, topLeftLat, bottomRightLat);
		boundaryLocations[0] = new Location(currentTopLeftLat, currentTopLeftLong);
		
		float currentBottomRightLong = parent.map(currentBottomRightX, 0, zoomWidth, topLeftLon, bottomRightLon);
		float currentBottomRightLat = parent.map(currentBottomRightY, 0, zoomHeight, topLeftLat, bottomRightLat);
		boundaryLocations[1] = new Location(currentBottomRightLat, currentBottomRightLong);
		
//		System.out.println("topLeft: " + boundaryLocations[0]);
//		System.out.println("bottomRight: " + boundaryLocations[1]);
	}
	
	//correct image position when zooming out of map center
	void updateCenter() {
		//if new left edge is to right of visible-area-left-edge, align image by left edge
		if(centerX - zoomWidth/2 > this.plotX1)
			centerX = zoomWidth/2;
		
		//elsewhere, if new right edge is to left of visible-area-right-edge, align image by right
		else if(centerX + zoomWidth/2 < mapWidth)
			centerX = mapWidth - zoomWidth/2;
		
		if(centerY - zoomHeight/2 > this.plotY1)
			centerY = zoomHeight/2;
		
		else if(centerY + zoomHeight/2 < mapHeight)
			centerY = mapHeight - zoomHeight/2;
	}
	
	public void drawPoint(float lat, float lon) {
		float x = parent.map(lon, topLeftLon, bottomRightLon, currentTopLeftX, currentBottomRightX);
		float y = parent.map(lat, topLeftLat, bottomRightLat, currentTopLeftY, currentBottomRightY);
		
		parent.pushStyle();
		parent.stroke(EnumColor.DARK_GRAY.getValue());
		parent.fill(EnumColor.RED.getValue());
		parent.ellipse(x, y, 10, 10);
		parent.popStyle();
	}
	
	public void drawPoints() {
		
	}
	
	public void zoomIn() {
		if(zoomLevel < noMoreZoomIn) {
			zoomWidth *= 1.5f;
			zoomHeight *= 1.5f;
			
			zoomLevel++;
			updateBoundaries();
		}
	}
	
	public void zoomOut() {
		if(zoomLevel > noMoreZoomOut) {
			zoomWidth /= 1.5f;
			zoomHeight /= 1.5f;

			zoomLevel--;
			updateBoundaries();
		}
	}

	public void panUp() {
		if(zoomLevel != 1)
			centerY += translateY;
		
		updateBoundaries();
	}
	
	public void panDown() {
		if(zoomLevel != 1)
			centerY -= translateY;
		
		updateBoundaries();
	}
	
	public void panLeft() {
		if(zoomLevel != 1)
			centerX += translateX;
		
		updateBoundaries();
	}
	
	public void panRight() {
		if(zoomLevel != 1)
			centerX -= translateX;
		
		updateBoundaries();
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
