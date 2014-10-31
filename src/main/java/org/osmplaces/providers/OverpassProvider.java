package org.osmplaces.providers;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.osmplaces.helpers.HttpHelper;
import org.osmplaces.model.OverpassElement;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class OverpassProvider {
	
		
	private String server;
	private HttpHelper hh;
	
	
	public OverpassProvider(String server, HttpHelper hh) {
		super();
		this.server = server;
		this.hh = hh;
	}
	
	
	/*
	 * input 
	 * { ["node", 123], ["way", 123]}
	 */
	public List<OverpassElement> getFromCoordsList(Set<String[]> coords){
		
		StringBuffer script = new StringBuffer();
		for(String[] coord: coords){
			script.append(coord[0]+"("+coord[1]+");");
		}
		String overpassScript = "(" + script.toString() + ");out;";
		
		return this.doQuery(overpassScript);
	}
	
	
	public List<OverpassElement> search(String script){
		return this.doQuery(script);
	}
	
	private List<OverpassElement> doQuery(String overpassQLScript){
		System.out.println("Executing script:" + overpassQLScript);
		String data = hh.encodeString("[out:json];" + overpassQLScript);
		
		String url = this.server + "?data=" + data;
		
		String response = hh.doGET(url);
		
		return this.jsonToPlace(response);		
	}
	
	
	private List<OverpassElement> jsonToPlace(String jstring){
		
		Gson gson = new Gson();
		
		JsonParser parser = new JsonParser();
	    JsonObject jobj = parser.parse(jstring).getAsJsonObject();
	    OverpassElement[] places = gson.fromJson(jobj.get("elements"), OverpassElement[].class);
		
		
		return Arrays.asList(places);
	}

	
}
