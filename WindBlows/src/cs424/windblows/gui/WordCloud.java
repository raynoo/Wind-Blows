package cs424.windblows.gui;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PFont;
import static cs424.windblows.application.Constants.wcX;
import static cs424.windblows.application.Constants.wcY;
import static cs424.windblows.application.Constants.wcWidth;
import static cs424.windblows.application.Constants.wcHeight;
import cs424.windblows.application.EnumColor;
import cs424.windblows.application.Filter;
import cs424.windblows.application.Utils;
import cs424.windblows.application.Variable;
import cs424.windblows.data.DBFacade;
import cs424.windblows.listeners.FilterListener;

public class WordCloud extends Sketch implements FilterListener {

	private int minCount;
	private int maxCount;
    private Date currentDate;
    private Filter curFilter;
    
    private boolean filterChanged = true;

	private PFont font;
	private int fontColor;

	private float minFontSize;
	private float maxFontSize;

	List<DataPoint> data;
	HashMap<String, Integer> filterCounts;
	
	public WordCloud(Variable data) {
		super(data);

		this.minFontSize = scale(10);
		this.maxFontSize = scale(40);
		this.font = parent.createFont("Helvetica", maxFontSize, true);
		this.fontColor = EnumColor.DARK_RED.getValue();
	}

	void getData() {
		
		if(curFilter == null) {
			curFilter = new Filter();
			currentDate = Utils.getDate("4/30/2011");
			curFilter.setDate(currentDate);
		}
		
		data = new ArrayList<DataPoint>();
		
		minCount = Integer.MAX_VALUE;
		maxCount = Integer.MIN_VALUE;
		
		filterCounts = DBFacade.getInstance().getKeywordCount(curFilter);
		
		for(Entry<String, Integer> counts : filterCounts.entrySet()) {
			data.add(new DataPoint(counts.getKey()+"("+counts.getValue()+")", counts.getValue()));
			
			if(filterCounts.size() == 1) {
				minCount = counts.getValue();
				maxCount = counts.getValue();
			} else {
				if (counts.getValue() < minCount)
					minCount = counts.getValue();
				if (counts.getValue() > maxCount)
					maxCount = counts.getValue();
			}
		}
		
	}

	@Override
	protected void draw() {

		if(filterChanged) {
			getData();
			filterChanged = false;
		}
		
		
		parent.pushStyle();
		parent.fill(EnumColor.GRAY_T.getValue());
		parent.rect(wcX, wcY, wcWidth, wcHeight);
		
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
		parent.textFont(font, d.getTextSize());
		int w = (int) (parent.textWidth(d.getValue()));

		float textX = (parent.random((float) (wcWidth - w)) + wcX);
		float textY = (parent.random((float) (wcHeight - d.getTextSize())) + wcY);
		boolean fits = true;

		//check if the entire word fits in the panel
		if(d.getTextX()+w > wcX+wcWidth || d.getTextY()+d.getTextSize() > wcY+wcHeight)
			fits = false;
		
		//if its not overlapping
		else {
			for (int i = 0; i < w && fits; i++)
				for (int j = 0; j < textX && fits; j++)
				    if (parent.get((int) (textX + i), (int) (textY + j)) == fontColor)
				    	fits = false;
		}

		if (fits) {
			d.setTextX(textX);
			d.setTextY(textY);
			
			return true;
		}
		return false;
	}

	@Override
	public void categoryAdded(int categoryId) {
		curFilter.addCategory(categoryId);
		filterChanged = true;
	}

	@Override
	public void categoryRemoved(int categoryId) {
		curFilter.removeCategory(categoryId);
		filterChanged = true;
	}

	@Override
	public void dateChanged(Date date) {
		this.currentDate = date;
		filterChanged = true;
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