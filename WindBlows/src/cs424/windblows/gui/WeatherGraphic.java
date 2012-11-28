package cs424.windblows.gui;

import java.io.File;
import java.util.Date;
import java.util.HashMap;

import processing.core.PApplet;
import processing.core.PShape;
import cs424.windblows.application.Constants;
import cs424.windblows.application.Utils;
import cs424.windblows.application.Variable;
import cs424.windblows.data.DBFacade;
import cs424.windblows.data.Weather;
import cs424.windblows.listeners.FilterListener;

public class WeatherGraphic extends Sketch implements FilterListener{

	protected Date currDate;
	protected Weather curWeather;
	protected HashMap<Date, Weather> data;
	
	protected PShape clear, cloudy, showers, rain;
	protected PShape E, N, W, NW, SE;
	
	protected float xWeather, yWeather, imageSize, xWind, yWind, windImage;
	PShape weather;
	PShape windDirection;
	
	public WeatherGraphic(Variable data) {
		super(data);
		xWeather = plotX1+(plotWidth/2 - scale(35));
		yWeather = plotY1+scale(5);
		imageSize = scale(50);
		
		xWind = plotX1;
		yWind = plotY1+scale(30);
		windImage = scale(20);
		
		initImages();
		
		currDate = Utils.getDate(Constants.minDate);
		this.data = DBFacade.getInstance().getWeatherData();
		
		
		curWeather = this.data.get(currDate);
		
	}

	@Override
	protected void draw() {
		parent.pushStyle();
		//weather = parent.loadShape(Utils.getProjectPath() + File.separator + "images/clear.svg");
		parent.shape(getImage(curWeather.getWeather()), xWeather, yWeather, imageSize, imageSize);
		parent.shape(getImage(curWeather.getWindDirection()), xWind, yWind, windImage, windImage);
		
		parent.fill(0);
		parent.textAlign(PApplet.CENTER);
		parent.textSize(scale(13));
		parent.text(getString(curWeather.getWeather()), xWeather + imageSize/2, yWeather + scale(70));
		parent.text(getString(curWeather.getWindDirection()), xWind , yWind + scale(45));
		parent.popStyle();
	}
	
	
	protected void initImages(){
		clear = parent.loadShape(Utils.getProjectPath() + File.separator + "images/clear.svg");
		cloudy = parent.loadShape(Utils.getProjectPath() + File.separator + "images/cloudy2.svg");
		showers = parent.loadShape(Utils.getProjectPath() + File.separator + "images/showers.svg");
		rain = parent.loadShape(Utils.getProjectPath() + File.separator + "images/rain.svg");
		
		E = parent.loadShape(Utils.getProjectPath() + File.separator + "images/east.svg");
		N = parent.loadShape(Utils.getProjectPath() + File.separator + "images/north.svg");
		W = parent.loadShape(Utils.getProjectPath() + File.separator + "images/west.svg");
		NW = parent.loadShape(Utils.getProjectPath() + File.separator + "images/northwest.svg");
		SE = parent.loadShape(Utils.getProjectPath() + File.separator + "images/southeast.svg");
	}

	@Override
	public void categoryAdded(int categoryId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void categoryRemoved(int categoryId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dateChanged(Date date) {
		curWeather = data.get(date);
	}

	@Override
	public void conditionChanged(int condition) {
		// TODO Auto-generated method stub
		
	}
	
	
	protected PShape getImage(String val){
		if(val.equalsIgnoreCase("cloudy")){
			return cloudy;
		}
		else if(val.equalsIgnoreCase("clear")){
			return clear;
		}
		else if(val.equalsIgnoreCase("showers")){
			return showers;
		}
		else if(val.equalsIgnoreCase("rain")){
			return rain;
		}
		else if(val.equalsIgnoreCase("N")){
			return N;
		}
		else if(val.equalsIgnoreCase("E")){
			return E;
		}
		else if(val.equalsIgnoreCase("W")){
			return W;
		}
		else if(val.equalsIgnoreCase("SE")){
			return SE;
		}
		else if(val.equalsIgnoreCase("NW") || 
				val.equalsIgnoreCase("WNW") ||
				val.equalsIgnoreCase("NNW") ){
			return NW;
		}
		return null;
	}
	

	protected String getString(String val){
		if(val.equalsIgnoreCase("cloudy")){
			return "Cloudy";
		}
		else if(val.equalsIgnoreCase("clear")){
			return "Clear";
		}
		else if(val.equalsIgnoreCase("showers")){
			return "Showers";
		}
		else if(val.equalsIgnoreCase("rain")){
			return "Rainy";
		}
		else if(val.equalsIgnoreCase("N")){
			return "North";
		}
		else if(val.equalsIgnoreCase("E")){
			return "East";
		}
		else if(val.equalsIgnoreCase("W")){
			return "West";
		}
		else if(val.equalsIgnoreCase("SE")){
			return "South East";
		}
		else if(val.equalsIgnoreCase("NW")){
			return "North West";
		}
		else if(val.equalsIgnoreCase("WNW")){
			return "W. North West";
		}
		else if(val.equalsIgnoreCase("NNW")){
			return "N. North West";
		}
		return null;
	}
}
