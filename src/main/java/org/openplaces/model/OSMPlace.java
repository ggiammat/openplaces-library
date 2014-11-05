package org.openplaces.model;

public class OSMPlace {
	
	private long id;
	private String name;
	private double lat;
	private double lon;
	private String fullAddress;
	private String osmType;
	private String osmAmenity;
	private Double averageRating;
	private Integer numReviews;
	
	private Object relatedObject;
	
	
	
	
	public Object getRelatedObject() {
		return relatedObject;
	}
	public void setRelatedObject(Object relatedObject) {
		this.relatedObject = relatedObject;
	}
	public Double getAverageRating() {
		return averageRating;
	}
	public void setAverageRating(Double averageRating) {
		this.averageRating = averageRating;
	}
	public Integer getNumReviews() {
		return numReviews;
	}
	public void setNumReviews(Integer numReviews) {
		this.numReviews = numReviews;
	}
	public String getOsmAmenity() {
		return osmAmenity;
	}
	public void setOsmAmenity(String osmAmenity) {
		this.osmAmenity = osmAmenity;
	}
	public String getOsmType() {
		return osmType;
	}
	public void setOsmType(String osmType) {
		this.osmType = osmType;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
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
	public String getFullAddress() {
		return fullAddress;
	}
	public void setFullAddress(String fullAddress) {
		this.fullAddress = fullAddress;
	}
	
	@Override
	public String toString() {
		return this.name + "(" + this.id + ";" + this.lat + "," + this.lon + ") " + this.averageRating + " on " + this.numReviews;
	}
}
