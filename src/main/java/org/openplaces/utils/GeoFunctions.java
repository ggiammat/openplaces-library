package org.openplaces.utils;

import org.openplaces.model.OPBoundingBox;
import org.openplaces.model.OPGeoPoint;
import org.openplaces.model.OPLocationInterface;
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

    /**
     *
     * @param center
     * @param area of the generated bb in square KM
     * @return
     */
    public static OPBoundingBox generateBoundingBox(OPGeoPoint center, double area){
        double r = 6378.137d; //Radius of earth in KM

        double bblength = Math.sqrt(area);
        System.out.println(bblength);
        OPGeoPoint p1 = getDestinationPoint(center, bblength / 2, 0);
        OPGeoPoint p2 = getDestinationPoint(center, bblength / 2, 90);
        OPGeoPoint p3 = getDestinationPoint(center, bblength / 2, 180);
        OPGeoPoint p4 = getDestinationPoint(center, bblength / 2, 270);

        return new OPBoundingBox(p1.getLat(), p2.getLon(), p3.getLat(), p4.getLon());
    }

    /**
     * see http://stackoverflow.com/questions/3225803/calculate-endpoint-given-distance-bearing-starting-point
     * @param center
     * @param distance in km
     * @param bearing in degree clockwise from north
     * @return
     */
    public static OPGeoPoint getDestinationPoint(OPGeoPoint center, double distance, double bearing){
        double bearingRad = bearing * (Math.PI / 180);
        double latRad = center.getLat() * (Math.PI / 180);
        double lonRad = center.getLon() * (Math.PI / 180);

//        System.out.println("Center is " + center);
//        System.out.println("Distance is "  + distance);
//        System.out.println("Bearing in rad is " + bearingRad);
        double r = 6378.137d; //Radius of earth in KM
        double lat2 =
                Math.asin(
                    Math.sin(latRad) * Math.cos(distance/r) +
                    Math.cos(latRad) * Math.sin(distance/r) * Math.cos(bearingRad)
                );
//        System.out.println("lat2 is " + lat2);
        double lon2 = lonRad + Math.atan2(Math.sin(bearingRad) * Math.sin(distance/r)*Math.cos(lonRad),
                Math.cos(distance/r)-Math.sin(latRad)*Math.sin(lat2));
//        System.out.println("lon2 is " + lon2);

//
//        var φ2 = Math.asin( Math.sin(φ1)*Math.cos(d/R) +
//                Math.cos(φ1)*Math.sin(d/R)*Math.cos(brng) );
//        var λ2 = λ1 + Math.atan2(Math.sin(brng)*Math.sin(d/R)*Math.cos(φ1),
//                Math.cos(d/R)-Math.sin(φ1)*Math.sin(φ2));
        return new OPGeoPoint(lat2 * (180/Math.PI), lon2 * (180/Math.PI));
    }


        /**
         *
         * @param p1
         * @param p2
         * @return distance in meters
         */
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

    public static void sortByDistanceFromPoint(List<OPLocationInterface> locs, final OPGeoPoint point){
        Collections.sort(locs, new Comparator<OPLocationInterface>() {
            @Override
            public int compare(OPLocationInterface opLocation, OPLocationInterface opLocation2) {
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
