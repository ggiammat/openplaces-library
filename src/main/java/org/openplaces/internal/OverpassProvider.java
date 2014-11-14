package org.openplaces.internal;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.openplaces.utils.HttpHelper;
import org.openplaces.model.OSMTagFilterGroup;
import org.openplaces.internal.model.OverpassElement;
import org.openplaces.model.OPBoundingBox;
import org.openplaces.model.OPGeoPoint;
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

    /**
     *
     * @param filterGroups
     * @param boundingBoxes
     * @param additionalFilterInAnd used to add matching on the name tag
     * @return
     */
    public Collection<OverpassElement> getPlaces(List<OSMTagFilterGroup> filterGroups, List<OPBoundingBox> boundingBoxes, OSMTagFilterGroup additionalFilterInAnd){

        String script = "(";

        List<String> bbFilters = new ArrayList<String>();
        for(OPBoundingBox bb: boundingBoxes){
            bbFilters.add("(" +
                    bb.getSouth() + "," +
                    bb.getWest() + "," +
                    bb.getNorth() + "," +
                    bb.getEast() + ")");
        }

        String additionalFilter = additionalFilterInAnd == null ? "" : additionalFilterInAnd.buildOverpassScript();
        for(OSMTagFilterGroup filterGroup: filterGroups){
            String tagsFilter = filterGroup.buildOverpassScript();
            for(String bbFilter: bbFilters){
                script +=
                        "(\n" +
                        "node"+bbFilter+tagsFilter+additionalFilter+";\n" +
                        "way"+bbFilter+tagsFilter+additionalFilter+";>;\n" +
                        ");\n";
            }

        }

        script += ");out body;\n";

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
	public List<OverpassElement> getAroundLocations(OPGeoPoint point, long radius){
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
		logger.debug("Executing script:\n" + overpassQLScript);
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
