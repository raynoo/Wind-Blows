package cs424.windblows.gui;

import java.util.ArrayList;
import java.util.List;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PFont;
import cs424.windblows.application.EnumColor;
import cs424.windblows.application.Variable;

public class WordCloud extends Sketch {

	private int minCount;
	private int maxCount;
//    private int activeYear;

	private PFont font;
	private int fontColor;

	private float minFontSize;
	private float maxFontSize;

	List<DataPoint> data;
	
	public WordCloud(Variable data) {
		super(data);

		this.minFontSize = scale(15);
		this.maxFontSize = scale(50);
		this.font = parent.createFont("Helvetica", maxFontSize, true);
		this.fontColor = EnumColor.DARK_RED.getValue();

		getData();
	}

	void getData() {
		data = new ArrayList<DataPoint>();

		data.add(new DataPoint("Fever", 890));
		data.add(new DataPoint("Chills", 550));
		data.add(new DataPoint("Cold", 390));
		data.add(new DataPoint("Feeling", 30));
		data.add(new DataPoint("Smoke", 66));
		data.add(new DataPoint("Disaster", 100));
		data.add(new DataPoint("Nausea", 412));
		data.add(new DataPoint("Pain", 178));
		data.add(new DataPoint("Stomach", 225));
		data.add(new DataPoint("Weather", 165));


		minCount = Integer.MAX_VALUE;
		maxCount = Integer.MIN_VALUE;

		for (DataPoint d:data) {
			if (d.getCount() < minCount)
				minCount = d.getCount();
			if (d.getCount() > maxCount)
				maxCount = d.getCount();
		}
	}

	@Override
	protected void draw() {

		parent.pushStyle();
		parent.fill(EnumColor.GRAY_T.getValue());
		parent.rect(plotX1, plotY1, plotWidth, plotHeight);
		
		for(DataPoint d:data) {
			float relativeSize = PApplet.map(d.getCount(), minCount,
					maxCount, minFontSize, maxFontSize);
			d.setTextSize((int)relativeSize);
			
			int tries = 0;
			while (! d.isDrawn() && tries < 50) {
				if(! d.isDrawn() )
					d.setIsDrawn(setWordPosition(d));
			
				if (! d.isDrawn() ) {
					tries++;
					relativeSize = (float) (relativeSize * 0.95);
				}
			}
		}
		
		parent.textAlign(PConstants.LEFT, PConstants.TOP);
		parent.fill(fontColor);
		
		for(DataPoint d:data) {
			parent.textFont(font, d.getTextSize());
			parent.text(d.getValue(), d.getTextX(), d.getTextY());
			
		}
		
		parent.popStyle();
	}

	boolean setWordPosition(DataPoint d) {
		
		int w = (int) (parent.textWidth(d.getValue()));
		
//		for (int tries = 50; tries > 0; tries--) {
			d.setTextX(parent.random((float) (this.plotWidth - w)) + this.plotX1);
			d.setTextY(parent.random((float) (this.plotHeight - d.getTextSize())) + this.plotY1);
			boolean fits = true;
			
			//check if the entire word fits in the panel
			if(d.getTextX()+w > plotX1+plotWidth || 
					d.getTextY()+d.getTextSize() > plotY1+plotHeight)
				fits = false;
			
			if (fits)
				return true;
//		}
		return false;
	}
}

class DataPoint {
	String value;
	int count, textSize;
	float textX, textY;
	boolean isDrawn = false;

	public DataPoint(String value, int count) {
		this.value = value;
		this.count = count;
	}
	
	public int getTextSize() {
		return textSize;
	}

	public void setTextSize(int textSize) {
		this.textSize = textSize;
	}
	
	public String getValue() {
		return this.value;
	}

	public int getCount() {
		return this.count;
	}

	public float getTextX() {
		return textX;
	}

	public void setTextX(float textX) {
		this.textX = textX;
	}

	public float getTextY() {
		return textY;
	}

	public void setTextY(float textY) {
		this.textY = textY;
	}
	
	public boolean isDrawn() {
		return isDrawn;
	}

	public void setIsDrawn(boolean drawn) {
		this.isDrawn = drawn;
	}
}