/**
 * 
 */
package cs424.windblows.application;

/**
 * @author Gaurav
 *
 */
public class Constants {
	public static float SCALE = 1;
	
	public static String minDate = "4/30/2011";
	public static String maxDate = "5/20/2011";
	public static int numberOfDays = 30;
	
	//Given Lat-Long of map image
	public static float topLeftLat = 42.3017f;
	public static float topLeftLon = 93.5673f;
	public static float bottomRightLat = 42.1609f;
	public static float bottomRightLon = 93.1923f;
	
	//Map panel
	public static int mapPanelX = 0;
	public static int mapPanelY = 0;
	public static int mapPanelWidth = 680;
	public static int mapPanelHeight = 384;
	
	//Map image
	public static int mapWidth = 640;
	public static int mapHeight = 320;
	
	//Map buttons
	public static int zoomInButtonX = 602;
	public static int zoomInButtonY = 267;
	public static int panLeftButtonX = 584;
	public static int panLeftButtonY = 275;
	public static int panRightButtonX = 620;
	public static int panRightButtonY = 275;
	
	public static int zoomButtonWidth = 15;
	public static int zoomButtonHeight = 15;
	
	//Play button 1
	public static int playButtonX = 266;
	public static int playButtonY = 365;
	public static int playButtonWidth = (int) Utils.scale(15);
	public static int playButtonHeight = (int) Utils.scale(15);
	
	//Keyword Panel outline
	public static int keywordPanelX = 640;
	public static int keywordPanelY = 85;
	public static int keywordPanelWidth = 320;
	public static int keywordPanelHeight = 235;
	
	//Date Panel outline
	public static int datePanelX = 640;
	public static int datePanelY = 0;
	public static int datePanelWidth = 160;
	public static int datePanelHeight = 85;
	
	//Weather Panel outline
	public static int weatherGraphicX = 800;
	public static int weatherGraphicY = 0;
	public static int weatherGraphiclWidth = 160;
	public static int weatherGraphicHeight = 85;
	
	//Word Cloud Panel
	public static int wcPanelX = 960;
	public static int wcPanelY = 0;
	public static int wcPanelWidth = 400;
	public static int wcPanelHeight = 104;
	
	public static int wcX = 1024;
	public static int wcY = 0;
	public static int wcWidth = 278;
	public static int wcHeight = 104;
	
	//Background to draw over map
	public static int backX1 = 640;
	public static int backY1 = 0;
	public static int backX2 = 1360;
	public static int backY2 = 384;
	//w:780 h:384
	
	//Line Graph Panel outline
	public static int lineGraphPanelX = 960;
	public static int lineGraphPanelY = 104;
	public static int lineGraphPanelWidth = 400;
	public static int lineGraphPanelHeight = 140;
	
	//Date Panel outline
	public static int tablePanelX = 960;
	public static int tablePanelY = 243;
	public static int tablePanelWidth = 400;
	public static int tablePanelHeight = 140;
}
