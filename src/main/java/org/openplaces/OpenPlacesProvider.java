package org.openplaces;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.openplaces.internal.NominatimProvider;
import org.openplaces.internal.OverpassProvider;
import org.openplaces.internal.ReviewServerProvider;
import org.openplaces.internal.helpers.OPPlaceHelper;
import org.openplaces.model.OPLocationInterface;
import org.openplaces.model.OPPlaceCategoryInterface;
import org.openplaces.model.OPPlaceInterface;
import org.openplaces.model.impl.OPLocationImpl;
import org.openplaces.utils.HttpHelper;
import org.openplaces.internal.model.NominatimElement;
import org.openplaces.model.OSMTagFilterGroup;
import org.openplaces.internal.model.OverpassElement;
import org.openplaces.internal.model.ReviewServerElement;
import org.openplaces.utils.GeoFunctions;
import org.openplaces.model.OPBoundingBox;
import org.openplaces.model.OPGeoPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class OpenPlacesProvider {

    Logger logger = LoggerFactory.getLogger(OpenPlacesProvider.class);

    //http://overpass.osm.rambler.ru/cgi/interpreter does not support "center" action
	public static final String OVERPASS_SERVER = "http://overpass-api.de/api/interpreter";

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


    public List<OPPlaceInterface> getPlacesByFreeQuery(String name){
        List<OPPlaceInterface> places = new LinkedList<OPPlaceInterface>();
        List<NominatimElement> nmRes = this.nmp.search(name);

        Map<Long, OPPlaceInterface> tmpMap = new HashMap<Long, OPPlaceInterface>();
        for(NominatimElement n: nmRes){

            OPPlaceInterface p = OPPlaceHelper.createFromNominatimElement(n);
            places.add(p);
            tmpMap.put(n.getOsm_id(), p);
        }

        this.completeWithOSMData(tmpMap);

        return places;
    }

    /**
     *
     * @param placesTypesAndIds each element in the form "type:id". E.g "node:123", "way:456"
     * @return
     */
    public List<OPPlaceInterface> getPlacesByTypesAndIds(Set<String> placesTypesAndIds){
        List<OPPlaceInterface> res = new ArrayList<OPPlaceInterface>();
        if(placesTypesAndIds.isEmpty()) {
            return res;
        }

        List<OverpassElement> opRes = opp.getFromTypeAndId(placesTypesAndIds);

        for(OverpassElement oe: opRes){
            res.add(OPPlaceHelper.createFromOverpassElement(oe));
        }
        return res;
    }

    public List<OPLocationInterface> getLocationsByName(String name){

        List<OPLocationInterface> locations = new LinkedList<OPLocationInterface>();
        List<NominatimElement> nmRes = this.nmp.search(name);

        for(NominatimElement n: nmRes){
            if(!"place".equals(n.getClazz()) && !"boundary".equals(n.getClazz())){
                logger.debug("Discarding >>{}<< bacause it is neither a place nor a boundary", n);
                continue;
            }
            locations.add(new OPLocationImpl(n));
        }

        return locations;
    }

    public List<OPLocationInterface> getLocationsAround(OPGeoPoint point, double radius){
        if(radius > this.getMaxSearchRadius()){
            logger.warn("Search radius is too big: {} km. Search will not be performed (max is {} km)", radius, this.getMaxSearchRadius());
            return new LinkedList<OPLocationInterface>();
        }
        List<OverpassElement> res = this.opp.getAroundLocations(point, Math.round(radius * 1000));
        List<OPLocationInterface> returnObj = new LinkedList<OPLocationInterface>();
        for(OverpassElement el: res){
            returnObj.add(new OPLocationImpl(el));
        }
        return returnObj;
    }


    public void addBoundingBoxFromNominatim(OPLocationInterface loc){

        List<OPLocationInterface> nlocs = this.getLocationsByName(loc.getDisplayName());
        for(OPLocationInterface nl: nlocs){
            if(nl.getBoundingBox().contains(loc.getPosition())){
                logger.debug("Setting bounding box from {}", nl);
                loc.setBoundingBox(nl.getBoundingBox());
                break;
            }
            logger.debug("Discarding because position not in the boundingbox: {}", nl);
        }
    }

    public void reverseGeocodePlace(OPPlaceInterface place){
        String type = "N";
        if("way".equals(place.getOsmType())){
            type = "W";
        }
        if("relation".equals(place.getOsmType())){
            type = "R";
        }
        NominatimElement el = this.nmp.reverse(Long.toString(place.getId()), type);
        OPPlaceHelper.loadDataFromNominatim(place, el);
    }


    public List<OPPlaceInterface> getPlaces(OPPlaceCategoryInterface type, List<OPLocationInterface> where){
        return this.getPlaces(Arrays.asList(new OPPlaceCategoryInterface[]{type}), where);
    }

    public List<OPPlaceInterface> getPlaces(List<OPPlaceCategoryInterface> types, OPLocationInterface where){
        return this.getPlaces(types, Arrays.asList(new OPLocationInterface[]{where}));
    }

    public List<OPPlaceInterface> getPlaces(OPPlaceCategoryInterface type, OPLocationInterface where){
        return this.getPlaces(Arrays.asList(new OPPlaceCategoryInterface[]{type}), Arrays.asList(new OPLocationInterface[]{where}));
    }

    public List<OPPlaceInterface> getPlaces(List<OPPlaceCategoryInterface> types, List<OPLocationInterface> where){

        List<OPBoundingBox> bboxes = new ArrayList<OPBoundingBox>();

        for(OPLocationInterface loc: where){
            if(loc.getBoundingBox() == null){
                logger.debug("Location boundingbox is null. Finding it from Nominatim");
                this.addBoundingBoxFromNominatim(loc);
            }
            if(GeoFunctions.boundingBoxArea(loc.getBoundingBox()) > this.getMaxSearchBoundingBox()){
                logger.warn("Bounding box is to large: {} sq. km. Search will not be performed (max is {} sq. km).", GeoFunctions.boundingBoxArea(loc.getBoundingBox()), this.getMaxSearchBoundingBox());
                return new LinkedList<OPPlaceInterface>();
            }
            bboxes.add(loc.getBoundingBox());
        }

        List<OSMTagFilterGroup> filters = new ArrayList<OSMTagFilterGroup>();
        for(OPPlaceCategoryInterface type: types){
            filters.addAll(type.getOsmTagFilterGroups());
        }

        Collection<OverpassElement> elements = this.opp.getPlaces(filters, bboxes);

        //to make it easy to access elements by id, put them in a map
        Map<Long, OverpassElement> tempMap = new HashMap<Long, OverpassElement>();
        for(OverpassElement el: elements){
            tempMap.put(el.getId(), el);
        }


        List<OPPlaceInterface> places = new LinkedList<OPPlaceInterface>();
        for(OverpassElement el: elements){


            places.add(OPPlaceHelper.createFromOverpassElement(el));

            //not necessary since "center" action in the query returns the center for areas in the
            //"center" tag of the response
//            if(el.getTags() == null){
//                //no tags. Assuming these are nodes referenced by a way which is in the resultset
//                continue;
//            }
//            if("node".equals(el.getType())){
//                places.add(OPPlaceHelper.createFromOverpassElement(el));
//            }
//            if("way".equals(el.getType())){
//                OPPlaceInterface p = OPPlaceHelper.createFromOverpassElement(el);
//                logger.debug("Way place found. Computing coordinates...");
//                OPGeoPoint[] points = new OPGeoPoint[el.getNodes().length];
//                for(int i = 0; i < el.getNodes().length; i++){
//                    OverpassElement n = tempMap.get(el.getNodes()[i]);
//                    points[i] = new OPGeoPoint(n.getLat(), n.getLon());
//                }
//                OPGeoPoint position = GeoFunctions.computeCentroid(points);
//                p.setPosition(position);
//                places.add(p);
//            }
        }

        return places;
    }


    private void completeWithOSMData(Map<Long, OPPlaceInterface> places){

        if(places.isEmpty()) {
            return;
        }

        logger.debug("Completing places data with Overpass for {} places", places.size());

        Set<String> overPassInput = new HashSet<String>();
        for(OPPlaceInterface p: places.values()){
            overPassInput.add(p.getOsmType() + ":" + Long.toString(p.getId()));
        }
        List<OverpassElement> opRes = opp.getFromTypeAndId(overPassInput);

        for(OverpassElement oe: opRes){
            OPPlaceHelper.loadDataFromOverpass(places.get(oe.getId()), oe);
        }
    }
	
 	
	public void mergeReviewServerInfo(OPPlaceInterface p){
		ReviewServerElement res = this.rsp.getPlace(Long.toString(p.getId()));
        OPPlaceHelper.loadDataFromReviewServer(p, res);
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
