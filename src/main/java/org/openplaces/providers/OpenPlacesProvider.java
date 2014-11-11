package org.openplaces.providers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.openplaces.helpers.HttpHelper;
import org.openplaces.model.NominatimElement;
import org.openplaces.model.OPLocation;
import org.openplaces.model.OPPlace;
import org.openplaces.model.OSMTagFilterGroup;
import org.openplaces.model.OverpassElement;
import org.openplaces.model.ReviewServerElement;
import org.openplaces.types.OPPlaceType;
import org.openplaces.utils.GeoFunctions;
import org.openplaces.utils.OPBoundingBox;
import org.openplaces.utils.OPGeoPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class OpenPlacesProvider {

    Logger logger = LoggerFactory.getLogger(OpenPlacesProvider.class);
	
	public static final String OVERPASS_SERVER = "http://overpass.osm.rambler.ru/cgi/interpreter";
	public static final String NOMINATIM_SERVER = "http://nominatim.openstreetmap.org/";
	public static final String REVIEW_SERVER_SERVER = "http://osmplaces-gi4mmyz.rhcloud.com/rest";
	
	private NominatimProvider nmp;
	private OverpassProvider opp;
	private ReviewServerProvider rsp;
    private double maxSearchBoundingBox = 1000; //square km
    private int maxSearchRadius = 50; // km
	
	
	public OpenPlacesProvider(HttpHelper hh, String userEmail,
                              String nominationServer, String overpassServer, String reviewServerServer) {
		super();
		
		this.nmp = new NominatimProvider(nominationServer, userEmail, hh);
		this.opp = new OverpassProvider(overpassServer, hh);
		this.rsp = new ReviewServerProvider(reviewServerServer, hh);
	}


    public List<OPPlace> getPlacesByFreeQuery(String name){
        List<OPPlace> places = new LinkedList<OPPlace>();
        List<NominatimElement> nmRes = this.nmp.search(name);

        Map<Long, OPPlace> tmpMap = new HashMap<Long, OPPlace>();
        for(NominatimElement n: nmRes){
            OPPlace p = new OPPlace(n);
            places.add(p);
            tmpMap.put(n.getOsm_id(), p);
        }

        this.completeWithOSMData(tmpMap);

        return places;
    }


    public List<OPLocation> getLocationsByName(String name){

        List<OPLocation> locations = new LinkedList<OPLocation>();
        List<NominatimElement> nmRes = this.nmp.search(name);

        for(NominatimElement n: nmRes){
            if(!"place".equals(n.getClazz()) && !"boundary".equals(n.getClazz())){
                logger.debug("Discarding >>{}<< bacause it is neither a place nor a boundary", n);
                continue;
            }
            locations.add(new OPLocation(n));
        }

        return locations;
    }

    public List<OPLocation> getLocationsAround(OPGeoPoint point, double radius){
        if(radius > this.getMaxSearchRadius()){
            logger.warn("Search radius is too big: {} km. Search will not be performed (max is {} km)", radius, this.getMaxSearchRadius());
            return new LinkedList<OPLocation>();
        }
        List<OverpassElement> res = this.opp.getAroundLocations(point, Math.round(radius * 1000));
        List<OPLocation> returnObj = new LinkedList<OPLocation>();
        for(OverpassElement el: res){
            returnObj.add(new OPLocation(el));
        }
        return returnObj;
    }


    public void addBoundingBoxFromNominatim(OPLocation loc){

        List<OPLocation> nlocs = this.getLocationsByName(loc.getDisplayName());
        for(OPLocation nl: nlocs){
            if(nl.getBoundingBox().contains(loc.getPosition())){
                logger.debug("Setting bounding box from {}", nl);
                loc.setBoundingBox(nl.getBoundingBox());
                break;
            }
            logger.debug("Discarding because position not in the boundingbox: {}", nl);
        }
    }

    public void reverseGeocodePlace(OPPlace place){
        String type = "N";
        if("way".equals(place.getOsmType())){
            type = "W";
        }
        if("relation".equals(place.getOsmType())){
            type = "R";
        }
        NominatimElement el = this.nmp.reverse(Long.toString(place.getId()), type);
        place.loadDataFromNominatim(el);
    }


    public List<OPPlace> getPlaces(List<OPPlaceType> types, List<OPLocation> where){

        List<OPBoundingBox> bboxes = new ArrayList<OPBoundingBox>();

        for(OPLocation loc: where){
            if(loc.getBoundingBox() == null){
                logger.debug("Location boundingbox is null. Finding it from Nominatim");
                this.addBoundingBoxFromNominatim(loc);
            }
            if(GeoFunctions.boundingBoxArea(loc.getBoundingBox()) > this.getMaxSearchBoundingBox()){
                logger.warn("Bounding box is to large: {} sq. km. Search will not be performed (max is {} sq. km).", GeoFunctions.boundingBoxArea(loc.getBoundingBox()), this.getMaxSearchBoundingBox());
                return new LinkedList<OPPlace>();
            }
            bboxes.add(loc.getBoundingBox());
        }

        List<OSMTagFilterGroup> filters = new ArrayList<OSMTagFilterGroup>();
        for(OPPlaceType type: types){
            filters.addAll(type.getOsmTagFilterGroups());
        }

        Collection<OverpassElement> elements = this.opp.getPlaces(filters, bboxes);

        //to make it easy to access elements by id, put them in a map
        Map<Long, OverpassElement> tempMap = new HashMap<Long, OverpassElement>();
        for(OverpassElement el: elements){
            tempMap.put(el.getId(), el);
        }


        List<OPPlace> places = new LinkedList<OPPlace>();
        for(OverpassElement el: elements){
            if(el.getTags() == null){
                //no tags. Assuming these are nodes referenced by a way which is in the resultset
                continue;
            }
            if("node".equals(el.getType())){
                places.add(new OPPlace(el));
            }

            if("way".equals(el.getType())){
                OPPlace p = new OPPlace(el);
                logger.debug("Way place found. Computing coordinates...");
                OPGeoPoint[] points = new OPGeoPoint[el.getNodes().length];
                for(int i = 0; i < el.getNodes().length; i++){
                    OverpassElement n = tempMap.get(el.getNodes()[i]);
                    points[i] = new OPGeoPoint(n.getLat(), n.getLon());
                }
                OPGeoPoint position = GeoFunctions.computeCentroid(points);
                p.setPosition(position);
                places.add(p);
            }
        }

        return places;
    }


    private void completeWithOSMData(Map<Long, OPPlace> places){

        if(places.isEmpty()) {
            return;
        }

        logger.debug("Completing places data with Overpass for {} places", places.size());

        Set<String[]> overPassInput = new HashSet<String[]>();
        for(OPPlace p: places.values()){
            overPassInput.add(new String[]{p.getOsmType(), Long.toString(p.getId())});
        }
        List<OverpassElement> opRes = opp.getFromCoordsList(overPassInput);

        for(OverpassElement oe: opRes){
            places.get(oe.getId()).loadDataFromOverpass(oe);
        }
    }
	
 	
	public void mergeReviewServerInfo(OPPlace p){
		ReviewServerElement res = this.rsp.getPlace(Long.toString(p.getId()));
		p.loadDataFromReviewServer(res);
	}

    public double getMaxSearchBoundingBox() {
        return maxSearchBoundingBox;
    }

    public void setMaxSearchBoundingBox(double maxSearchBoundingBox) {
        this.maxSearchBoundingBox = maxSearchBoundingBox;
    }

    public int getMaxSearchRadius() {
        return maxSearchRadius;
    }

    public void setMaxSearchRadius(int maxSearchRadius) {
        this.maxSearchRadius = maxSearchRadius;
    }
}
