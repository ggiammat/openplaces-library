package org.osmplaces.model;

public class Place {
	
	
	private double lat;
	private double lon;
	private long osmId;
	private String displayName;
	
	public Place() {
		super();
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



	public long getOsmId() {
		return osmId;
	}



	public void setOsmId(long osmId) {
		this.osmId = osmId;
	}



	public String getDisplayName() {
		return displayName;
	}



	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}



	@Override
	public String toString() {
		return this.displayName + "(" + this.osmId + ") at " + this.lat + "," + this.lon;
	}
	
	

}
