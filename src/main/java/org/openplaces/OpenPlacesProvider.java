package org.openplaces;

import java.io.IOException;
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

import javax.xml.transform.Result;


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


    public OPProviderResultObject getPlacesByFreeQuery(String name){
        List<OPPlaceInterface> places = new LinkedList<OPPlaceInterface>();
        List<NominatimElement> nmRes = null;

        try {
            nmRes = this.nmp.search(name);
        } catch (IOException e) {
            e.printStackTrace();
            return OPProviderResultObject.getNetworkErrorRO(e.getMessage());
        }

        Map<Long, OPPlaceInterface> tmpMap = new HashMap<Long, OPPlaceInterface>();
        for(NominatimElement n: nmRes){

            OPPlaceInterface p = OPPlaceHelper.createFromNominatimElement(n);
            places.add(p);
            tmpMap.put(n.getOsm_id(), p);
        }

        try {
            this.completeWithOSMData(tmpMap);
        } catch (IOException e) {
            e.printStackTrace();
            return OPProviderResultObject.getNetworkErrorRO(e.getMessage());
        }

        OPProviderResultObject res = new OPProviderResultObject();
        res.places = places;
        return res;
    }

    /**
     *
     * @param placesTypesAndIds each element in the form "type:id". E.g "node:123", "way:456"
     * @return
     */
    public OPProviderResultObject getPlacesByTypesAndIds(Set<String> placesTypesAndIds){
        OPProviderResultObject resObj = new OPProviderResultObject();

        List<OPPlaceInterface> res = new ArrayList<OPPlaceInterface>();
        if(placesTypesAndIds.isEmpty()) {
            return resObj;
        }

        List<OverpassElement> opRes = null;
        try {
            opRes = opp.getFromTypeAndId(placesTypesAndIds);
        } catch (IOException e) {
            e.printStackTrace();
            return OPProviderResultObject.getNetworkErrorRO(e.getMessage());
        }

        for(OverpassElement oe: opRes){
            res.add(OPPlaceHelper.createFromOverpassElement(oe));
        }

        resObj.places = res;
        return resObj;
    }

    public OPProviderResultObject getLocationsByName(String name){
        OPProviderResultObject resObj = new OPProviderResultObject();

        List<OPLocationInterface> locations = new LinkedList<OPLocationInterface>();
        List<NominatimElement> nmRes = null;
        try {
            nmRes = this.nmp.search(name);
        } catch (IOException e) {
            e.printStackTrace();
            return OPProviderResultObject.getNetworkErrorRO(e.getMessage());
        }

        for(NominatimElement n: nmRes){
            if(!"place".equals(n.getClazz()) && !"boundary".equals(n.getClazz())){
                logger.debug("Discarding >>{}<< bacause it is neither a place nor a boundary", n);
                continue;
            }
            locations.add(new OPLocationImpl(n));
        }

        resObj.locations = locations;
        return resObj;
    }

    public OPProviderResultObject getLocationsAround(OPGeoPoint point, double radius){
        OPProviderResultObject resObj = new OPProviderResultObject();

        if(radius > this.getMaxSearchRadius()){
            resObj.errorCode = OPProviderResultObject.RADIUS_ERROR;
            resObj.errorMessage = "Search radius is too big: "+radius+" mt. Search will not be performed (max is "+this.getMaxSearchRadius()+" mt.)";
            logger.warn(resObj.errorMessage);
            return resObj;
        }
        List<OverpassElement> res = null;
        try {
            res = this.opp.getAroundLocations(point, Math.round(radius * 1000));
        } catch (IOException e) {
            e.printStackTrace();
            return OPProviderResultObject.getNetworkErrorRO(e.getMessage());
        }

        List<OPLocationInterface> returnObj = new LinkedList<OPLocationInterface>();
        for(OverpassElement el: res){
            returnObj.add(new OPLocationImpl(el));
        }
        resObj.locations = returnObj;
        return resObj;
    }


    public void addBoundingBoxFromNominatim(OPLocationInterface loc){

        List<OPLocationInterface> nlocs = this.getLocationsByName(loc.getDisplayName()).locations;
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
        NominatimElement el = null;
        try {
            el = this.nmp.reverse(Long.toString(place.getId()), type);
        } catch (IOException e) {
            e.printStackTrace();
        }
        OPPlaceHelper.loadDataFromNominatim(place, el);
    }



    public OPProviderResultObject getPlaces(OPPlaceCategoryInterface type, List<OPLocationInterface> where){
        return this.getPlaces(Arrays.asList(new OPPlaceCategoryInterface[]{type}), where, null);
    }

    public OPProviderResultObject getPlaces(List<OPPlaceCategoryInterface> types, OPLocationInterface where){
        return this.getPlaces(types, Arrays.asList(new OPLocationInterface[]{where}), null);
    }

    public OPProviderResultObject getPlaces(OPPlaceCategoryInterface type, OPLocationInterface where){
        return this.getPlaces(Arrays.asList(new OPPlaceCategoryInterface[]{type}), Arrays.asList(new OPLocationInterface[]{where}), null);
    }

    public OPProviderResultObject getPlaces(List<OPPlaceCategoryInterface> types, List<OPLocationInterface> where) {
        return this.getPlaces(types, where, null);
    }


    public OPProviderResultObject getPlaces(OPPlaceCategoryInterface type, List<OPLocationInterface> where, String nameMatching){
        return this.getPlaces(Arrays.asList(new OPPlaceCategoryInterface[]{type}), where, nameMatching);
    }

    public OPProviderResultObject getPlaces(List<OPPlaceCategoryInterface> types, OPLocationInterface where, String nameMatching){
        return this.getPlaces(types, Arrays.asList(new OPLocationInterface[]{where}), nameMatching);
    }

