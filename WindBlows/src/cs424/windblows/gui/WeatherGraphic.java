package cs424.windblows.gui;

import processing.core.PShape;
import cs424.windblows.application.EnumColor;
import cs424.windblows.application.Variable;

public class WeatherGraphic extends Sketch {

	PShape weather;
	PShape windDirection;
	
	public WeatherGraphic(Variable data) {
		super(data);
	}

	@Override
	protected void draw() {
		parent.pushStyle();
		parent.fill(EnumColor.GRAY_T.getValue());
		parent.rect(plotX1, plotY1, plotWidth, plotHeight);
		weather = parent.loadShape("../images/weather.svg");
		parent.shape(weather, plotX1+10, plotY1+10, plotWidth-20, plotHeight-20);
		parent.popStyle();
	}
}
