package cs424.windblows.application;

import processing.core.PApplet;

/**
 * This class instance is used for passing data between sketches.
 * I created this class with an intention to shorten the number of 
 * parameters needed to pass sketch instances.
 * 
 * @author Gaurav
 *
 */
public class Variable {
	protected PApplet parent;
	protected float plotX1, plotX2, plotY1, plotY2;
	protected String label;
	
	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public PApplet getParent() {
		return parent;
	}

	public void setParent(PApplet parent) {
		this.parent = parent;
	}

	public float getPlotX1() {
		return plotX1;
	}

	public float getPlotX2() {
		return plotX2;
	}

	public float getPlotY1() {
		return plotY1;
	}

	public float getPlotY2() {
		return plotY2;
	}
	
	public void setPlot( float x1, float y1, float x2, float y2){
		this.plotX1 = x1;
		this.plotY1 = y1;
		this.plotX2 = x2;
		this.plotY2 = y2;
	}
}