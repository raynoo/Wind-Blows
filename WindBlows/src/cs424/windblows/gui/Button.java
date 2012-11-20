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
		
		parent.rect(plotX1, plotY1, plotWidth, plotHeight);
		parent.textSize(textSize);
		parent.textAlign(PApplet.CENTER, PApplet.CENTER);
		parent.fill(EnumColor.BLACK.getValue());
		parent.text(text, plotX1+plotWidth/2f, plotY1+plotHeight/2f);
		
		parent.popStyle();
	}
	
	public boolean containsPoint(float x, float y) {
		return (x > plotX1 && x < plotX2 && y > plotY1 && y < plotY2);
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

}
