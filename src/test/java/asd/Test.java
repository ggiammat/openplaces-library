package asd;

import org.openplaces.model.OPLocationInterface;
import org.openplaces.model.OPPlaceCategoriesLibrary;
import org.openplaces.model.OPPlaceInterface;
import org.openplaces.utils.GeoFunctions;
import org.openplaces.utils.HttpHelper;
import org.openplaces.OpenPlacesProvider;
import org.openplaces.model.OPGeoPoint;

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



//        List<OPLocationInterface> locations2 = osmp.getLocationsAround(new OPGeoPoint(41.73d, 12.84d), 2.55555555);
//        for(OPLocationInterface loc: locations2){
//            System.out.println(locations2.indexOf(loc) + " " +loc);
//        }


        OPLocationInterface loc = osmp.getLocationsByName("Velletri").get(0);

        OPPlaceCategoriesLibrary r = OPPlaceCategoriesLibrary.loadFromFile("/home/ggiammat/projects/P.OSMPlaces/workspace/openplaces-library/src/test/resources/default-categories-library.json");
        System.out.println(r);

        List<OPPlaceInterface> res = osmp.getPlaces(r.getCategories().get(3), loc);

        for(OPPlaceInterface p: res){
            System.out.println(p);
        }



	}

}
