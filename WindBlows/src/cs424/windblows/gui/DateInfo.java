package cs424.windblows.gui;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import processing.core.PConstants;
import cs424.windblows.application.Constants;
import cs424.windblows.application.EnumColor;
import cs424.windblows.application.Utils;
import cs424.windblows.application.Variable;
import cs424.windblows.listeners.FilterListener;

public class DateInfo extends Sketch implements FilterListener{

	protected Date currDate;
	
	public DateInfo(Variable data) {
		super(data);
		currDate = Utils.getDate(Constants.minDate);
	}
	
	@Override
	protected void draw() {
		SimpleDateFormat sf = new SimpleDateFormat("EEE, d MMM yyyy", Locale.ENGLISH);
		parent.pushStyle();
		parent.fill(EnumColor.BLACK.getValue());
		parent.textSize(scale(15));
		parent.textAlign(PConstants.CENTER);
		parent.text(sf.format(currDate), plotX1+scale(70), plotY1+scale(50));
		parent.popStyle();
	}
	
	@Override
	public void categoryAdded(int categoryId) {
	}

	@Override
	public void categoryRemoved(int categoryId) {
	}

	@Override
	public void dateChanged(Date date) {
		currDate = date;
	}

	@Override
	public void conditionChanged(int condition) {
	}
}
