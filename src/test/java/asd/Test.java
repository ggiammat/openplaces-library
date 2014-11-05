package asd;

import java.util.Collection;
import java.util.Iterator;
import java.util.logging.ConsoleHandler;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import org.openplaces.helpers.HttpHelper;
import org.openplaces.model.SearchSuggestionElement;
import org.openplaces.providers.OpenPlacesProvider;
import org.openplaces.providers.OverpassProvider;

public class Test {

	public static void main(String[] args) {
//
//        Handler ch = new ConsoleHandler();
//        Formatter ft = new SimpleFormatter();
//
//        ch.setFormatter(ft);
//        ch.setLevel (Level.FINE);
//        Logger logger = Logger.getLogger("org.openplaces");
//        logger.addHandler (ch);
//        logger.setLevel(Level.FINE);

		HttpHelper hh = new HttpHelper();
		
		hh.setHttpProxy("www-proxy.eng.it", 8080);

		OpenPlacesProvider osmp = new OpenPlacesProvider(
				hh, "gabriele.giammatteo@gmail.com", 
				OpenPlacesProvider.NOMINATIM_SERVER,
				OpenPlacesProvider.OVERPASS_SERVER,
				OpenPlacesProvider.REVIEW_SERVER_SERVER);


        System.out.println(osmp.getLocationsByName("roma"));
//
//		
//		Collection<OSMPlace> res = osmp.searchInside(
//				12.76, 41.69, 12.79, 41.67, "ristoranti");
//		for(OSMPlace p: res){
//			osmp.mergeReviewServerInfo(p);
//			System.out.println(p);
//		}
	
		
		
		
		/*


node
  (around:5000,41.68,12.77)["amenity"]["name"~"[vV]elletrano"];
out body;

		
		/*
		 * [out:json][timeout:25];
// gather results
(
  // query part for: “place=*”
  node(around:15000,41.9008,12.5019)["place"];
);
// print results
out body;
>;
out skel qt;




//		 */
//		OverpassProvider opp = new OverpassProvider(OpenPlacesProvider.OVERPASS_SERVER, hh);
//
//		//Collection<OverpassElement> res = opp.search("<osm-script><query type=\"node\"><has-kv k=\"name\" regv=\"holtorf\"/><bbox-query e=\"7.25\" n=\"50.8\" s=\"50.7\" w=\"7.1\"/></query><print/></osm-script>");
//
//		Collection<SearchSuggestionElement> res = osmp.getAroundLocations(41.688d, 12.77d);
//
//		//Collection<SearchSuggestionElement> res = osmp.getPlacesSearchSuggestions(41.68d,12.77d, null, "velletrano");
//
//		for (Iterator iterator = res.iterator(); iterator.hasNext();) {
//			SearchSuggestionElement overpassElement = (SearchSuggestionElement) iterator.next();
//			System.out.println(overpassElement);
//		}
		
	}

}
