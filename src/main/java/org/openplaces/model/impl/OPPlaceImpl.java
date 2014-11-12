package org.openplaces.model.impl;

import org.openplaces.internal.model.NominatimElement;
import org.openplaces.internal.model.OverpassElement;
import org.openplaces.internal.model.ReviewServerElement;
import org.openplaces.model.OPGeoPoint;
import org.openplaces.model.OPPlaceInterface;

import java.util.Map;

/**
 * Created by ggiammat on 11/5/14.
 */
public class OPPlaceImpl implements OPPlaceInterface {


    private long id;
    private String name;
    private String osmType;
    private Double averageRating;
    private Integer numReviews;
    private OPGeoPoint position;
    private String addressString;
    private Map<String, String> addressTokens;
    private String type;


    public OPPlaceImpl(){

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOsmType() {
        return osmType;
    }

    public void setOsmType(String osmType) {
        this.osmType = osmType;
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

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return this.getName() + ", " + " ("+this.getId()+") at " +this.position + " [" + this.getAddressTokens() + "]";
    }

    public OPGeoPoint getPosition() {
        return position;
    }

    public void setPosition(OPGeoPoint position) {
        this.position = position;
    }

    public String getAddressString() {
        return addressString;
    }

    public void setAddressString(String addressString) {
        this.addressString = addressString;
    }

    public Map<String, String> getAddressTokens() {
        return addressTokens;
    }

    public void setAddressTokens(Map<String, String> addressTokens) {
        this.addressTokens = addressTokens;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
