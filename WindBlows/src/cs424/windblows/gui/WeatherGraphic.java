package cs424.windblows.gui;

import java.io.File;

import processing.core.PShape;
import cs424.windblows.application.EnumColor;
import cs424.windblows.application.Utils;
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
//		parent.fill(EnumColor.GRAY_T.getValue());
//		parent.rect(plotX1, plotY1, plotWidth, plotHeight);
		weather = parent.loadShape(Utils.getProjectPath() + File.separator + "images/weather.svg");
		parent.shape(weather, plotX1+(plotWidth/2 - scale(35)), plotY1+scale(5), 
				scale(70), scale(70));
		parent.popStyle();
	}
}
