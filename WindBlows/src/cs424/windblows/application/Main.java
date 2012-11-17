package cs424.windblows.application;
import java.util.ArrayList;

import omicronAPI.OmicronAPI;
import omicronAPI.OmicronTouchListener;
import processing.core.PApplet;
import processing.core.PFont;
import cs424.windblows.gui.Sketch;

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
	

	
	protected ArrayList<Sketch> sketches = new ArrayList<Sketch>();
	
	/***************************************************************/
	
	public static void main(String args[]) {
	    PApplet.main(new String[] { "cs424.carcrashes.application.Main" });
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
			//size(8160/4, 384*3);
			Constants.SCALE = 1;
		}
		// initialize the gui elements
		initApp();
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
	
		
	}

	
	@Override
	public void draw() {
		// set the background
		background(255);
	
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
		
		return null;
	}
}
