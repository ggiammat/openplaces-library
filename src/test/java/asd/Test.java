package asd;

import java.util.Collection;

import org.osmplaces.helpers.HttpHelper;
import org.osmplaces.model.OSMPlace;
import org.osmplaces.providers.OSMPlacesProvider;

public class Test {

	public static void main(String[] args) {
		HttpHelper hh = new HttpHelper();
		
		//hh.setHttpProxy("www-proxy.eng.it", 8080);

		OSMPlacesProvider osmp = new OSMPlacesProvider(
				hh, "gabriele.giammatteo@gmail.com", 
				OSMPlacesProvider.NOMINATIM_SERVER, 
				OSMPlacesProvider.OVERPASS_SERVER,
				OSMPlacesProvider.REVIEW_SERVER_SERVER);

		
		Collection<OSMPlace> res = osmp.searchInside(
				12.76, 41.69, 12.79, 41.67, "ristoranti");
		for(OSMPlace p: res){
			osmp.mergeReviewServerInfo(p);
			System.out.println(p);
		}
	
	}

}
