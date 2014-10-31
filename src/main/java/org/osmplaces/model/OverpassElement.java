package org.osmplaces.model;

import java.util.Map;

public class OverpassElement {
	
	
	
	private long id;
	private Double lon;
	private Double lat;
	private Map<String, String> tags;

	public OverpassElement() {
		super();
	}
	
	
	public boolean hasTag(String tagName){
		return this.tags.containsKey(tagName);
	}
	
	public String getTag(String tagName){
		return this.tags.get(tagName);
	}


	public long getId() {
		return id;
	}




	public void setId(long id) {
		this.id = id;
	}




	public double getLon() {
		return lon;
	}




	public void setLon(double lon) {
		this.lon = lon;
	}




	public double getLat() {
		return lat;
	}




	public void setLat(double lat) {
		this.lat = lat;
	}




	public Map<String, String> getTags() {
		return tags;
	}




	public void setTags(Map<String, String> tags) {
		this.tags = tags;
	}




	@Override
	public String toString() {
		return this.id + "(" + this.tags + ") at " + this.lat + "," + this.lon;
	}

}
