package org.openplaces.model;

import org.openplaces.utils.OPGeoPoint;

import java.util.Map;

/**
 * Created by ggiammat on 11/5/14.
 */
public class OPPlace {


    private long id;
    private String name;
    private String osmType;
    private Double averageRating;
    private Integer numReviews;
    private OPGeoPoint position;
    private String addressString;
    private Map<String, String> addressTokens;
    private String type;


    public OPPlace(OverpassElement el) {
        this.loadDataFromOverpass(el);
    }


    public OPPlace(NominatimElement el) {
        this.loadDataFromNominatim(el);
    }


    public void loadDataFromOverpass(OverpassElement el){
        this.setId(el.getId());
        this.setName(el.getTag("name", null));
        this.setOsmType(el.getType());
        //if position is available from overpass, overwrite current position
        //(the position from Nominatim might be computed and not from OSM data)
        if(el.getLat() != null && el.getLon() != null) {
            this.setPosition(new OPGeoPoint(el.getLat(), el.getLon()));
        }
    }

    public void loadDataFromNominatim(NominatimElement el){
        this.setId(el.getOsm_id());
        this.setOsmType(el.getOsm_type());
        //if position is already set (with overpass) do not overwrite
        if(this.getPosition() == null && el.getLat() != null && el.getLon() != null) {
            this.setPosition(new OPGeoPoint(el.getLat(), el.getLon()));
        }
        this.setAddressString(el.getDisplay_name());
        this.setAddressTokens(el.getAddress());
        this.setType(el.getType());
    }

    public void loadDataFromReviewServer(ReviewServerElement re){
        this.setNumReviews(re.getNumReviews());
        this.setAverageRating(re.getAverageRating());
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
