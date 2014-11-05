package org.openplaces.helpers;

import org.openplaces.model.NominationElement;
import org.openplaces.model.OSMPlace;
import org.openplaces.model.OverpassElement;
import org.openplaces.model.ReviewServerElement;

public class OSMPlaceHelper {

	
	public static OSMPlace createFromNominationElement(NominationElement nme){
		OSMPlace p = new OSMPlace();
		
		p.setId(nme.getOsm_id());
		p.setLat(nme.getLat());
		p.setLon(nme.getLon());
		p.setOsmType(nme.getOsm_type());
		p.setFullAddress(nme.getDisplay_name());
		
		return p;
	}
	
	public static void mergeWithOverpassElement(OSMPlace p, OverpassElement oe){
		
		if(oe.hasTag("name") && p.getName() == null){
			p.setName(oe.getTag("name"));
		}
		if(oe.hasTag("amenity") && p.getOsmAmenity() == null){
			p.setOsmAmenity(oe.getTag("amenity"));
		}	
	}
	
	public static void mergeWithReviewServerElement(OSMPlace p, ReviewServerElement re){
		p.setNumReviews(re.getNumReviews());
		p.setAverageRating(re.getAverageRating());
	}
}
