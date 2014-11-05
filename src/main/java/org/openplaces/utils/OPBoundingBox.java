package org.openplaces.utils;

/**
 * Created by ggiammat on 11/5/14.
 */
public class OPBoundingBox {

    private double north;
    private double east;
    private double south;
    private double west;

    public OPBoundingBox(double north, double east, double south, double west) {
        this.north = north;
        this.east = east;
        this.south = south;
        this.west = west;
    }


    public boolean contains(OPGeoPoint position) {
        return
                this.getNorth() > position.getLat() &&
                this.getEast() > position.getLon() &&
                this.getSouth() < position.getLat() &&
                this.getWest() < position.getLon();
    }

    public double getNorth() {
        return north;
    }

    public void setNorth(double north) {
        this.north = north;
    }

    public double getEast() {
        return east;
    }

    public void setEast(double east) {
        this.east = east;
    }

    public double getSouth() {
        return south;
    }

    public void setSouth(double south) {
        this.south = south;
    }

    public double getWest() {
        return west;
    }

    public void setWest(double west) {
        this.west = west;
    }

    @Override
    public String toString() {
        return "[" + this.getNorth() + "," + this.getEast() + "," + this.getSouth() + "," + this.getWest() + "]";
    }


}
