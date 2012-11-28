package cs424.windblows.gui;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import omicronAPI.OmicronTouchListener;
import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PFont;
import cs424.windblows.application.Constants;
import cs424.windblows.application.EnumColor;
import cs424.windblows.application.Filter;
import cs424.windblows.application.Utils;
import cs424.windblows.application.Variable;
import cs424.windblows.data.DBFacade;
import cs424.windblows.listeners.FilterListener;
import cs424.windblows.application.ColorCodes;

public class LineGraph extends Sketch  implements FilterListener{

	protected Filter curFilter;
	//protected HashMap<Date,Integer> countValues;
	protected ArrayList<HashMap<Date,Integer>> countValues = new ArrayList<HashMap<Date,Integer>>();
	protected ArrayList<Integer> categories = new ArrayList<Integer>();
		
	protected String xlabel;
	protected int xminValue, xmaxValue, yminValue, ymaxValue; 
	protected int yintervals = 4; //default
	protected float marginX, marginY;	 
	protected int font_color;
	protected int line_color;
	protected PFont font;
	protected float fontSize;
	
	public LineGraph(Variable data) {
		// TODO Auto-generated constructor stub		
		super(data);
		
		marginX = (float) (0.2 * plotWidth);
		marginY = (float) (0.25 * plotHeight);
		xlabel = "Tweets Vs Time";
		font_color = EnumColor.DARK_RED.getValue();
		line_color = EnumColor.WHITE.getValue();
		fontSize = scale(12);
		font = parent.createFont("Helvetica", fontSize, true);
		
		yminValue = 0;
		ymaxValue = 0;
		countValues.clear();
		categories.clear();
		
		getData();
	}
	
	private void getData(){
		
		int cat_id = 0;
		
		if(curFilter == null) {
			curFilter = new Filter();
			curFilter.setCondition(KeywordsSketch.OR);
		}
		
		if(curFilter.getCondition() == KeywordsSketch.OR){
			
			if(categories.size() > 0){
				cat_id = categories.get(categories.size() - 1);
				this.countValues.add(DBFacade.getInstance().getCategoryCounts(curFilter, cat_id));
			}
			else
				cat_id = 0;			
		}
		else{
			this.countValues.add(DBFacade.getInstance().getCategoryCounts(curFilter, 0));
			categories.add(0);
		}
		
		setAxisLimits();		
	}
	
	private void setAxisLimits(){
		
		List<Date> dateKey;
		List<Integer> count;
		
		for(HashMap<Date,Integer> map : countValues){
		
			dateKey = new ArrayList<Date>(map.keySet());		
			Collections.sort(dateKey);
			count = new ArrayList<Integer>();		
			
			for(Date d : dateKey){
				count.add(map.get(d));
			}		
	
			Collections.sort(count);			
			
			yminValue = ((int) Math.floor(count.get(0)) < yminValue) ? (int) Math.floor(count.get(0)) : yminValue; 
			ymaxValue = ((int) Math.ceil(count.get(count.size()-1)) > ymaxValue) ? (int) Math.ceil(count.get(count.size()-1)) : ymaxValue; 
				   
			xminValue = 0;
			xmaxValue = map.keySet().size();
		}
	}
	
