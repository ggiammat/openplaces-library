package asd;

import java.util.List;

import org.osmplaces.helpers.HttpHelper;
import org.osmplaces.model.Place;
import org.osmplaces.providers.NominatimProvider;

public class Test {

	public static void main(String[] args) {
		HttpHelper hh = new HttpHelper();
		
		hh.setHttpProxy("www-proxy.eng.it", 8080);

		
		NominatimProvider nnp = new NominatimProvider(
				NominatimProvider.NOMINATIM_SERVER, 
				"gabriele.giammatteo@gmail.com",
				hh);
		
		List<Place> res = nnp.searchInside(12 , 41 ,13, 40, "ristoranti");
		
		for(Place p: res){
			System.out.println(p);
		}
		
	}

}
