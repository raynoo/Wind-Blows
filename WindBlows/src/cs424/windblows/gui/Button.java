package cs424.windblows.gui;

import processing.core.PApplet;
import cs424.windblows.application.EnumColor;
import cs424.windblows.application.Variable;

public class Button extends Sketch {

	private boolean isPressed = false;
	private String text;
	private int textSize = 10;
	
	public Button(Variable data) {
		super(data);
	}
	
	public Button(Variable data, String text) {
		super(data);
		this.setText(text);
	}

	@Override
	protected void draw() {
		parent.pushStyle();
		
		if(this.isPressed())
			parent.fill(EnumColor.DARK_GRAY.getValue());
		else
			parent.fill(EnumColor.OFFWHITE.getValue());
		
		parent.rect(scale(plotX1), scale(plotY1), scale(plotWidth), scale(plotHeight));
		parent.textSize(scale(textSize));
		parent.textAlign(PApplet.CENTER, PApplet.CENTER);
		parent.fill(EnumColor.BLACK.getValue());
		parent.text(text, scale(plotX1+plotWidth/2f), scale(plotY1+plotHeight/2f));
		
		parent.popStyle();
	}
	
	public void setPressed(boolean isPressed) {
		this.isPressed = isPressed;
	}

	public boolean isPressed() {
		return isPressed;
	}
	
	public void setText(String text) {
		this.text = text;
	}
	
	public void setTextSize(int size) {
		this.textSize = size;
	}

	public boolean containsPoint(float xPos, float yPos) {
		if(xPos > plotX1 && xPos < plotX2 && yPos > plotY1 && yPos < plotY2)
			return true;
		return false;
	}

}
