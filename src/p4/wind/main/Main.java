package p4.wind.main;

import omicronAPI.OmicronAPI;
import static p4.wind.application.AppConstants.*;
import processing.core.PApplet;

public class Main extends PApplet {

	public static void main(String args[]) {
	    PApplet.main(new String[] { p4.wind.main.Main.class.getName() });
    }
	
	OmicronAPI omicronManager;
	
	public void setup() {
		this.size(fullScreenWidth, fullScreenHeight);
		this.background(255, 255, 255);
	}
	
	public void draw() {
		
	}
}
