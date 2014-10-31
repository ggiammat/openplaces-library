package org.osmplaces.providers;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.osmplaces.helpers.HttpHelper;
import org.osmplaces.helpers.OSMPlaceHelper;
import org.osmplaces.model.NominationElement;
import org.osmplaces.model.OSMPlace;
import org.osmplaces.model.OverpassElement;
import org.osmplaces.model.ReviewServerElement;


public class OSMPlacesProvider {
	
	public static final String OVERPASS_SERVER = "http://overpass.osm.rambler.ru/cgi/interpreter";
	public static final String NOMINATIM_SERVER = "http://nominatim.openstreetmap.org/";
	public static final String REVIEW_SERVER_SERVER = "http://osmplaces-gi4mmyz.rhcloud.com/rest";
	
	private NominatimProvider nmp;
	private OverpassProvider opp;
	private ReviewServerProvider rsp;
	
	
	public OSMPlacesProvider(HttpHelper hh, String userEmail,
			String nominationServer, String overpassServer, String reviewServerServer) {
		super();
		
		this.nmp = new NominatimProvider(nominationServer, userEmail, hh);
		this.opp = new OverpassProvider(overpassServer, hh);
		this.rsp = new ReviewServerProvider(reviewServerServer, hh);
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
