package org.openplaces.utils;

/**
 * Created by ggiammat on 11/5/14.
 */
public class OPGeoPoint {

    private double lat;
    private double lon;

    public OPGeoPoint(double lat, double lon){
        this.setLat(lat);
        this.setLon(lon);
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


    @Override
    public String toString() {
        return "(" + this.lat + "," + this.lon + ")";
    }
}
