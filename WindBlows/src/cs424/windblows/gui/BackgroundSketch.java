package cs424.windblows.gui;

import cs424.windblows.application.EnumColor;
import cs424.windblows.application.Variable;

public class BackgroundSketch extends Sketch {

	public BackgroundSketch(Variable data) {
		super(data);
	}

	@Override
	protected void draw() {
		parent.pushStyle();
		parent.fill(EnumColor.DARK_GRAY.getValue());
		parent.noStroke();
		parent.rect(plotX1, plotY1, plotWidth, plotWidth);
		parent.popStyle();
	}
}
