package asd;

import org.openplaces.model.OPBoundingBox;
import org.openplaces.model.OPLocationInterface;
import org.openplaces.model.OPPlaceCategoriesLibrary;
import org.openplaces.model.OPPlaceInterface;
import org.openplaces.model.impl.OPLocationImpl;
import org.openplaces.utils.GeoFunctions;
import org.openplaces.utils.HttpHelper;
import org.openplaces.OpenPlacesProvider;
import org.openplaces.model.OPGeoPoint;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Test {

	public static void main(String[] args) {


		HttpHelper hh = new HttpHelper();
		
		//hh.setHttpProxy("www-proxy.eng.it", 8080);

		OpenPlacesProvider osmp = new OpenPlacesProvider(
				hh, "gabriele.giammatteo@gmail.com", 
				OpenPlacesProvider.NOMINATIM_SERVER,
				OpenPlacesProvider.OVERPASS_SERVER,
				OpenPlacesProvider.REVIEW_SERVER_SERVER);


//
//        List<OPLocationInterface> locations2 = osmp.getLocationsAround(new OPGeoPoint(41.73d, 12.84d), 2);
//        for(OPLocationInterface loc: locations2){
//            System.out.println(locations2.indexOf(loc) + " " +loc);
//        }




        OPLocationInterface loc = osmp.getLocationsByName("Velletri").get(0);

        OPPlaceCategoriesLibrary r = OPPlaceCategoriesLibrary.loadFromFile("/home/gabriele/projects/P.OpenPlaces/workspace/openplaces-android/app/src/main/res/raw/default_categories_library.json");
        System.out.println(r);

        List<OPPlaceInterface> res = osmp.getPlaces(r.getCategories().get(0), loc);

        for(OPPlaceInterface p: res){
            System.out.println(p);
            System.out.println(r.getCategories().get(2).placeMatchesCategory(p));
        }


//
//        Set<String> places = new HashSet<String>();
//        places.add("node:2187805971");
//        places.add("way:207894705");
//        places.add("node:672980664");
//        List<OPPlaceInterface> res = osmp.getPlacesByTypesAndIds(places);
//
//        for(OPPlaceInterface p: res){
//            System.out.println(p);
//        }


	}

}
