package cs424.windblows.gui;

import processing.core.PImage;
import cs424.windblows.application.EnumColor;
import cs424.windblows.application.Variable;

public class Map extends Sketch {

	PImage mapImage;
	
	public Map(Variable data) {
		super(data);
		this.mapImage = parent.loadImage("../images/Vastopolis_Map.png");
	}
	
	@Override
	protected void draw() {
		parent.pushStyle();
		parent.fill(EnumColor.OFFWHITE.getValue());
		parent.rect(plotX1, plotY1, plotX2, plotY2);
		parent.image(mapImage, this.plotX1, this.plotY1, this.plotWidth, this.plotHeight);
		parent.popStyle();
	}

	void zoom() {
		
	}
	
	void pan() {
		
	}
}
