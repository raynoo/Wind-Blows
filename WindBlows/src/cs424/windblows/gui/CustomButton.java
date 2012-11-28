/*
 * CustomButton
 * Last Modified Date: 28-Oct-2012
 * Author: Kevin Samuel Paul
 * kevinsamuelpaul@gmail.com
 */

package cs424.windblows.gui;

import cs424.windblows.application.EnumColor;
import processing.core.PApplet;
import processing.core.PFont;

public class CustomButton {
	  float posX;
	  float posY;
	  float width;
	  float height;
	  String text;
	  int backgroundColor;
	  int textColor;
	  boolean active;
	  PFont pfont;
	  int strokeWeight;
	  int alignText;	//0-CENTER, 1-LEFT, 2-RIGHT
	  int strokeColor;
	  float rounded;
	  PApplet parent;
	  String value;
	 
	  //Only to use with keyboard/listbox
	  CustomButton(PApplet parent) {
		  this.parent = parent;
		  setDefaultAttributes();
	  }
	  
	 //Set all attributes manually or use the other constructor
	 public CustomButton(PApplet parent, String textDisplayed, float posX, float posY, float buttonWidth, float buttonHeight) {
	   this.parent = parent;
	   this.posX = posX;
	   this.posY = posY;
	   this.text = textDisplayed;
	   this.width = buttonWidth;
	   this.height = buttonHeight;
	   active = false;
	   this.alignText=0;
	   this.rounded=0;
	 }
	 
	 CustomButton(PApplet parent, String textDisplayed, float posX, float posY, 
			 float buttonWidth, float buttonHeight, boolean useDefault) { 
		this.parent = parent;
		this.posX = posX;
		this.posY = posY;
		this.text = textDisplayed;
		this.width = buttonWidth;
		this.height = buttonHeight;
		if(useDefault)
			setDefaultAttributes();
	 }
	 
	 CustomButton(PApplet parent, String textDisplayed, String value, float posX, float posY, 
			 float buttonWidth, float buttonHeight, boolean useDefault) { 
		this.parent = parent;
		this.posX = posX;
		this.posY = posY;
		this.text = textDisplayed;
		this.value = value;
		this.width = buttonWidth;
		this.height = buttonHeight;
		if(useDefault)
			setDefaultAttributes();
	 }
	 
	 void setDefaultAttributes() {
		 active = false;
		 this.backgroundColor = parent.color(EnumColor.OFFWHITE.getValue());	   
		 this.textColor = parent.color(EnumColor.BLACK.getValue());
		 this.strokeColor = this.textColor;
		 this.pfont = parent.createFont("SansSerif", 15);
		 this.strokeWeight=1;
		 this.alignText=0;
		 this.rounded = 0;
		 }
	 
	 public void display() {
		parent.pushStyle();
		parent.fill(backgroundColor);
		parent.rectMode(PApplet.CORNER);
		parent.stroke(strokeColor);
		parent.strokeWeight(strokeWeight);
		parent.rect(posX, posY, width, height, rounded);
		parent.fill(textColor);
		if(alignText==0)
			parent.textAlign(PApplet.CENTER, PApplet.CENTER);
		else if(alignText==1)
			parent.textAlign(PApplet.LEFT, PApplet.CENTER);
		else if(alignText==2)
			parent.textAlign(PApplet.RIGHT, PApplet.CENTER);
		else if(alignText==3)
			parent.textAlign(PApplet.CENTER, PApplet.TOP);
		
		//parent.textFont(pfont);
		//parent.textLeading(10);
		parent.text(" "+text, posX, posY, width, height);
		parent.popStyle();
		}
	 
	 public void updateButton(float mX, float mY) {
		 if(mX>posX && mX<(posX+width) && mY>posY && mY<(posY+height)) {
			 active = active ? false : true;
//			 System.out.println("Updating button " + getText() + " to " + active);
			 }
		 }

	 public boolean isClicked() {
	   return active;
	 }
	 
	 boolean isFocus(float mX, float mY) {
	   if(mX>posX && mX<(posX+width) && mY>posY && mY<(posY+height)) {
	      return true;
	    }
	    return false;
	 }

	 float getPositionX() {
	   return posX;
	 }
	 
	 float getPositionY() {
	   return posY;
	 }
	 
	 float getWidth() {
	   return width;
	 }
	 
	 float getHeight() {
	   return height;
	 }

	 void setWidth(float buttonWidth) {
	   this.width = buttonWidth;
	 }
	 
	 void setWidth(double buttonWidth) {
	   this.width = (float) buttonWidth;
	 }
		 
	 void setHeight(float buttonHeight) {
	   this.height = buttonHeight;
	 }
	 
	 void setHeight(double buttonHeight) {
	   this.height = (float) buttonHeight;
	 }
	 
	 void setPosX(float posX) {
	   this.posX = posX;
	 }

	void setPosY(float posY) {
	   this.posY = posY;
	 }
	
	void setPosX(double posX) {
	   this.posX = (float) posX;
	 }

	void setPosY(double posY) {
	   this.posY = (float) posY;
	 }
	 
	 void setPosition(float x, float y) {
	   this.posX = x;
	   this.posY = y;
	 }
	 
	 void setStroke(int k) {
	   this.strokeWeight=k;
	 }
	 
	 void setRounded(int k) {
	   this.rounded = k;
	 }
	 
	 void setAlignText(int k) {
	   this.alignText=k;
	 }

	 void setBackground(int col) {
	   this.backgroundColor = col;
	 }
	 
	 void setTextColor(int col) {
	   this.textColor = col;
	 }
	 
	 void setBorderColor(int col) {
	   this.strokeColor = col;
	 }
	 
	 void setFont(PFont pfont) {
	   this.pfont = pfont;
	 }
	 
	void setText(String t) {
	  this.text = t;
	  }
	
	 void setButton(int s) {
		 active = s==0 ? false : true;
	 }
	 
	 void reset() {
		 active = false;
	 }
 
	 public String getText() {
	   return text;
	 }
	 
	 String getValue() {
		 return (String) value;
	 }
	 
	 void setValue(String value) {
		 this.value = value;
	 }
}