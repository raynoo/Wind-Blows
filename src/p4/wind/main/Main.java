package p4.wind.main;

import omicronAPI.OmicronAPI;
import p4.wind.application.AppConstants;
import processing.core.PApplet;

public class Main extends PApplet {

	static public void main(String args[]) {
	    PApplet.main(new String[] { "main.Program" });
    }
	
	OmicronAPI omicronManager;
	
	public void setup() {
		this.size(AppConstants.fullScreenWidth, AppConstants.fullScreenHeight);
		this.background(0, 0, 0);
	}
	
	public void draw() {
		
	}
}
