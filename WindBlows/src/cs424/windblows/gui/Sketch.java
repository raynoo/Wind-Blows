package cs424.windblows.gui;

import processing.core.PApplet;
import static cs424.windblows.application.Constants.SCALE;
import cs424.windblows.application.Variable;

/**
 * Super class for all the GUI elements in the visualization
 * @author Gaurav
 */
public class Sketch{

	/** These variables restrict the display area for this sketch. 
	 * You should try to contain the drawing based on these parameters 
	 **/
	protected float plotX1, plotX2, plotY1, plotY2;
	protected float plotWidth, plotHeight;
	
	/**
	 * This is a reference to Main instance, which has all the processing methods and variables.
	 */
	protected static PApplet parent;
	
	/**
	 * display method checks this variable before drawing.
	 */
	protected boolean isActive;
	
	public Sketch(Variable data) {
		init(data);
	}
	
	/**
	 * This method is called in the constructor to initialize the instance.
	 */
	public void init(Variable data){
		this.parent = data.getParent();
		this.plotX1 = data.getPlotX1();
		this.plotY1 = data.getPlotY1();
		this.plotX2 = data.getPlotX2();
		this.plotY2 = data.getPlotY2();
		this.plotWidth = this.plotX2 - this.plotX1;
		this.plotHeight = this.plotY2 - this.plotY1;
	}
	
	/**
	 * This method is responsible for displaying sketch on the given screen resolution. 
	 * DO NOT OVERRIDE
	 */
	public final void display(){
		if(!isActive) return;
		else draw();
	}
	
	/**
	 * This method needs to be overridden by all the subclasses
	 */
	protected void draw(){
		
	}
	
	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}
	
	public static PApplet getParent() {
		return parent;
	}
	
	public float scale(float value) {
		return value*SCALE;
	}
	
	/**
	 * Override this method to check if the touch is present in this Gui element.
	 * Not necessary when Omicron touch listener is not implemented.
	 * @param xPos
	 * @param yPos
	 * @return
	 */
	public boolean isTouchValid(float xPos, float yPos){
		return false;
	}
}
	
