package org.openplaces.providers;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openplaces.helpers.HttpHelper;
import org.openplaces.model.NominatimElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;


public class NominatimProvider {


    Logger logger = LoggerFactory.getLogger(NominatimProvider.class);


    private String server;
	private HttpHelper hh;
	private String userEmail;
	
	public NominatimProvider(String server, String userEmail, HttpHelper hh){
		this.server = server;
		this.hh = hh;
		this.userEmail = userEmail;
	}

	
	public List<NominatimElement> search(String query){
		Map<String, String> queryStringParams = new HashMap<String, String>();
		queryStringParams.put("q", hh.encodeString(query));
        queryStringParams.put("addressdetails", "1");
		return this.jsonArrayToPlace(this.doCall("search", queryStringParams));
	}

	public NominatimElement reverse(String osmId, String osmType){
        Map<String, String> queryParams = new HashMap<String, String>();
        queryParams.put("osm_id", osmId);
        queryParams.put("osm_type", osmType);
        queryParams.put("addressdetails", "1");

        NominatimElement res = this.jsonObjectToPlace(this.doCall("reverse", queryParams));
        return res;
    }
	
	private String doCall(String method, Map<String, String> queryStringParams){
        if(!"search".equals(method) && !"reverse".equals(method)){
            logger.debug("Invalid method: {}. Only 'search' and 'reverse' are recognized", method);
        }
		queryStringParams.put("format", "json");
		queryStringParams.put("email", this.userEmail);

		String url = this.server + method + "?" + this.buildQueryString(queryStringParams);
		
		String response = hh.doGET(url);
		
		return response;
	}
	
	
	private String buildQueryString(Map<String, String> queryStringParams){
		StringBuffer qs = new StringBuffer();
		
		for(String k: queryStringParams.keySet()){
			qs.append(k +"="+queryStringParams.get(k)+"&");
		}
		
		return qs.toString().substring(0, qs.length()-1);
	}


    private NominatimElement jsonObjectToPlace(String jstring){

        Gson gson = new Gson();
        NominatimElement place = gson.fromJson(jstring, NominatimElement.class);


        return place;
    }
	
	private List<NominatimElement> jsonArrayToPlace(String jstring){
		
		Gson gson = new Gson();
		NominatimElement[] places = gson.fromJson(jstring, NominatimElement[].class);
		
		
		return Arrays.asList(places);
	}

}
