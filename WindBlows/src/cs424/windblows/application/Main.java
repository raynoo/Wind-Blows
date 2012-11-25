package cs424.windblows.application;

import java.util.ArrayList;
import java.util.List;

import omicronAPI.OmicronAPI;
import omicronAPI.OmicronTouchListener;
import processing.core.PApplet;
import processing.core.PFont;
import cs424.windblows.data.DBFacade;
import cs424.windblows.gui.ControlPanel;
import cs424.windblows.gui.KeywordsSketch;
import cs424.windblows.gui.Map;
import cs424.windblows.gui.PlotterSketch;
import cs424.windblows.gui.Playback;
import cs424.windblows.gui.Sketch;
import cs424.windblows.gui.WeatherGraphic;
import cs424.windblows.listeners.MapListener;

import static cs424.windblows.application.Constants.*;

/**
 * TODO
 * 		
 * @author Gaurav
 */
public class Main extends PApplet implements OmicronTouchListener {

	// leave this alone
	private static final long serialVersionUID = 1L;
	
	/*************************CHANGE THIS FOR WALL******************/
	protected boolean usingWall = false;
	/***************************************************************/
	
	protected OmicronAPI omicronManager;
	protected PFont plotFont;
	
	
	/*************************SKETCHES******************************/
	protected PlotterSketch plotter;
	public Map map;
	protected KeywordsSketch keywords;
	
	protected ArrayList<Sketch> sketches = new ArrayList<Sketch>();
	
	/***************************************************************/
	
	public static void main(String args[]) {
	    PApplet.main(new String[] { "cs424.windblows.application.Main" });
	}
	
	public void init() {
		super.init();
		omicronManager = new OmicronAPI(this);
		
		if(usingWall) 
			omicronManager.setFullscreen(true);
	}
	
	@Override
	public void setup() {
		if( usingWall ){ 
			size( 8160, 2304 );
			omicronManager.ConnectToTracker(7001, 7340, "131.193.77.159");
			Constants.SCALE = 6;
		}
		else {
			size(8160/6, 384);
			Constants.SCALE = 1;
		}
		// initialize the gui elements
		initMap();
		initApp();
		initPlaybackButtons();
		//initKeywordPanel();
		initWeatherPanel();
		omicronManager.setTouchListener(this);
	}
	
	/**
	 * Initializes all the sketch elements in the visualization.
	 * Called in setup, should handle all the initialization for the application related to GUI.
	 * 
	 */
	protected void initApp(){
		
		// set the font
		plotFont = createFont( "Helvetica", Utils.scale(12));
		textFont(plotFont);
		
		// initialize dbfacade
		DBFacade.setPApplet(this);
		
		Variable data = new Variable();
		data.setParent(this);
		data.setPlot(0,0, (width * 3)/4, height);
	
		// set plotter sketch
		data.setPlot(Constants.mapPanelX, Constants.mapPanelY, Constants.mapPanelWidth, Constants.mapPanelHeight);
		plotter = new PlotterSketch(data);
		plotter.setActive(true);
		sketches.add(plotter);
		
		// init keywords sketch
		data.setPlot(Constants.keywordPanelX, Constants.keywordPanelY, 
				(Constants.keywordPanelWidth * 3)/2, Constants.keywordPanelHeight);
		keywords = new KeywordsSketch(data);
		keywords.setActive(true);
		keywords.setListener(plotter);
		sketches.add(keywords);
		
		// set map sketch
	//	map = new MapSkectch(data);
	//	map.setActive(true);
	//	sketches.add(map);
		
		
	}

		
	void initMap() {
		Variable mapData = new Variable();
		mapData.setPlot(Constants.mapPanelX, Constants.mapPanelY, Constants.mapPanelWidth-Constants.mapPanelX, Constants.mapPanelHeight-Constants.mapPanelY);
		mapData.setParent(this);
		mapData.setLabel("Map");
		
		map = new Map(mapData);
		map.setActive(true);

		addSketch(map);
	}
	