	private void plotLine(HashMap<Date,Integer> map, int color){
		
		float graphX, graphY, graphHeight, graphWidth;
		
		graphX = plotX1 + marginX;
		graphY = plotY1 + plotHeight - marginY;
		graphWidth = plotWidth - marginX;
		graphHeight = plotHeight - marginY;
		
		int difference = ymaxValue - yminValue;	    
	    int intervals = (int) Math.floor(difference/yintervals);
	    
	    if(intervals == 0)
	      intervals = 1;	

	    //for(HashMap<Date,Integer> map : countValues){
	    
		    List<Date> dateKey = new ArrayList<Date>(map.keySet());		
			Collections.sort(dateKey);
			List<Integer> count = new ArrayList<Integer>();		
			for(Date d : dateKey){
				count.add(map.get(d));
			}
			
			parent.pushStyle();
			//plotting
			if(curFilter.getCondition() == KeywordsSketch.AND){
				parent.fill(font_color);
				parent.stroke(font_color);
			}
			else{
				parent.noFill();
				parent.stroke(color);
			}
			
			
			parent.strokeWeight(scale(2));
			
			parent.beginShape();		
			parent.vertex(graphX, graphY);
			
			float finalXVal = graphX;
			
		    for (int val = xminValue, index = 0; val < xmaxValue; val++, index++) {
		        Float value = (float)count.get(index);
	
		        Float xVal = (graphWidth * index)/(xmaxValue-xminValue); 
		        Float yVal = (graphHeight * (value - yminValue))/difference;
	       
		        parent.vertex(graphX + xVal, graphY - yVal); 
		        parent.point(graphX + xVal, graphY - yVal);
		        finalXVal = xVal; 
		    }
		       
		    parent.vertex(graphX + finalXVal,graphY);	
		    parent.vertex(graphX,graphY);
		    parent.endShape();	    
		    parent.smooth();
		    
		    parent.popStyle();
	    
	    //}
	}
	
	private void drawYAxis(){
		
		float graphX, graphY, graphHeight, graphWidth;
		
		graphX = plotX1 + marginX;
		graphY = plotY1 + plotHeight - marginY;
		graphWidth = plotWidth - marginX;
		graphHeight = plotHeight - marginY;
		
		int difference = ymaxValue - yminValue;	    
	    int intervals = (int) Math.floor(difference/yintervals);
		
		//y axis markings
	    DecimalFormat formatter = new DecimalFormat("##,##,###");

	    for (int value = yminValue, index = 0; value <= ymaxValue; value++, index++) {
	       
	     if (intervals == 0)
	       intervals = 1; 
	        
	      if(index % intervals == 0){
	          if(value == yminValue){
	        	  parent.textAlign( PConstants.RIGHT, PConstants.BOTTOM);
	          }
	          else if(value >= ymaxValue){
	        	  parent.textAlign( PConstants.RIGHT, PConstants.TOP);
	          }
	          else
	        	  parent.textAlign( PConstants.RIGHT,  PConstants.CENTER);
	          
	          parent.pushStyle();
	          parent.stroke(line_color);
	          parent.fill(line_color);
	          
	          parent.text(formatter.format(value).toString(),(float) (graphX - 0.2*marginX), graphY - (index * graphHeight/difference));    
	          parent.line(graphX,graphY - (index * graphHeight/difference),(float) (graphX - 0.1*marginX),graphY - (index * graphHeight/difference));
	          parent.popStyle();
	        }
	     }
	}
	
	private void drawXAxis(){
		
		float graphX, graphY, graphHeight, graphWidth;
		
		graphX = plotX1 + marginX;
		graphY = plotY1 + plotHeight - marginY;
		graphWidth = plotWidth - marginX;
		graphHeight = plotHeight - marginY;
		
		//x axis markings
	    int xintervals = (xmaxValue == 0) ? 1 : xmaxValue;
	    int index = 0;
	    int i = 1;
	    Date minDate = Utils.getDate(Constants.minDate);
		Date maxDate = Utils.getDate(Constants.maxDate);		
	    
	    int intervals = (xmaxValue-xminValue)/xintervals;
	     
	     if (intervals == 0)
	       intervals = 1;
	     	     
	     for(Date d = minDate; !d.after(maxDate); d = Utils.addDays(d, 1), index++,i++){
	    	 
	    	 if(index % intervals == 0){
	          
	    		 if(d == maxDate){
	    			 parent.textAlign(PConstants.RIGHT, PConstants.TOP);
	    		 }
	    		 else if(d == minDate){
	    			 parent.textAlign(PConstants.LEFT,PConstants.TOP);
	    		 }
	    		 else          
	    			 parent.textAlign(PConstants.CENTER, PConstants.TOP);
	            
	    		 parent.pushStyle();
		         parent.stroke(line_color);
		         parent.fill(line_color);
	    		 
	    		 if(index%4 == 0){
	    			 parent.text(Utils.getFormattedDateMonth(d),graphX + (index * graphWidth/(xmaxValue-xminValue)),(float) (graphY + 0.2*marginY));
	    			 parent.line(graphX + (index * graphWidth/(xmaxValue-xminValue)),graphY,graphX + (index * graphWidth/(xmaxValue-xminValue)),(float) (graphY + 0.18*marginY));    
	    		 }
	    		 
	             parent.line(graphX + (index * graphWidth/(xmaxValue-xminValue)),graphY,graphX + (index * graphWidth/(xmaxValue-xminValue)),(float) (graphY + 0.08*marginY));
	             parent.popStyle();
	        }	        
	     }
	}
	
