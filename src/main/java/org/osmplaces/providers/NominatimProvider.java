package org.osmplaces.providers;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.osmplaces.helpers.HttpHelper;
import org.osmplaces.model.NominationElement;
import org.osmplaces.model.OSMPlace;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


public class NominatimProvider {
	
	
	
	
	private String server;
	private HttpHelper hh;
	private String userEmail;
	
	public NominatimProvider(String server, String userEmail, HttpHelper hh){
		this.server = server;
		this.hh = hh;
		this.userEmail = userEmail;
	}
	
	
	
	public List<NominationElement> searchInside(
			double vbLeft, double vbTop, double vbRight, double vbBottom, String query){
		Map<String, String> queryStringParams = new HashMap<String, String>();
		queryStringParams.put("q", hh.encodeString(query));
		queryStringParams.put("viewbox", vbLeft+","+vbTop+","+vbRight+","+vbBottom);
		queryStringParams.put("bounded", "1");
		return this.doSearch(queryStringParams);
	}
	
	
	
	public List<NominationElement> search(String query){
		Map<String, String> queryStringParams = new HashMap<String, String>();
		queryStringParams.put("q", hh.encodeString(query));
		return this.doSearch(queryStringParams);
	}
	
	
	
	private List<NominationElement> doSearch(Map<String, String> queryStringParams){
		queryStringParams.put("format", "json");
		queryStringParams.put("email", this.userEmail);
		queryStringParams.put("addressdetails", "1");
		
		String url = this.server + "search?" + this.buildQueryString(queryStringParams);
		
		String response = hh.doGET(url);
		
		return this.jsonToPlace(response);		
	}
	
	
	private String buildQueryString(Map<String, String> queryStringParams){
		StringBuffer qs = new StringBuffer();
		
		for(String k: queryStringParams.keySet()){
			qs.append(k +"="+queryStringParams.get(k)+"&");
		}
		
		return qs.toString().substring(0, qs.length()-1);
	}
	
	private List<NominationElement> jsonToPlace(String jstring){
		
		Gson gson = new Gson();
		NominationElement[] places = gson.fromJson(jstring, NominationElement[].class);
		
		
		return Arrays.asList(places);
	}

}