	void initKeywordPanel() {
		Variable data = new Variable();
		data.setPlot(Constants.keywordPanelX, Constants.keywordPanelY, Constants.keywordPanelX+Constants.keywordPanelWidth, 
				Constants.keywordPanelY+Constants.keywordPanelHeight);
		data.setParent(this);
		ControlPanel cp = new ControlPanel(data);
		cp.setActive(true);
		addSketch(cp);
	}
	
	void initWeatherPanel() {
		Variable data = new Variable();
		data.setPlot(Constants.weatherGraphicX, Constants.weatherGraphicY, Constants.weatherGraphicX+Constants.weatherGraphiclWidth, 
				Constants.weatherGraphicY+Constants.weatherGraphicHeight);
		data.setParent(this);
		WeatherGraphic weather = new WeatherGraphic(data);
		weather.setActive(true);
		addSketch(weather);
	}
	
	void initPlaybackButtons() {
		
		Variable b1 = new Variable();
		int x=Constants.playButtonX;
		b1.setPlot(x, Constants.playButtonY, Constants.playButtonWidth+x, Constants.playButtonHeight+Constants.playButtonY);
		b1.setParent(this);
		
		Variable b2 = new Variable();
		x += Constants.playButtonWidth+5;
		b2.setPlot(x, Constants.playButtonY, Constants.playButtonWidth+x, Constants.playButtonHeight+Constants.playButtonY);
		b2.setParent(this);
		
		Variable b3 = new Variable();
		x += Constants.playButtonWidth+5;
		b3.setPlot(x, Constants.playButtonY, Constants.playButtonWidth+x, Constants.playButtonHeight+Constants.playButtonY);
		b3.setParent(this);
		
		Variable b4 = new Variable();
		x += Constants.playButtonWidth+5;
		b4.setPlot(x, Constants.playButtonY, Constants.playButtonWidth+x, Constants.playButtonHeight+Constants.playButtonY);
		b4.setParent(this);
		
		Variable b5 = new Variable();
		x += Constants.playButtonWidth+5;
		b5.setPlot(x, Constants.playButtonY, Constants.playButtonWidth+x, Constants.playButtonHeight+Constants.playButtonY);
		b5.setParent(this);
		
		Playback playback = new Playback(b1, b2, b3, b4, b5);
		playback.setActive(true);

		addSketch(playback);
	}
	
	void addSketch(Sketch sketch) {
		this.sketches.add(sketch);
	}

	void addSketches(List<Sketch> sketch) {
		this.sketches.addAll(sketch);
	}
	
	@Override
	public void draw() {
		// set the background
		background(EnumColor.DARK_GRAY.getValue());
	
		// display all the GUI elements
		for(Sketch sk : sketches){
			sk.display();
		}
		
		omicronManager.process();
	}
	
	
	@Override
	public void touchDown(int ID, float xPos, float yPos, float xWidth, float yWidth){
		OmicronTouchListener listener = getValidListener(xPos, yPos);
		listener.touchDown(ID, xPos, yPos, xWidth, yWidth);
	}

	@Override
	public void touchMove(int ID, float xPos, float yPos, float xWidth, float yWidth){
		OmicronTouchListener listener = getValidListener(xPos, yPos);
		if(listener != null)
			listener.touchMove(ID, xPos, yPos, xWidth, yWidth);
	}

	@Override
	public void touchUp(int ID, float xPos, float yPos, float xWidth, float yWidth){
		OmicronTouchListener listener = getValidListener(xPos, yPos);
		listener.touchUp(ID, xPos, yPos, xWidth, yWidth);
	}
	
	/**
	 * Returns the valid sketch based on the type of listener.
	 * 
	 * @param xPos
	 * @param yPos
	 * @return
	 */
	public OmicronTouchListener getValidListener(float xPos, float yPos){
		
		if(keywords.isTouchValid(xPos, yPos)){
			return keywords;
		}
		else if(plotter.isTouchValid(xPos, yPos)){
			return plotter;
		}
		
		//if(map.containsPoint(xPos, yPos))
			return new MapListener(this);
		//return null;
	}
}
