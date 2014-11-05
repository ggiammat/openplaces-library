package org.openplaces.providers;

import org.openplaces.helpers.HttpHelper;
import org.openplaces.model.ReviewServerElement;

import com.google.gson.Gson;

public class ReviewServerProvider {
	
	
	private String server;
	private HttpHelper hh;
	
	
	
	public ReviewServerProvider(String server, HttpHelper hh) {
		super();
		this.server = server;
		this.hh = hh;
	}



	public ReviewServerElement getPlace(String placeId){
	
		String url = this.server + "/places/" + placeId;
		String res = hh.doGET(url);	
				
		return this.jsonTObject(res);
	}
	
	
	private ReviewServerElement jsonTObject(String jstring){
		
		Gson gson = new Gson();
		
		return gson.fromJson(jstring, ReviewServerElement.class);
		
	}
	
}
