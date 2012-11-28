/**
 * 
 */
package cs424.windblows.application;


/**
 * @author Gaurav
 *
 */
public class Constants {
//	public static String projDir = Utils.getProjectPath();
	
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
	
	//Marker info on top of map
	public static int infoX = 235;
	public static int infoY = 20;
	public static int infoWidth = 170;
	public static int infoHeight = 60;
	
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
	public static int keywordPanelX = (int) Utils.scale(640);
	public static int keywordPanelY = (int) Utils.scale(85);
	public static int keywordPanelWidth = (int) Utils.scale(320);
	public static int keywordPanelHeight = (int) Utils.scale(335);
	
	//Date Panel outline
	public static int datePanelX = (int) Utils.scale(640);
	public static int datePanelY = (int) Utils.scale(0);
	public static int datePanelWidth = (int) Utils.scale(160);
	public static int datePanelHeight = (int) Utils.scale(85);
	
	//Weather Panel outline
	public static int weatherGraphicX = (int) Utils.scale(800);
	public static int weatherGraphicY = (int) Utils.scale(0);
	public static int weatherGraphiclWidth = (int) Utils.scale(160);
	public static int weatherGraphicHeight = (int) Utils.scale(85);
	
	//Word Cloud Panel
	public static int wcPanelX = (int) Utils.scale(960);
	public static int wcPanelY = (int) Utils.scale(0);
	public static int wcPanelWidth = (int) Utils.scale(400);
	public static int wcPanelHeight = (int) Utils.scale(104);
	
	public static int wcX = (int) Utils.scale(1024);
	public static int wcY = (int) Utils.scale(0);
	public static int wcWidth = (int) Utils.scale(278);
	public static int wcHeight = (int) Utils.scale(104);
	
	//Background to draw over map
	public static int backX1 = (int) Utils.scale(640);
	public static int backY1 = (int) Utils.scale(0);
	public static int backX2 = (int) Utils.scale(1360);
	public static int backY2 = (int) Utils.scale(384);
	//w:780 h:384
	

	//Line Graph Panel
	public static int graphX = (int) Utils.scale(990);
	public static int graphY = (int) Utils.scale(180);
	public static int graphWidth = (int) Utils.scale(350);
	public static int graphHeight = (int) Utils.scale(200);

	/*//Line Graph Panel outline
	public static int lineGraphPanelX = 960;
	public static int lineGraphPanelY = 104;
	public static int lineGraphPanelWidth = 400;
	public static int lineGraphPanelHeight = 140;*/
	
	//Date Panel outline
	public static int tablePanelX = (int) Utils.scale(960);
	public static int tablePanelY = (int) Utils.scale(243);
	public static int tablePanelWidth = (int) Utils.scale(400);
	public static int tablePanelHeight = (int) Utils.scale(140);

}
