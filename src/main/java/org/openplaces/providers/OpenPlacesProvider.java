package org.openplaces.providers;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.openplaces.helpers.HttpHelper;
import org.openplaces.helpers.OSMPlaceHelper;
import org.openplaces.model.NominationElement;
import org.openplaces.model.OLocation;
import org.openplaces.model.OSMPlace;
import org.openplaces.model.OverpassElement;
import org.openplaces.model.ReviewServerElement;
import org.openplaces.model.SearchSuggestionElement;
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
	
	
	public OpenPlacesProvider(HttpHelper hh, String userEmail,
                              String nominationServer, String overpassServer, String reviewServerServer) {
		super();
		
		this.nmp = new NominatimProvider(nominationServer, userEmail, hh);
		this.opp = new OverpassProvider(overpassServer, hh);
		this.rsp = new ReviewServerProvider(reviewServerServer, hh);
	}




    public Collection<OLocation> getLocationsByName(String name){

        List<NominationElement> nmRes = this.nmp.search(name);

        for(NominationElement n: nmRes){
            if(!"place".equals(n.getClazz()) && !"boundary".equals(n.getClazz())){
                logger.debug("Discarding >>{}<< bacause it is neither a place nor a boundary", n);
                continue;
            }
            System.out.println(n);
        }


        return null;
    }

    public Collection<OLocation> getLocationsAround(double lat, double lon, int radius){
        //TODO... use overpass since Nominatim does not allow this type of search
        return null;
    }
	
	
	
	public Collection<SearchSuggestionElement> getPlacesSearchSuggestions(double lat, double lon, String amenity, String partialName){
		List<OverpassElement> res = this.opp.getPlaces(lat, lon, 5000, amenity, partialName);
		List<SearchSuggestionElement> returnObj = new LinkedList<SearchSuggestionElement>();
		for(OverpassElement el: res){
			returnObj.add(new SearchSuggestionElement(SearchSuggestionElement.SuggestionType.PLACE, el));
		}
		return returnObj;
	}
	
	//TODO: use is_in, addr:city, etc... tags for building further suggestion
	public Collection<SearchSuggestionElement> getAroundLocations(double lat, double lon){
		List<OverpassElement> res = this.opp.getAroundLocations(lat, lon, 10000);
		List<SearchSuggestionElement> returnObj = new LinkedList<SearchSuggestionElement>();
		for(OverpassElement el: res){
			returnObj.add(new SearchSuggestionElement(SearchSuggestionElement.SuggestionType.LOCATION, el));
		}
		return returnObj;
	}
	
 	
	public void mergeReviewServerInfo(OSMPlace p){
		ReviewServerElement res = this.rsp.getPlace(Long.toString(p.getId()));
		OSMPlaceHelper.mergeWithReviewServerElement(p, res);
	}
	
	
	public Collection<OSMPlace> searchInside(
			double vbLeft, double vbTop, double vbRight, double vbBottom, String query){
		
		
		Map<Long, OSMPlace> res = new HashMap<Long, OSMPlace>();
		
		List<NominationElement> nmRes = this.nmp.searchInside(vbLeft, vbTop, vbRight, vbBottom, query);
		
		for(NominationElement n: nmRes){
			res.put(n.getOsm_id(), OSMPlaceHelper.createFromNominationElement(n));
		}
		
		this.completeWithOSMData(res);
		
		return res.values();	
	}
	
	

	public Collection<OSMPlace> search(String query){
		
		Map<Long, OSMPlace> res = new HashMap<Long, OSMPlace>();
		
		List<NominationElement> nmRes = this.nmp.search(query);
		
		for(NominationElement n: nmRes){
			res.put(n.getOsm_id(), OSMPlaceHelper.createFromNominationElement(n));
		}
		
		this.completeWithOSMData(res);
		
		return res.values();
	}
	
	
	private void completeWithOSMData(Map<Long, OSMPlace> places){
		
		if(places.isEmpty())
			return;

		Set<String[]> overPassInput = new HashSet<String[]>();
		for(OSMPlace p: places.values()){
			overPassInput.add(new String[]{p.getOsmType(), Long.toString(p.getId())});
		}
		List<OverpassElement> opRes = opp.getFromCoordsList(overPassInput);
		
		for(OverpassElement oe: opRes){
			OSMPlaceHelper.mergeWithOverpassElement(places.get(oe.getId()), oe);
		}		
	}
}
