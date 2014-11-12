package org.openplaces.internal.model;

public class ReviewServerElement {

	private String osmid;
	private Double averageRating;
	private Integer numReviews;
	public String getOsmid() {
		return osmid;
	}
	public void setOsmid(String osmid) {
		this.osmid = osmid;
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
	
	
}
