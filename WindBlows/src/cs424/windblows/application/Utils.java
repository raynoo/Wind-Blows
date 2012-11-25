package cs424.windblows.application;


/** 
 * Created with an intention to define all the utiliy methods
 * @author Gaurav
 *
 */
public class Utils {
	
	
	/**
	 * Scales float value to appropriate level based on screen size.
	 * I hope jvm inlines this function.
	 * @param val
	 * @return scaled value
	 */
	public static final float scale(float val){
		return val * Constants.SCALE;
	}
	
	/**
	 * Returns true if the value if present between min and max values
	 * @param minVal
	 * @param maxVal
	 * @param val
	 * @return
	 */
	public static boolean isPresent(float minVal, float maxVal, float val){
		if(val < maxVal && val > minVal) return true;
		else return false;
	}
}