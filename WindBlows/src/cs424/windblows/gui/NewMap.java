package cs424.windblows.gui;

import static cs424.windblows.application.Constants.*;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import omicronAPI.OmicronTouchListener;
import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PVector;
import cs424.windblows.application.ColorCodes;
import cs424.windblows.application.EnumColor;
import cs424.windblows.application.Filter;
import cs424.windblows.application.Utils;
import cs424.windblows.application.Variable;
import cs424.windblows.data.DBFacade;
import cs424.windblows.data.Tweet;
import cs424.windblows.listeners.FilterListener;
import cs424.windblows.listeners.MarkerListener;
import cs424.windblows.listeners.TimeChanged;

public class NewMap extends Sketch implements OmicronTouchListener, FilterListener, MarkerListener, TimeChanged {

	PImage mapImage;
	MercatorMap mercatorMap;
	
	protected ArrayList<PImage> imageList;
	protected int curImage = 0;
	//all required coordinates and step values
	float translateY = scale(10), translateX = scale(30);

	float imageCenterX, imageCenterY, zoomHeight, zoomWidth, currentCenterX, currentCenterY;
	float currentImageTopLeftX, currentImageBottomRightX, currentImageTopLeftY, currentImageBottomRightY;

	int zoomLevel = 1, noMoreZoomIn = 5, noMoreZoomOut = 1;
	Location[] boundaryLocations = new Location[2];

	//Buttons
	ArrayList<Button> mapButtons = new ArrayList<Button>();
	Button zoomIn, zoomOut, panLeft, panUp, panRight, panDown;
	Button toggleMap;
	
	//Data
	Filter curFilter;
	Date min = Utils.getDate("4/30/2011"), max = Utils.getDate("5/20/2011"), currentDate;
	ArrayList<Tweet> tweetData;
	ArrayList<Marker> markers;
	Marker currentMarker, previousMarker;
	boolean showMarkerInfo;

	boolean filterChanged = true, isPan = false;

	//list to save people
	static ArrayList<Integer> savedPeople = new ArrayList<Integer>();
	HashMap<Integer, List<Tweet>> userTweets;
	
	public NewMap(Variable data) {
		super(data);
		initMap();
		initButtons();
		
		imageList = new ArrayList<PImage>();
		imageList.add(parent.loadImage(Utils.getProjectPath() + File.separator + "images/Vastopolis_Map_greyscale.png"));
		imageList.add(parent.loadImage(Utils.getProjectPath() + File.separator + "images/Vastopolis_Map_PopulationDensity.png"));
		imageList.add(parent.loadImage(Utils.getProjectPath() + File.separator + "images/Vastopolis_Map_DayTimePopulation.png"));
		mapImage = imageList.get(curImage);
	}

	void initMap() {
		this.mapImage = parent.loadImage(Utils.getProjectPath() + File.separator + "images/Vastopolis_Map_greyscale.png");
		mercatorMap = new MercatorMap(mapWidth, mapHeight, topLeftLat, bottomRightLat, topLeftLon, bottomRightLon);
		
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
		data.setParent(Sketch.parent);
		
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
		
		data.setPlot(scale(zoomInButtonX - zoomButtonWidth * 2), scale(zoomInButtonY-(zoomButtonHeight*2) - 6), 
				scale(zoomInButtonX+zoomButtonWidth * 2), 
				scale(zoomInButtonY- zoomButtonHeight - 6));
		toggleMap = new Button(data, "Map type");
		
		
		mapButtons.add(zoomIn);
		mapButtons.add(zoomOut);
		mapButtons.add(panLeft);
		mapButtons.add(panUp);
		mapButtons.add(panRight);
		mapButtons.add(panDown);
		mapButtons.add(toggleMap);
		
	}
	
	
	public void toggleImage(){
		if((curImage + 1) == imageList.size())
			curImage = 0;
		else curImage++;
		
		mapImage = imageList.get(curImage);
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
		
		if(mercatorMap.mapScreenWidth != zoomWidth || mercatorMap.mapScreenHeight != zoomHeight)
			mercatorMap = new MercatorMap(zoomWidth, zoomHeight, topLeftLat, bottomRightLat, topLeftLon, bottomRightLon);
		
		parent.stroke(EnumColor.DARK_GRAY.getValue());
		parent.fill(EnumColor.RED.getValue());
		
		//plot points of present filters
		drawDataPoints();
		
		parent.popStyle();
		
		for(Button b:mapButtons)
			b.draw();
		
		if(showMarkerInfo) {
			currentMarker.selected = true;
			currentMarker.displayInfo();

			if(previousMarker != null)
				previousMarker.selected = false;
		}
		
		
		parent.rectMode(PApplet.CORNER);
		parent.fill(200,200,200,200);
		parent.noStroke();
		parent.rect(scale(plotX1), scale(plotY1), scale(plotWidth), scale(25));
		parent.fill(0);
		parent.text(getImageType(), scale(plotX1 + 20), scale(plotY1 + 15));
	}
	
	
	public String getImageType(){
		switch (curImage){
		case 0: return "Image Type : Greyscaled";
		case 1: return "Image Type : Population Density";
		case 2: return "Image Type : Population Density during day";
		default: return "Image Type : Greyscaled";
		}
	}
	
