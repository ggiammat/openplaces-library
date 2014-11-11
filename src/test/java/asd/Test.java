package asd;

import org.openplaces.helpers.HttpHelper;
import org.openplaces.model.OPLocation;
import org.openplaces.model.OPPlace;
import org.openplaces.model.OSMTagFilterGroup;
import org.openplaces.providers.OpenPlacesProvider;
import org.openplaces.types.OPPlaceTypesLibrary;
import org.openplaces.utils.GeoFunctions;
import org.openplaces.utils.OPGeoPoint;

import java.util.List;

public class Test {

	public static void main(String[] args) {

		HttpHelper hh = new HttpHelper();
		
		hh.setHttpProxy("www-proxy.eng.it", 8080);

		OpenPlacesProvider osmp = new OpenPlacesProvider(
				hh, "gabriele.giammatteo@gmail.com", 
				OpenPlacesProvider.NOMINATIM_SERVER,
				OpenPlacesProvider.OVERPASS_SERVER,
				OpenPlacesProvider.REVIEW_SERVER_SERVER);



        List<OPLocation> locations2 = osmp.getLocationsAround(new OPGeoPoint(41.73d, 12.84d), 2.55555555);
        for(OPLocation loc: locations2){
            System.out.println(locations2.indexOf(loc) + " " +loc);
        }

        OPPlaceTypesLibrary r = OPPlaceTypesLibrary.loadFromFile("/home/ggiammat/projects/P.OSMPlaces/workspace/openplaces-library/src/test/resources/default-types-library.json");
        System.out.println(r);

        List<OPPlace> res = osmp.getPlaces(r.getTypes().subList(2, 3), locations2);

        for(OPPlace p: res){
            System.out.println(p);
        }

//        GeoFunctions.sortByDistanceFromPoint(locations2, new OPGeoPoint(41.73d, 12.84d));
//        for(OPLocation loc: locations2){
//            System.out.println(locations2.indexOf(loc) + " " +loc);
//        }


	}

}
