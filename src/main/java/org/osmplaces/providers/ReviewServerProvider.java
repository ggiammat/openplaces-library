package org.osmplaces.providers;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.osmplaces.helpers.HttpHelper;
import org.osmplaces.model.NominationElement;
import org.osmplaces.model.OverpassElement;
import org.osmplaces.model.ReviewServerElement;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

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