	public Location[] getBoundaryLatLong() {
		return boundaryLocations;
	}
	
	void updateBoundaries() {
		updateCenter();
		
		currentImageTopLeftX = imageCenterX - zoomWidth/2; currentImageBottomRightX = imageCenterX + zoomWidth/2;
		currentImageTopLeftY = imageCenterY - zoomHeight/2; currentImageBottomRightY = imageCenterY + zoomWidth/2;
		
//		mercatorMap = new MercatorMap(zoomWidth, zoomHeight, topLeftLat, bottomRightLat, topLeftLon, bottomRightLon);
		
//		float currentTopLeftLong = PApplet.map(currentImageTopLeftX, 0, zoomWidth, topLeftLon, bottomRightLon);
//		float currentTopLeftLat = PApplet.map(currentImageTopLeftY, 0, zoomHeight, topLeftLat, bottomRightLat);
//		boundaryLocations[0] = new Location(currentTopLeftLat, currentTopLeftLong);
//		System.out.println("Lat1: " + currentTopLeftLat + ", Long1: " + currentTopLeftLong);
//		
//		float currentBottomRightLong = PApplet.map(currentImageBottomRightX, 0, zoomWidth, topLeftLon, bottomRightLon);
//		float currentBottomRightLat = PApplet.map(currentImageBottomRightY, 0, zoomHeight, topLeftLat, bottomRightLat);
//		boundaryLocations[1] = new Location(currentBottomRightLat, currentBottomRightLong);
		
		boundaryLocations[0] = new Location(topLeftLat, topLeftLon);
		boundaryLocations[1] = new Location(bottomRightLat, bottomRightLon);
		
		curFilter.setBoundary(boundaryLocations);
		filterChanged = true;
	}
	
	//correct image position when zooming out of map center
	boolean updateCenter() {
		
		boolean centerCorrected = false;
		
		//if new left edge is to right of visible-area-left-edge, align image by left
		if(imageCenterX - zoomWidth/2 > this.plotX1) {
			imageCenterX = zoomWidth/2;
			centerCorrected = true;
		}
		//if new right edge is to left of visible-area-right-edge, align image by right
		else if(imageCenterX + zoomWidth/2 < mapWidth) {
			imageCenterX = scale(mapWidth) - zoomWidth/2;
			centerCorrected = true;
		}
		//if new top edge is below visible-area-top-edge, align image by top
		if(imageCenterY - zoomHeight/2 > this.plotY1) {
			imageCenterY = zoomHeight/2;
			centerCorrected = true;
		}
		//if new bottom edge is above visible-area-bottom-edge, align image by bottom
		else if(imageCenterY + zoomHeight/2 < mapHeight) {
			imageCenterY = scale(mapHeight) - zoomHeight/2;
			centerCorrected = true;
		}
		return centerCorrected;
	}
	
	public void drawDataPoints() {
		// loop through the tweet's in the list
		if(filterChanged) {
			getData();
		
			markers = new ArrayList<Marker>();
			
			for(Tweet t : tweetData) {
				float x = mercatorMap.getScreenX((float)t.getLon());
				float y = mercatorMap.getScreenY((float)t.getLat());
				
				PVector coord =  mercatorMap.getScreenLocation(new PVector((float)t.getLon(), (float)t.getLat()));
				
				markers.add(new Marker(x, y, Utils.scale(4), Sketch.parent, t.getTweetID(), t.getCategoryId()));
			}
			filterChanged = false;
		}
		
		for(Marker m : markers) {
			m.setColor(ColorCodes.getColor(m.getCategoryID()));
			m.setListener(this);
			m.draw();
		}
//		System.out.println("Filtered data size: " + markers.size());
	}
	
