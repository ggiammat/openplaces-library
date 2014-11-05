package asd;

import org.openplaces.helpers.HttpHelper;
import org.openplaces.model.OPLocation;
import org.openplaces.model.OPPlace;
import org.openplaces.model.OPTagsFilter;
import org.openplaces.providers.OpenPlacesProvider;
import org.openplaces.utils.GeoFunctions;
import org.openplaces.utils.OPGeoPoint;

import java.util.List;

public class Test {

	public static void main(String[] args) {

		HttpHelper hh = new HttpHelper();
		
		//hh.setHttpProxy("www-proxy.eng.it", 8080);

		OpenPlacesProvider osmp = new OpenPlacesProvider(
				hh, "gabriele.giammatteo@gmail.com", 
				OpenPlacesProvider.NOMINATIM_SERVER,
				OpenPlacesProvider.OVERPASS_SERVER,
				OpenPlacesProvider.REVIEW_SERVER_SERVER);



/*


        System.exit(0);

        //41.5727358,12.6974885,41.7392634,12.8408451
        OPLocation velletri = new OPLocation();
        velletri.setBoundingBox(new OPBoundingBox(
            41.7392634, 12.8408451, 41.5727358, 12.6974885
        ));
*/


//        List<OPLocation> locations2 = osmp.getLocationsAround(new OPGeoPoint(41.73d, 12.84d), 10);
//        for(OPLocation loc: locations2){
//            System.out.println(locations2.indexOf(loc) + " " +loc);
//        }
//        GeoFunctions.sortByDistanceFromPoint(locations2, new OPGeoPoint(41.73d, 12.84d));
//        for(OPLocation loc: locations2){
//            System.out.println(locations2.indexOf(loc) + " " +loc);
//        }
//
//        System.exit(0);
//
//
        List<OPLocation> locations = osmp.getLocationsByName("Velletri");
        for(OPLocation loc: locations){
            System.out.println(loc);
        }
        OPTagsFilter filters = new OPTagsFilter().setTagFilter("amenity", OPTagsFilter.TagFilterOperation.IS_EQUALS_TO, "cinema");
//        filters.setTagFilter("name", OPTagsFilter.TagFilterOperation.MATCHES, "gioia");
        List<OPPlace> places = osmp.getPlaces(filters, locations.get(0));


        for(OPPlace p: places){
            osmp.reverseGeocodePlace(p);
            System.out.println(p);
        }

	}

}
