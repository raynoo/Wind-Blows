package cs424.windblows.application;

import static cs424.windblows.application.Constants.backX1;
import static cs424.windblows.application.Constants.backX2;
import static cs424.windblows.application.Constants.backY1;
import static cs424.windblows.application.Constants.backY2;
import static cs424.windblows.application.Constants.mapPanelHeight;
import static cs424.windblows.application.Constants.mapPanelWidth;
import static cs424.windblows.application.Constants.mapPanelX;
import static cs424.windblows.application.Constants.mapPanelY;
import static cs424.windblows.application.Constants.playButtonHeight;
import static cs424.windblows.application.Constants.playButtonWidth;
import static cs424.windblows.application.Constants.playButtonX;
import static cs424.windblows.application.Constants.playButtonY;
import static cs424.windblows.application.Constants.wcHeight;
import static cs424.windblows.application.Constants.wcWidth;
import static cs424.windblows.application.Constants.wcX;
import static cs424.windblows.application.Constants.wcY;
import static cs424.windblows.application.Constants.weatherGraphicHeight;
import static cs424.windblows.application.Constants.weatherGraphicX;
import static cs424.windblows.application.Constants.weatherGraphicY;
import static cs424.windblows.application.Constants.weatherGraphiclWidth;
import static cs424.windblows.application.Constants.graphHeight;
import static cs424.windblows.application.Constants.graphWidth;
import static cs424.windblows.application.Constants.graphX;
import static cs424.windblows.application.Constants.graphY;

import java.util.ArrayList;
import java.util.List;

import omicronAPI.OmicronAPI;
import omicronAPI.OmicronTouchListener;
import processing.core.PApplet;
import processing.core.PFont;
import cs424.windblows.data.DBFacade;
import cs424.windblows.gui.BackgroundSketch;
import cs424.windblows.gui.DateInfo;
import cs424.windblows.gui.KeywordsSketch;
import cs424.windblows.gui.LineGraph;
import cs424.windblows.gui.Map;
import cs424.windblows.gui.Playback;
import cs424.windblows.gui.Sketch;
import cs424.windblows.gui.SliderSketch;
import cs424.windblows.gui.WeatherGraphic;
import cs424.windblows.gui.WordCloud;

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
//	protected PlotterSketch plotter;
	public Map map;
	protected KeywordsSketch keywords;
	protected SliderSketch slider;
	protected WordCloud wordcloud;
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
		initApp();
		
		initMap();
		initBackgroundPanel();//have to be called right after map to draw over the spilled-over map
		initKeywordPanel();
		initPlaybackPanel();
		initDateInfoPanel();
		initWeatherPanel();
		initWordCloudPanel();
		initLineGraphPanel();
		initSlider();
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
	}
	
	void initSlider(){
		// init the slider
		Variable data = new Variable();
		data.setParent(this);
		data.setPlot(width/20, (height*11)/13, Constants.mapPanelWidth, (height*11)/12);
		slider = new SliderSketch(data);
		slider.setActive(true);
		sketches.add(slider);
		slider.addListener(map);
	}
	
	
	void initKeywordPanel() {
		// init keywords sketch
		Variable data = new Variable();
		data.setParent(this);
		data.setPlot(Constants.keywordPanelX, Constants.keywordPanelY, 
				Constants.keywordPanelWidth, Constants.keywordPanelHeight);
		keywords = new KeywordsSketch(data);
		keywords.setActive(true);
		keywords.setListener(map);
		
		sketches.add(keywords);
	}
		
	void initMap() {
		Variable mapData = new Variable();
		mapData.setPlot(mapPanelX, mapPanelY, mapPanelWidth-mapPanelX, mapPanelHeight-mapPanelY);
		mapData.setParent(this);
		mapData.setLabel("Map");
		
		map = new Map(mapData);
		map.setActive(true);

		addSketch(map);
	}
	
	void initWordCloudPanel() {
		Variable data = new Variable();
		data.setPlot(wcPanelX, wcPanelY, wcPanelX+wcPanelWidth, wcPanelY+wcPanelHeight);
		data.setParent(this);
		data.setLabel("Cloud");
		
		wordcloud = new WordCloud(data);
		wordcloud.setActive(true);
		
		keywords.setListener(wordcloud);
		
		addSketch(wordcloud);
	}
	
	void initDateInfoPanel() {
		Variable data = new Variable();
		data.setPlot(datePanelX, datePanelY, datePanelX+datePanelWidth, datePanelY+datePanelHeight);
		data.setParent(this);
		
		DateInfo dateinfo = new DateInfo(data);
		dateinfo.setActive(true);
		
		addSketch(dateinfo);
	}
	
	void initWeatherPanel() {
		Variable data = new Variable();
		data.setPlot(weatherGraphicX, weatherGraphicY, weatherGraphicX+weatherGraphiclWidth, 
				weatherGraphicY+weatherGraphicHeight);
		data.setParent(this);
		WeatherGraphic weather = new WeatherGraphic(data);
		weather.setActive(true);
		addSketch(weather);
	}
	
	void initPlaybackPanel() {
		//TODO: create a sketch for the entire panel
		
		Variable b1 = new Variable();
		int x=playButtonX;
		b1.setPlot(x, playButtonY, playButtonWidth+x, playButtonHeight+playButtonY);
		b1.setParent(this);
		
		Variable b2 = new Variable();
		x += playButtonWidth+5;
		b2.setPlot(x, playButtonY, playButtonWidth+x, playButtonHeight+playButtonY);
		b2.setParent(this);
		
		Variable b3 = new Variable();
		x += playButtonWidth+5;
		b3.setPlot(x, playButtonY, playButtonWidth+x, playButtonHeight+playButtonY);
		b3.setParent(this);
		
		Variable b4 = new Variable();
		x += playButtonWidth+5;
		b4.setPlot(x, playButtonY, playButtonWidth+x, playButtonHeight+playButtonY);
		b4.setParent(this);
		
		Variable b5 = new Variable();
		x += playButtonWidth+5;
		b5.setPlot(x, playButtonY, playButtonWidth+x, playButtonHeight+playButtonY);
		b5.setParent(this);
		
		Playback playback = new Playback(b1, b2, b3, b4, b5);
		playback.setActive(true);

		addSketch(playback);
	}
	
	//to redraw the background to draw over the zoomed in map
	void initBackgroundPanel() {
		Variable data = new Variable();
		data.setPlot(backX1, backY1, backX2, backY2);
		data.setParent(this);
		
		BackgroundSketch back = new BackgroundSketch(data);
		back.setActive(true);
		
		addSketch(back);
	}
	
	void initLineGraphPanel(){
		Variable data = new Variable();
		data.setPlot(graphX, graphY, graphX+graphWidth, graphY+graphHeight);
		data.setParent(this);
				
		LineGraph graph = new LineGraph(data);
		graph.setActive(true);
		
		addSketch(graph);
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
		
		if(listener != null)
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
		
		if(listener != null)
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
		
		else if(map.isTouchValid(xPos, yPos))
			return map;
		
		else if(slider.isTouchValid(xPos, yPos)){
			System.err.println("slider called");
			return slider;
		}
		return null;
	}
}
