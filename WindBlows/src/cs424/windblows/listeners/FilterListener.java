package cs424.windblows.listeners;

import java.util.Date;

public interface FilterListener {
	public void categoryAdded(int categoryId);
	public void categoryRemoved(int categoryId);
	public void dateChanged(Date date);
}
