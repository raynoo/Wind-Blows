package cs424.windblows.gui;

import omicronAPI.OmicronTouchListener;
import cs424.windblows.application.EnumColor;
import cs424.windblows.application.Utils;
import processing.core.PApplet;

public class Marker implements OmicronTouchListener {
	
	Location location;
	
	float centerX, centerY;
	float radius;
	int color;
	
	PApplet p;
	
	//coordinates are already scaled while creating markers.
	//ie pass in scaled coordinates.
	public Marker(float centerX, float centerY, float radius, PApplet papplet) {
		this.centerX = centerX;
		this.centerY = centerY;
		this.radius = radius;
		this.p = papplet;
	}
	
	public void setColor(int color) {
		this.color = color;
	}
	
	public void draw() {
		p.pushStyle();
		p.stroke(EnumColor.DARK_RED.getValue());
		p.strokeWeight(Utils.scale(1f));
		p.fill(color);
		p.ellipse(centerX, centerY, 2*radius, 2*radius);
		p.popStyle();
	}
	
	public boolean containsPoint(float x, float y) {
		if(Math.pow(centerX - x, 2) + Math.pow(centerY - y, 2) <= Math.pow(radius, 2))
			return true;
		
		return false;
	}

	@Override
	public void touchDown(int arg0, float arg1, float arg2, float arg3,
			float arg4) {
		//display info
		System.out.println("Marker.touchDown()");
	}

	@Override
	public void touchMove(int arg0, float arg1, float arg2, float arg3,
			float arg4) {
		// TODO Auto-generated method stub
	}

	@Override
	public void touchUp(int arg0, float arg1, float arg2, float arg3, float arg4) {
		// TODO Auto-generated method stub
	}
	
}
