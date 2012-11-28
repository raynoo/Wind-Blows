package cs424.windblows.listeners;

import cs424.windblows.gui.Marker;

public interface MarkerListener {
	public void markerSelected(Marker m);
	public void markerUserSelected(Marker m);
	public void markerUserUnselected(Marker m);
}
