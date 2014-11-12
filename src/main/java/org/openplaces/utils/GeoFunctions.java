package org.openplaces.utils;

import org.openplaces.model.OPBoundingBox;
import org.openplaces.model.OPGeoPoint;
import org.openplaces.model.impl.OPLocationImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by ggiammat on 11/5/14.
 */
public class GeoFunctions {

    static Logger logger = LoggerFactory.getLogger(GeoFunctions.class);


    public static OPGeoPoint computeCentroid(OPGeoPoint[] points){
        int numPoints = points.length;
        double sumLat = 0;
        double sumLon = 0;
        for(int i=0; i < numPoints; i++){
            sumLat += points[i].getLat();
            sumLon += points[i].getLon();
        }

        return new OPGeoPoint(sumLat/numPoints, sumLon/numPoints);
    }

    /**
     * returns the area of the region in the bounding box in square KM
     * @param bb
     * @return
     */
    public static double boundingBoxArea(OPBoundingBox bb){
        double dLat = distance(new OPGeoPoint(bb.getSouth(), bb.getEast()), new OPGeoPoint(bb.getNorth(), bb.getEast()));
        double dLon = distance(new OPGeoPoint(bb.getSouth(), bb.getWest()), new OPGeoPoint(bb.getSouth(), bb.getEast()));

        return (dLat * dLon) * 1E-6;
    }

    public static double distance(OPGeoPoint p1, OPGeoPoint p2){
        double r = 6378.137d; //Radius of earth in KM
        double dLat = (p2.getLat() - p1.getLat()) * Math.PI / 180;
        double dLon = (p2.getLon() - p1.getLon()) * Math.PI / 180;
        double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
                Math.cos(p1.getLat() * Math.PI / 180) * Math.cos(p2.getLat() * Math.PI / 180) *
                        Math.sin(dLon/2) * Math.sin(dLon/2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        double d = r * c;
        return d * 1000;
    }

    public static void sortByDistanceFromPoint(List<OPLocationImpl> locs, final OPGeoPoint point){
        Collections.sort(locs, new Comparator<OPLocationImpl>() {
            @Override
            public int compare(OPLocationImpl opLocation, OPLocationImpl opLocation2) {
                if(distance(opLocation.getPosition(), point) > distance(opLocation2.getPosition(), point)){
                    return 1;
                }
                else {
                    return -1;
                }
            }
        });
    }
}
