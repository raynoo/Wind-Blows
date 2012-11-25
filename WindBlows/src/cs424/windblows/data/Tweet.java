package cs424.windblows.data;

import java.util.Date;

public class Tweet {
	protected int tweetID;
	protected Date dateTime;
	protected String tweet;
	protected double lat, lon;
	
	public int getTweetID() {
		return tweetID;
	}
	public void setTweetID(int tweetID) {
		this.tweetID = tweetID;
	}
	public Date getDateTime() {
		return dateTime;
	}
	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}
	public String getTweet() {
		return tweet;
	}
	public void setTweet(String tweet) {
		this.tweet = tweet;
	}
	public double getLat() {
		return lat;
	}
	public void setLat(double lat) {
		this.lat = lat;
	}
	public double getLon() {
		return lon;
	}
	public void setLon(double lon) {
		this.lon = lon;
	}
}
