package cs424.windblows.application;

import cs424.windblows.gui.Sketch;

public enum EnumColor {

	SOMERANDOM(133,133,1),
	DARK_GRAY(105,105,105),
	GRAY_T(255,255,255,100),
	GRAY(190,190,190),
	WHITE(255,255,255),
	OFFWHITE(216,216,216),
	BLACK(0,0,0),
	RED(170,42,40),
	DARK_RED(85,21,20),
	PINK(255,153,204),
	RED_T(170,42,40,200);
	
	int color;

	private EnumColor(int r, int g, int b) {
		color = Sketch.getParent().color(r, g, b);
	}
	
	private EnumColor(int r, int g, int b, int a) {
		color = Sketch.getParent().color(r, g, b, a);
	}
	
	public int getValue() {
		return color;
	}
}
