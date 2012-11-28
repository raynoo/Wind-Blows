package cs424.windblows.gui;

import static cs424.windblows.application.Constants.*;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;

import omicronAPI.OmicronTouchListener;

import processing.core.PApplet;
import processing.core.PImage;
import cs424.windblows.application.EnumColor;
import cs424.windblows.application.Filter;
import cs424.windblows.application.Utils;
import cs424.windblows.application.Variable;
import cs424.windblows.data.DBFacade;
import cs424.windblows.data.Tweet;
import cs424.windblows.listeners.FilterListener;
import cs424.windblows.listeners.MarkerListener;

public class Map extends Sketch implements OmicronTouchListener, FilterListener, MarkerListener {

	PImage mapImage;
	
	//all required coordinates and step values
	float translateY = scale(10), translateX = scale(30);
	
	float imageCenterX, imageCenterY, zoomHeight, zoomWidth, currentCenterX, currentCenterY;
	float currentImageTopLeftX, currentImageBottomRightX, currentImageTopLeftY, currentImageBottomRightY;
	
	int zoomLevel = 1, noMoreZoomIn = 5, noMoreZoomOut = 1;
	Location[] boundaryLocations = new Location[2];
	
	//Buttons
	ArrayList<Button> mapButtons = new ArrayList<Button>();
	Button zoomIn, zoomOut, panLeft, panUp, panRight, panDown;
	
	//Data
	Filter curFilter;
	Date min = Utils.getDate("4/30/2011"), max = Utils.getDate("5/20/2011"), currentDate;
	ArrayList<Tweet> tweetData;
	ArrayList<Marker> markers;
	Marker currentMarker;
	boolean showMarkerInfo;
	
	boolean filterChanged = true;
	
	public Map(Variable data) {
		super(data);
		initMap();
		initButtons();
	}
	
	void initMap() {
		this.mapImage = parent.loadImage(Utils.getProjectPath() + File.separator + "images/Vastopolis_Map_greyscale.png");
		this.imageCenterX = scale(mapWidth)/2;
		this.imageCenterY = scale(mapHeight)/2;
		this.zoomWidth = scale(mapWidth);
		this.zoomHeight = scale(mapHeight);
		
		this.currentImageTopLeftX = imageCenterX - zoomWidth/2;
		this.currentImageBottomRightX = imageCenterX + zoomWidth/2;
		this.currentImageTopLeftY = imageCenterY - zoomHeight/2;
		this.currentImageBottomRightY = imageCenterY + zoomWidth/2;
	}
	
	void initButtons() {
		Variable data = new Variable();
		data.setParent(this.parent);
		
		data.setPlot(scale(zoomInButtonX), scale(zoomInButtonY-zoomButtonHeight-scale(3)), 
				scale(zoomInButtonX+zoomButtonWidth), 
				scale(zoomInButtonY-scale(3)));
		panUp = new Button(data, "^");
		
		data.setPlot(scale(zoomInButtonX), scale(zoomInButtonY), 
				scale(zoomInButtonX+zoomButtonWidth), 
				scale(zoomInButtonY+zoomButtonHeight));
		zoomIn = new Button(data, "+");
		
		data.setPlot(scale(zoomInButtonX), scale(zoomInButtonY+zoomButtonHeight), 
				scale(zoomInButtonX+zoomButtonWidth), 
				scale(zoomInButtonY+zoomButtonHeight*2));
		zoomOut = new Button(data, "-");
		
		data.setPlot(scale(zoomInButtonX), scale(zoomInButtonY+zoomButtonHeight*2+scale(3)), 
				scale(zoomInButtonX+zoomButtonWidth), 
				scale(zoomInButtonY+zoomButtonHeight*3+scale(3)));
		panDown = new Button(data, "v");
		
		data.setPlot(scale(panLeftButtonX), scale(panLeftButtonY), 
				scale(panLeftButtonX+zoomButtonWidth), 
				scale(panLeftButtonY+zoomButtonHeight));
		panLeft = new Button(data, "<");
		
		data.setPlot(scale(panRightButtonX), scale(panRightButtonY), 
				scale(panRightButtonX+zoomButtonWidth), 
				scale(panRightButtonY+zoomButtonHeight));
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
		parent.rect(scale(plotX1), scale(plotY1), scale(plotX2), scale(plotY2));
		
		parent.imageMode(PApplet.CENTER);
		parent.image(mapImage, scale(imageCenterX), scale(imageCenterY), scale(zoomWidth), scale(zoomHeight));
		
		parent.stroke(EnumColor.DARK_GRAY.getValue());
		parent.fill(EnumColor.RED.getValue());
		
		//plot points of present filters
		drawDataPoints();
		
		parent.popStyle();
		
		for(Button b:mapButtons)
			b.draw();
		
		if(showMarkerInfo)
			currentMarker.displayInfo();
	}
	
	public Location[] getBoundaryLatLong() {
		return boundaryLocations;
	}
	
	void updateBoundaries() {
		updateCenter();
				
		currentImageTopLeftX = imageCenterX - zoomWidth/2; currentImageBottomRightX = imageCenterX + zoomWidth/2;
		currentImageTopLeftY = imageCenterY - zoomHeight/2; currentImageBottomRightY = imageCenterY + zoomWidth/2;
		
		float currentTopLeftLong = PApplet.map(currentImageTopLeftX, 0, zoomWidth, topLeftLon, bottomRightLon);
		float currentTopLeftLat = PApplet.map(currentImageTopLeftY, 0, zoomHeight, topLeftLat, bottomRightLat);
		boundaryLocations[0] = new Location(currentTopLeftLat, currentTopLeftLong);
		System.out.println("Lat1: " + currentTopLeftLat + ", Long1: " + currentTopLeftLong);
		
		float currentBottomRightLong = PApplet.map(currentImageBottomRightX, 0, zoomWidth, topLeftLon, bottomRightLon);
		float currentBottomRightLat = PApplet.map(currentImageBottomRightY, 0, zoomHeight, topLeftLat, bottomRightLat);
		boundaryLocations[1] = new Location(currentBottomRightLat, currentBottomRightLong);
		
		curFilter.setBoundary(boundaryLocations);
		filterChanged = true;
	}
	
