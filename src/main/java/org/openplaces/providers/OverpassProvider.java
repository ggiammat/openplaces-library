package org.openplaces.providers;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.openplaces.helpers.HttpHelper;
import org.openplaces.model.OverpassElement;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class OverpassProvider {
	
		
	private String server;
	private HttpHelper hh;
	
	public int overpassTimeout;
	
	
	public OverpassProvider(String server, HttpHelper hh) {
		super();
		this.server = server;
		this.hh = hh;
		this.overpassTimeout = 25;
	}
	
	
	
	
	
	
	public int getOverpassTimeout() {
		return overpassTimeout;
	}






	public void setOverpassTimeout(int overpassTimeout) {
		this.overpassTimeout = overpassTimeout;
	}




	/**
	 * Very simple implementation. gets only nodes with a "place" tag
	 * @param lat
	 * @param lon
	 * @param radius
	 * @return
	 */
	public List<OverpassElement> getAroundLocations(double lat, double lon, int radius){
		String script = "node(around:"+radius+","+lat+","+lon+")[\"place\"];out;";
		return this.doQuery(script);
	}
	
	
	public List<OverpassElement> getPlaces(double lat, double lon, int radius, String amenity, String name){
		
		String nameMatching = "";
		if(name != null){
			nameMatching = "~\""+this.buildRegex(name)+"\"";
		}
		
		String amenityMatching = "";
		if(amenity != null){
			amenityMatching = "=\""+amenity+"\"";
		}
		
		String script = "node(around:"+radius+","+lat+","+lon+")[\"amenity\""+amenityMatching+"][\"name\""+nameMatching+"];out;";
		return this.doQuery(script);
	}

	
	//TODO: search for upper and lower case for first letters.
	//      E.g. Gioa mia and gioia mia -> "[gG]ioia [mM]ia"

	private String buildRegex(String name) {
		String res = "";
		String[] words = name.trim().split("\\s+");
		for (int i = 0; i < words.length; i++) {
			Character firstLetter = words[i].charAt(0);
			res = res + "["+firstLetter.toUpperCase(firstLetter)+firstLetter.toLowerCase(firstLetter)+"]"+words[i].substring(1)+" ";
			
		}
		System.out.println("Returning " + res);
		return res.trim();
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
		String data = hh.encodeString("[out:json][timeout:"+this.overpassTimeout+"];" + overpassQLScript);
		
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
