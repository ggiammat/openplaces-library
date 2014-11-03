package asd;

import java.util.Collection;
import java.util.Iterator;

import org.osmplaces.helpers.HttpHelper;
import org.osmplaces.model.SearchSuggestionElement;
import org.osmplaces.providers.OSMPlacesProvider;
import org.osmplaces.providers.OverpassProvider;

public class Test {

	public static void main(String[] args) {
		HttpHelper hh = new HttpHelper();
		
		hh.setHttpProxy("www-proxy.eng.it", 8080);

		OSMPlacesProvider osmp = new OSMPlacesProvider(
				hh, "gabriele.giammatteo@gmail.com", 
				OSMPlacesProvider.NOMINATIM_SERVER, 
				OSMPlacesProvider.OVERPASS_SERVER,
				OSMPlacesProvider.REVIEW_SERVER_SERVER);
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




		 */
		OverpassProvider opp = new OverpassProvider(OSMPlacesProvider.OVERPASS_SERVER, hh);
		
		//Collection<OverpassElement> res = opp.search("<osm-script><query type=\"node\"><has-kv k=\"name\" regv=\"holtorf\"/><bbox-query e=\"7.25\" n=\"50.8\" s=\"50.7\" w=\"7.1\"/></query><print/></osm-script>");
		
		Collection<SearchSuggestionElement> res = osmp.getAroundLocations(41.688d, 12.77d);
		
		//Collection<SearchSuggestionElement> res = osmp.getPlacesSearchSuggestions(41.68d,12.77d, null, "velletrano");
		
		for (Iterator iterator = res.iterator(); iterator.hasNext();) {
			SearchSuggestionElement overpassElement = (SearchSuggestionElement) iterator.next();
			System.out.println(overpassElement);
		}
		
	}

}