	//correct image position when zooming out of map center
	void updateCenter() {
		//if new left edge is to right of visible-area-left-edge, align image by left
		if(imageCenterX - zoomWidth/2 > this.plotX1)
			imageCenterX = zoomWidth/2;
		
		//if new right edge is to left of visible-area-right-edge, align image by right
		else if(imageCenterX + zoomWidth/2 < mapWidth)
			imageCenterX = scale(mapWidth) - zoomWidth/2;
		
		//if new top edge is below visible-area-top-edge, align image by top
		if(imageCenterY - zoomHeight/2 > this.plotY1)
			imageCenterY = zoomHeight/2;
		
		//if new bottom edge is above visible-area-bottom-edge, align image by bottom
		else if(imageCenterY + zoomHeight/2 < mapHeight)
			imageCenterY = scale(mapHeight) - zoomHeight/2;
	}
	
	public void drawPoint(float lat, float lon) {
		float x = PApplet.map(lon, topLeftLon, bottomRightLon, currentImageTopLeftX, currentImageBottomRightX);
		float y = PApplet.map(lat, topLeftLat, bottomRightLat, currentImageTopLeftY, currentImageBottomRightY);
		
		parent.pushStyle();
		parent.stroke(EnumColor.DARK_GRAY.getValue());
		parent.fill(EnumColor.RED.getValue());
		parent.ellipse(x, y, scale(10), scale(10));
		parent.popStyle();
	}
	
	public void drawDataPoints() {
		// loop through the tweet's in the list
		if(filterChanged) {
			getData();
			markers = new ArrayList<Marker>();
			
			for(Tweet t : tweetData) {
				float x = PApplet.map((float)t.getLon(), topLeftLon, bottomRightLon, currentImageTopLeftX, currentImageBottomRightX);
				float y = PApplet.map((float)t.getLat(), topLeftLat, bottomRightLat, currentImageTopLeftY, currentImageBottomRightY);

				markers.add(new Marker(x, y, Utils.scale(10), this.parent, t.getTweetID()));
			}
			filterChanged = false;
		}
		
		for(Marker m : markers) {
			m.setColor(EnumColor.RED_T.getValue());
			m.setListener(this);
			m.draw();
		}
//		System.out.println("Filtered data size: " + markers.size());
	}
	
	//keyword panel can update this way
	public void setMarkers(ArrayList<Marker> markers) {
		this.markers = markers;
	}
	
	public void getData() {
		if(curFilter == null) {
			curFilter = new Filter();
			currentDate = min;
			curFilter.setDate(currentDate);
		}
		tweetData = DBFacade.getInstance().getTweets(curFilter);
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
			imageCenterY += translateY;
		
		updateBoundaries();
	}
	
	public void panDown() {
		if(zoomLevel != 1)
			imageCenterY -= translateY;
		
		updateBoundaries();
	}
	
	public void panLeft() {
		if(zoomLevel != 1)
			imageCenterX += translateX;
		
		updateBoundaries();
	}
	
	public void panRight() {
		if(zoomLevel != 1)
			imageCenterX -= translateX;
		
		updateBoundaries();
	}
	
	@Override
	public void categoryAdded(int categoryId) {
		curFilter.addCategory(categoryId);
		filterChanged = true;
	}
	

	@Override
	public void categoryRemoved(int categoryId) {
		curFilter.removeCategory(categoryId);
		filterChanged = true;
	}
	

	@Override
	public void touchDown(int arg0, float arg1, float arg2, float arg3, float arg4) {
		
		//on buttons
		if(zoomIn.containsPoint(arg1, arg2))
			zoomIn();
		else if(zoomOut.containsPoint(arg1, arg2))
			zoomOut();
		else if(panUp.containsPoint(arg1, arg2))
			panUp();
		else if(panDown.containsPoint(arg1, arg2))
			panDown();
		else if(panLeft.containsPoint(arg1, arg2))
			panLeft();
		else if(panRight.containsPoint(arg1, arg2))
			panRight();
		
		//on map
		else {
			
			for(Marker m : markers) {
				if(m.containsPoint(arg1, arg2)) {
					m.touchDown(arg0, arg1, arg2, arg3, arg4);
					return;
				}
			}
			
//			if(currentDate.equals(max)) {
//				currentDate = min;
//				curFilter.setDate(min);
//			}
//			else {
//				currentDate = Utils.addDays(currentDate, 1);
//				curFilter.setDate(currentDate);
//			}
			filterChanged = true;
			System.out.println(currentDate);
			showMarkerInfo = false;
		}
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
	
	@Override
	public boolean isTouchValid(float xPos, float yPos) {
		if(Utils.isPresent(plotX1, mapWidth, xPos)
				&& Utils.isPresent(plotY1, mapHeight, yPos))
			return true;
		else return false;
	}
	
	@Override
	public void dateChanged(Date date) {
		this.currentDate = date;
		System.out.println(date);
		curFilter.setDate(date);
		filterChanged = true;
	}

	@Override
	public void markerSelected(Marker m) {
		this.currentMarker = m;
		this.showMarkerInfo = true;
	}
}
