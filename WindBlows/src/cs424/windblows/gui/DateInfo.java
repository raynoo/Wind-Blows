package cs424.windblows.gui;

import processing.core.PConstants;
import cs424.windblows.application.EnumColor;
import cs424.windblows.application.Variable;
import cs424.windblows.listeners.FilterListener;

public class DateInfo extends Sketch {

	FilterListener listener;

	public DateInfo(Variable data) {
		super(data);
	}
	
	@Override
	protected void draw() {
		parent.pushStyle();
//		parent.fill(EnumColor.GRAY_T.getValue());
//		parent.rect(plotX1, plotY1, plotWidth, plotHeight);
		parent.fill(EnumColor.BLACK.getValue());
		parent.textSize(scale(20));
		parent.textAlign(PConstants.CENTER, PConstants.TOP);
		parent.text("04/30/2011", plotX1+plotWidth/2, plotY1+10);
		parent.popStyle();
	}

	public FilterListener getListener() {
		return listener;
	}

	public void setListener(FilterListener listener) {
		this.listener = listener;
	}
}
