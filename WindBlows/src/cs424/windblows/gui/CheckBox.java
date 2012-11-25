package cs424.windblows.gui;

import omicronAPI.OmicronTouchListener;
import processing.core.PApplet;
import processing.core.PShape;
import cs424.windblows.application.EnumColor;
import cs424.windblows.application.Utils;
import cs424.windblows.application.Variable;

class CheckBox extends Sketch implements OmicronTouchListener{
	
	protected float boxWidth = Utils.scale(15), boxHeight = Utils.scale(15);
	protected boolean isSelected;
	protected String label;
	protected int id;
	
	
	protected PShape check;
	protected float imageSize = Utils.scale(15);
	
	public int getId() {
		return id;
	}
	

	public void setId(int id) {
		this.id = id;
	}

	public CheckBox(Variable data) {
		super(data);
		isSelected = false;
		check = parent.loadShape("images/black_check.svg");
	}
	
	@Override
	public void init(Variable data) {
		super.init(data);
		this.label = data.getLabel();
	}
	public boolean isSelected() {
		return isSelected;
	}

	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}

	public void draw() {
		// try to use push and pop
		parent.pushStyle();
		
		parent.fill(EnumColor.WHITE.getValue());
		parent.rectMode(PApplet.CORNER);
		parent.rectMode(PApplet.CORNER);
		parent.rect(plotX1, plotY1, boxWidth, boxHeight, Utils.scale(3));
		
		
		if(isSelected){
			parent.shape(check, plotX1, plotY1, imageSize, imageSize);
		}		
		
		
		parent.fill(EnumColor.BLACK.getValue());
		parent.textAlign(PApplet.LEFT);

		float y = (plotY1 + parent.textAscent() + parent.textDescent());
		parent.text(label, plotX1 + boxWidth + Utils.scale(7), y );
		parent.popStyle();
	}
	
	
	@Override
	public void touchDown(int ID, float xPos, float yPos, float xWidth, float yWidth){
		if(!isActive) return;
		// check if the box is touched
		if(!(Utils.isPresent(plotX1, plotX1 + boxWidth, xPos)
		   && Utils.isPresent(plotY1, plotY1 + boxHeight, yPos))) return;
		else{
			if(isSelected){
				isSelected = false;
			}
			else {
				isSelected = true;
			}
		}
	}

	@Override
	public void touchMove(int ID, float xPos, float yPos, float xWidth, float yWidth){
	}

	@Override
	public void touchUp(int ID, float xPos, float yPos, float xWidth, float yWidth){
	}
}
