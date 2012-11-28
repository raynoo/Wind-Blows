package cs424.windblows.application;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;


public class ColorCodes {
	
	protected static ArrayList<Color> colors = new ArrayList<Color>();
	protected static HashMap<Object, Color> mapping = new HashMap<Object, Color>();
	protected static int defaultColor = EnumColor.DARK_RED.getValue();
	protected static boolean isOR = true;
	
	public static boolean isOR() {
		return isOR;
	}

	public static void setOR(boolean isOR) {
		ColorCodes.isOR = isOR;
	}

	static {
		colors.add(new Color(0x94A8BF));
		colors.add(new Color(0x2E6A1E));
		colors.add(new Color(0xC2A16D));
		colors.add(new Color(0x4B647F));
		colors.add(new Color(0x7E5A23));
		colors.add(new Color(0x6BA45B));
	}
	
	public static void genMappings(Object i){
		//mapping
		Color to = null;
		
		if( colors.size() > 0) to = colors.remove(0);
		else return;
		
		mapping.put(i, to);
		//return to;
	}
	public static boolean isAvailabe(int id){
		if(isOR && !(getMapping(id) == null)) return true;
		else return false;
	}
	public static int getColor(Object i){
		if(!(getMapping(i) == null) && isOR) return getMapping(i).getRGB();
		else return defaultColor;
	}
	
	public static Color getMapping(Object i){
		return mapping.get(i);
	}
	
	public static void removeMapping(Object i){
		Color from = mapping.remove(i);
		colors.add(from);
	}
	
	public static void reset(){
		for(Object key : mapping.keySet()){
			colors.add(mapping.get(key));
		}
		mapping.clear();
	}
	
	public static boolean isAvailabe(){
		if(!isOR || colors.size() > 0) return true;
		else return false;
	}
}