//    public List<OPPlaceInterface> getPlaces(OPPlaceCategoryInterface type, OPLocationInterface where, String nameMatching){
//        return this.getPlaces(Arrays.asList(new OPPlaceCategoryInterface[]{type}), Arrays.asList(new OPLocationInterface[]{where}), nameMatching);
//    }

    public OPProviderResultObject getPlaces(List<OPPlaceCategoryInterface> types, List<OPLocationInterface> where, String nameMatching){
        OPProviderResultObject res = new OPProviderResultObject();

        List<OPBoundingBox> bboxes = new ArrayList<OPBoundingBox>();

        for(OPLocationInterface loc: where){
            if(loc.getBoundingBox() == null){
                logger.debug("Location boundingbox is null. Searching it i Nominatim");
                this.addBoundingBoxFromNominatim(loc);
            }
            if(GeoFunctions.boundingBoxArea(loc.getBoundingBox()) > this.getMaxSearchBoundingBox()){
                res.errorCode = 1;
                res.errorMessage = "Search radius is too big: "+GeoFunctions.boundingBoxArea(loc.getBoundingBox())+" km. Search will not be performed (max is "+this.getMaxSearchBoundingBox()+" km)";
                logger.warn(res.errorMessage);
                return res;
            }
            bboxes.add(loc.getBoundingBox());
        }

        List<OSMTagFilterGroup> filters = new ArrayList<OSMTagFilterGroup>();

        if(types != null){
            for(OPPlaceCategoryInterface type: types){
                filters.addAll(type.getOsmTagFilterGroups());
            }
        }


        OSMTagFilterGroup nameFiltering = null;

        if(nameMatching != null){
            nameFiltering = new OSMTagFilterGroup().setTagFilter("name", OSMTagFilterGroup.OSMTagFilterPredicate.MATCHES, nameMatching);
        }


        Collection<OverpassElement> elements = null;
        try {
            elements = this.opp.getPlaces(filters, bboxes, nameFiltering);
        } catch (IOException e) {
            e.printStackTrace();
            return OPProviderResultObject.getNetworkErrorRO(e.getMessage());
        }

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

        res.places = places;
        return res;
    }


    private void completeWithOSMData(Map<Long, OPPlaceInterface> places) throws IOException {

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
	
 	
	public void mergeReviewServerInfo(OPPlaceInterface p) throws IOException {
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