	private void otherPlumbing(){
		
		float graphX, graphY, graphHeight, graphWidth;
		
		graphX = plotX1 + marginX;
		graphY = plotY1 + plotHeight - marginY;
		graphWidth = plotWidth - marginX;
		graphHeight = plotHeight - marginY;
		
		//x and y axis
		parent.pushStyle();
		parent.fill(line_color);
	    parent.stroke(line_color);
		parent.line(graphX, graphY, graphX, graphY - graphHeight);
		parent.line(graphX, graphY, graphX + graphWidth, graphY);
		parent.text(xlabel,graphX + (float)(0.35*plotWidth), (float) (graphY + 0.7 * marginY));	
		parent.popStyle();
	}
	
	private void plotChart(){			
		
		int index = 0;
		int color = 0;
		int cat_id = 0;
		
		//System.out.println(countValues);
		
		if(categories.size() > 0){
		
			for(HashMap<Date,Integer> map : countValues){	
				
				if(categories.size() > index)
					cat_id = categories.get(index);
				else
					cat_id = 0;

				color = ColorCodes.getColor(cat_id);					
				
				plotLine(map,color);
				index++;
			}
		
		}
		drawYAxis();
		
		drawXAxis();
		
		otherPlumbing();
	}
	
	@Override
	protected void draw() {
		
		parent.pushStyle();
		parent.fill(EnumColor.GRAY_T.getValue());
		//parent.rect(plotX1, plotY1, plotWidth, plotHeight);
		
		plotChart();
		
		parent.popStyle();
	}

	@Override
	public void categoryAdded(int categoryId) {
		curFilter.addCategory(categoryId);
		
		if(curFilter.getCondition() == KeywordsSketch.OR){
			
			categories.add(categoryId);
			getData();
		}
		else{
			yminValue = 0;
			ymaxValue = 0;
			countValues.clear();
			categories.clear();
			getData();
		}
	}

	@Override
	public void categoryRemoved(int categoryId) {
		curFilter.removeCategory(categoryId);
		
		if(curFilter.getCondition() == KeywordsSketch.OR){

			countValues.remove(countValues.get(categories.indexOf((Integer)categoryId)));
			categories.remove((Integer)categoryId);
			
			if(curFilter.getCategories().size() <=0 ){
				yminValue = 0;
				ymaxValue = 0;
				countValues.clear();
				categories.clear();
			}		
			
		}
		else{
			yminValue = 0;
			ymaxValue = 0;
			countValues.clear();
			categories.clear();
			getData();
		}
	}

	@Override
	public void dateChanged(Date date) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void conditionChanged(int condition) {
		curFilter.setCondition(condition);
		
		if(curFilter.getCondition() == KeywordsSketch.AND){
			yminValue = 0;
			ymaxValue = 0;
			countValues.clear();
			categories.clear();
			getData();
		}
		else{
			yminValue = 0;
			ymaxValue = 0;
			countValues.clear();
			categories.clear();
			
			for(Integer cat : curFilter.getCategories()){
				categories.add(cat);
				getData();
			}			
		}		
	}

}
