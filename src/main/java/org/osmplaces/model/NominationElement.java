package org.osmplaces.model;

import java.util.Map;

public class NominationElement {
	
	
	
	private Double lat;
	private Double lon;
	private long osm_id;
	private String osm_type;
	
	private String display_name;
	private Double importance;
	private Map<String, String> address;
	
	public NominationElement(){
		
	}
	
	
	public Double getLat() {
		return lat;
	}
	public void setLat(Double lat) {
		this.lat = lat;
	}
	public Double getLon() {
		return lon;
	}
	public void setLon(Double lon) {
		this.lon = lon;
	}
	public long getOsm_id() {
		return osm_id;
	}
	public void setOsm_id(long osm_id) {
		this.osm_id = osm_id;
	}
	public String getOsm_type() {
		return osm_type;
	}
	public void setOsm_type(String osm_type) {
		this.osm_type = osm_type;
	}
	public String getDisplay_name() {
		return display_name;
	}
	public void setDisplay_name(String display_name) {
		this.display_name = display_name;
	}
	public Double getImportance() {
		return importance;
	}
	public void setImportance(Double importance) {
		this.importance = importance;
	}
	public Map<String, String> getAddress() {
		return address;
	}
	public void setAddress(Map<String, String> address) {
		this.address = address;
	}
	
	
	@Override
	public String toString() {
		return this.osm_type + "(" + this.osm_id+") " + this.address; 
	}

}