	public void getData() {
		if(curFilter == null) {
			curFilter = new Filter();
			currentDate = min;
			curFilter.setDate(currentDate);
			curFilter.setCondition(KeywordsSketch.OR);
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
		if(zoomLevel != 1) {
			imageCenterY += translateY;
			
			boolean centerCorrected = updateCenter();
			
			if(!centerCorrected)
				for(Marker m : markers)
					m.centerY += translateY;
		
//		isPan = true;
//		updateBoundaries();
		}
	}
	
	public void panDown() {
		if(zoomLevel != 1) {
			imageCenterY -= translateY;
		
		boolean centerCorrected = updateCenter();
		
		if(!centerCorrected)
			for(Marker m : markers)
				m.centerY -= translateY;
		
//		isPan = true;
//		updateBoundaries();
		}
	}
	
	public void panLeft() {
		if(zoomLevel != 1) {
			imageCenterX += translateX;
		
		boolean centerCorrected = updateCenter();
		
		if(!centerCorrected)
			for(Marker m : markers)
				m.centerX += translateX;
		
//		isPan = true;
//		updateBoundaries();
		}
	}
	
	public void panRight() {
		if(zoomLevel != 1) {
			imageCenterX -= translateX;
		
			boolean centerCorrected = updateCenter();
			
			if(!centerCorrected)
				for(Marker m : markers)
					m.centerX -= translateX;
		
//		isPan = true;
//		updateBoundaries();
		
		}
	}
	
	@Override
	public void markerSelected(Marker m) {
		this.previousMarker = this.currentMarker;
		this.currentMarker = m;
		this.showMarkerInfo = true;
	}

	@Override
	public void markerUserSelected(Marker m) {
		if(addUser(m.getUserID()))
			m.infoPanel.addUser.setPressed(true);
	}

	@Override
	public void markerUserUnselected(Marker m) {
		removeUser(m.getUserID());
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
	public void dateChanged(Date date) {
		this.currentDate = date;
		System.out.println(date);
		curFilter.setDate(date);
		filterChanged = true;
	}

	@Override
	public void conditionChanged(int condition) {
		curFilter.setCondition(condition);
		filterChanged = true;
	}

	@Override
	public boolean isTouchValid(float xPos, float yPos) {
		if(Utils.isPresent(plotX1, mapWidth, xPos)
				&& Utils.isPresent(plotY1, mapHeight, yPos))
			return true;
		else return false;
	}
	
	@Override
	public void touchDown(int arg0, float arg1, float arg2, float arg3,
			float arg4) {
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
		else if(toggleMap.containsPoint(arg1, arg2))
			toggleImage();
		//on map
		else {
			
			if(currentMarker != null &&
				currentMarker.infoPanel != null && 
				currentMarker.infoPanel.containsPoint(arg1, arg2)) {
				currentMarker.infoPanel.touchDown(arg0, arg1, arg2, arg3, arg4);
				return;
			}
			
			for(Marker m : markers) {
				if(m.containsPoint(arg1, arg2)) {
					m.selected = true;
					m.touchDown(arg0, arg1, arg2, arg3, arg4);
					return;
				}
			}
			
			filterChanged = true;
			System.out.println(currentDate);
			showMarkerInfo = false;
		}
	}
	
	public HashMap<Integer, List<Tweet>> getSavedPeoplesTweets() {
		userTweets = new HashMap<Integer, List<Tweet>>();
		
		for(int id : savedPeople) {
			userTweets.put(id, DBFacade.getInstance().getTweetsByUser(id));
		}
		return userTweets;
	}
	
	public static ArrayList<Integer> getSavedPeople() {
		return savedPeople;
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
	
	public boolean addUser(int id) {
		if(! this.savedPeople.contains(id)) {
			System.out.println("Map.markerUserSelected(): Added user " + id);
			this.savedPeople.add(id);
			return true;
		}
		return false;
	}
	
	public void removeUser(int id) {
		this.savedPeople.remove(new Integer(id));
	}

	@Override
	public void timeChanged(int id) {
		curFilter.setTime(id);
		filterChanged = true;
	}
	
}
