package org.openplaces.providers;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.openplaces.helpers.HttpHelper;
import org.openplaces.model.OPTagsFilter;
import org.openplaces.model.OverpassElement;
import org.openplaces.utils.OPBoundingBox;
import org.openplaces.utils.OPGeoPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class OverpassProvider {

    Logger logger = LoggerFactory.getLogger(OverpassProvider.class);

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


    public Collection<OverpassElement> getPlaces(OPTagsFilter filters, OPBoundingBox boundingBox){

        String bbFilter = "(" +
                boundingBox.getSouth() + "," +
                boundingBox.getWest() + "," +
                boundingBox.getNorth() + "," +
                boundingBox.getEast() + ")";

        String tagsFilter = filters.buildOverpassScript();

        String script = "(" +
                "node"+bbFilter+tagsFilter+";\n" +
                "way"+bbFilter+tagsFilter+";\n" +
                ">;\n" +
                " );\n" +
                "out body;";
        return this.doQuery(script);
    }




	public void setOverpassTimeout(int overpassTimeout) {
		this.overpassTimeout = overpassTimeout;
	}


    /**
     * at the moment consider only nodes
     * @param point
     * @param radius
     * @return
     */
	public List<OverpassElement> getAroundLocations(OPGeoPoint point, int radius){
		String script = "(" +
                "node(around:"+radius+","+point.getLat()+","+point.getLon()+")[\"place\"=\"village\"];" +
                "node(around:"+radius+","+point.getLat()+","+point.getLon()+")[\"place\"=\"hamlet\"];" +
                "node(around:"+radius+","+point.getLat()+","+point.getLon()+")[\"place\"=\"isolated_dwelling\"];" +
                "node(around:"+radius+","+point.getLat()+","+point.getLon()+")[\"place\"=\"town\"];" +
                "node(around:"+radius+","+point.getLat()+","+point.getLon()+")[\"place\"=\"suburb\"];" +
                "node(around:"+radius+","+point.getLat()+","+point.getLon()+")[\"place\"=\"neighbourhood\"];" +
                "node(around:"+radius+","+point.getLat()+","+point.getLon()+")[\"place\"=\"city\"];" +
                ");out;";
		return this.doQuery(script);
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
		logger.debug("Executing script:" + overpassQLScript);
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
