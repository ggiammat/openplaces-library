package org.openplaces.internal.helpers;

import org.openplaces.internal.model.NominatimElement;
import org.openplaces.internal.model.OverpassElement;
import org.openplaces.internal.model.ReviewServerElement;
import org.openplaces.model.OPGeoPoint;
import org.openplaces.model.OPLocationInterface;
import org.openplaces.model.OPPlaceInterface;
import org.openplaces.model.impl.OPPlaceImpl;

/**
 * Created by ggiammat on 11/12/14.
 */
public class OPPlaceHelper {


    public static OPPlaceInterface createFromOPLocation(OPLocationInterface loc){
        OPPlaceInterface place = new OPPlaceImpl();
        place.setId(loc.getId());
        place.setName(loc.getDisplayName());
        place.setPosition(loc.getPosition());
        place.setOsmType(loc.getType());
        return place;
    }


    public static OPPlaceInterface createFromOverpassElement(OverpassElement el){
        OPPlaceImpl place = new OPPlaceImpl();
        loadDataFromOverpass(place, el);
        return place;
    }

    public static void loadDataFromOverpass(OPPlaceInterface place, OverpassElement el){
        place.setId(el.getId());
        place.setName(el.getTag("name", null));
        place.setOsmType(el.getType());
        place.setOsmTags(el.getTags());
        //if position is available from overpass, overwrite current position
        //(the position from Nominatim might be computed and not from OSM data)
        if(el.getLat() != null && el.getLon() != null) {
            place.setPosition(new OPGeoPoint(el.getLat(), el.getLon()));
        }
        else if(el.getCenter() != null) {
            place.setPosition(new OPGeoPoint(el.getCenter().get("lat"), el.getCenter().get("lon")));
        }
    }

    public static OPPlaceInterface createFromNominatimElement(NominatimElement el){
        OPPlaceImpl place = new OPPlaceImpl();
        loadDataFromNominatim(place, el);
        return place;
    }

    public static void loadDataFromNominatim(OPPlaceInterface place, NominatimElement el){
        place.setId(el.getOsm_id());
        place.setOsmType(el.getOsm_type());
        //if position is already set (with overpass) do not overwrite
        if(place.getPosition() == null && el.getLat() != null && el.getLon() != null) {
            place.setPosition(new OPGeoPoint(el.getLat(), el.getLon()));
        }
        place.setAddressString(el.getDisplay_name());
        place.setAddressTokens(el.getAddress());
        place.setType(el.getType());
    }

    public static void loadDataFromReviewServer(OPPlaceInterface place, ReviewServerElement re){
        place.setNumReviews(re.getNumReviews());
        place.setAverageRating(re.getAverageRating());
    }

}
