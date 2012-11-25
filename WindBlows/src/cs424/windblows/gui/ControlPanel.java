package cs424.windblows.gui;

import cs424.windblows.application.EnumColor;
import cs424.windblows.application.Variable;

public class ControlPanel extends Sketch {

	public ControlPanel(Variable data) {
		super(data);
	}

	@Override
	protected void draw() {
		parent.pushStyle();
		parent.fill(EnumColor.GRAY_T.getValue());
		parent.rect(plotX1, plotY1, plotWidth, plotHeight);
		parent.popStyle();
	}
	
	
}
