package cs424.windblows.gui;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import omicronAPI.OmicronTouchListener;
import processing.core.PApplet;
import cs424.windblows.application.Constants;
import cs424.windblows.application.EnumColor;
import cs424.windblows.application.Filter;
import cs424.windblows.application.Utils;
import cs424.windblows.application.Variable;
import cs424.windblows.data.DBFacade;
import cs424.windblows.listeners.FilterListener;

public class LineGraph extends Sketch  implements FilterListener{

	protected Filter curFilter;
	protected HashMap<String,Integer> countValues;
	
	protected String xlabel;
	protected int xminValue, xmaxValue, yminValue, ymaxValue; 
	protected int yintervals = 4; //default
	protected float marginX, marginY;	  
	
	public LineGraph(Variable data) {
		// TODO Auto-generated constructor stub		
		super(data);
		
		marginX = (float) (0.2 * plotWidth);
		marginY = (float) (0.25 * plotHeight);
		xlabel = "Tweets Vs Time";
		
		getData();
	}
	
	private void getData(){
		
		this.countValues = DBFacade.getInstance().getCategoryCounts(curFilter);
		
		List<Integer> count = new ArrayList<Integer>(countValues.values());		
		
		Collections.sort(count);			
		
		yminValue = (int) Math.floor(count.get(0)); 
		ymaxValue = (int) Math.ceil(count.get(count.size()-1));  
		
		xminValue = 1;
		xmaxValue = countValues.keySet().size();   
	}
	
	private void plotChart(){			
		
		float graphX, graphY, graphHeight, graphWidth;
		
		graphX = plotX1 + marginX;
		graphY = plotY1 + plotHeight - marginY;
		graphWidth = plotWidth - marginX;
		graphHeight = plotHeight - marginY;
		
		//plotting
		parent.noFill();
		parent.beginShape();	    
		parent.vertex(graphX, graphY);
		
		int difference = ymaxValue - yminValue;	    
	    int intervals = (int) Math.floor(difference/yintervals);
	    
	    if(intervals == 0)
	      intervals = 1;	      
	    //float finalXVal = graphX;
	    
	    List<Integer> count = new ArrayList<Integer>(countValues.values());		
	    
	    for (int val = xminValue, index = 0; val < xmaxValue; val++, index++) {
	        Float value = (float)count.get(index);

	        Float xVal = (graphWidth * index)/(xmaxValue-xminValue); 
	        Float yVal = (graphHeight * (value - yminValue))/difference;
       
	        parent.vertex(graphX + xVal, graphY - yVal); 
	        parent.point(graphX + xVal, graphY - yVal);
	        //finalXVal = xVal; 
	    }
	       
	    //parent.vertex(graphX + finalXVal,graphY + plotHeight);	    
	    parent.endShape();
	    parent.smooth();
	    parent.fill(0);	    
	    
	    //y axis markings
	    DecimalFormat formatter = new DecimalFormat("##,##,###");

	    for (int value = yminValue, index = 0; value <= ymaxValue; value++, index++) {
	       
	     if (intervals == 0)
	       intervals = 1; 
	        
	      if(index % intervals == 0){
	          if(value == yminValue){
	        	  parent.textAlign( parent.RIGHT, parent.BOTTOM);
	          }
	          else if(value == xminValue){
	        	  parent.textAlign( parent.RIGHT, parent.TOP);
	          }
	          else
	        	  parent.textAlign( parent.RIGHT,  parent.CENTER);
	           
	          parent.text(formatter.format(value).toString(),(float) (graphX - 0.2*marginX), graphY - (index * graphHeight/difference));    
	          parent.line(graphX,graphY - (index * graphHeight/difference),(float) (graphX - 0.1*marginX),graphY - (index * graphHeight/difference));
	        }
	     }

	     /*parent.pushMatrix();
	     parent.translate((float) (graphX - 0.8* marginX), graphY - graphHeight);
	     parent.rotate(parent.radians(-90));
	     parent.text ("test",0,0);
	     parent.popMatrix();*/
	    
	    //x axis markings
	    int xintervals = countValues.keySet().size();
	    int index = 0;
	    int i = 1;
	    Date minDate = Utils.getDate(Constants.minDate);
		Date maxDate = Utils.getDate(Constants.maxDate);		
	    
	    intervals = (xmaxValue-xminValue)/xintervals;
	     
	     if (intervals == 0)
	       intervals = 1;
	     
	     //for (Integer value = xminValue, index = 0; value < xmaxValue; value++, index++) {
	     for(Date d = minDate; !d.after(maxDate); d = Utils.addDays(d, 1), index++,i++){
	    	 
	    	 if(index % intervals == 0){
	          
	    		 if(d == maxDate){
	    			 parent.textAlign(parent.RIGHT, parent.TOP);
	    		 }
	    		 else if(d == minDate){
	    			 parent.textAlign(parent.LEFT,parent.TOP);
	    		 }
	    		 else          
	    			 parent.textAlign(parent.CENTER, parent.TOP);
	            
	    		 if(index%4 == 0){
	    			 parent.text(Utils.getFormattedDateMonth(d),graphX + (index * graphWidth/(xmaxValue-xminValue)),(float) (graphY + 0.2*marginY));
	    			 parent.line(graphX + (index * graphWidth/(xmaxValue-xminValue)),graphY,graphX + (index * graphWidth/(xmaxValue-xminValue)),(float) (graphY + 0.18*marginY));    
	    		 }
	    		 
	             parent.line(graphX + (index * graphWidth/(xmaxValue-xminValue)),graphY,graphX + (index * graphWidth/(xmaxValue-xminValue)),(float) (graphY + 0.08*marginY));    
	        }	        
	     }
		
		//x and y axis
		parent.line(graphX, graphY, graphX, graphY - graphHeight);
		parent.line(graphX, graphY, graphX + graphWidth, graphY);
		parent.text(xlabel,graphX + (float)(0.35*plotWidth), (float) (graphY + 0.7 * marginY));
	
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
		// TODO Auto-generated method stub
		getData();
	}

	@Override
	public void categoryRemoved(int categoryId) {
		// TODO Auto-generated method stub
		getData();
	}

	@Override
	public void dateChanged(Date date) {
		// TODO Auto-generated method stub
		
	}

}
