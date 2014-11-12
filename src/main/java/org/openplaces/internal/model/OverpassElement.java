package org.openplaces.internal.model;

import java.util.Map;

public class OverpassElement {
	
	
	
	private long id;
	private Double lon;
	private Double lat;
	private Map<String, String> tags;
    private String type;
    private long[] nodes;

	public OverpassElement() {
		super();
	}
	
	
	public boolean hasTag(String tagName){
		return this.tags.containsKey(tagName);
	}
	
	public String getTag(String tagName, String def){
        if(def!=null && !this.tags.containsKey(tagName)){
            return def;
        }
		return this.tags.get(tagName);
	}


	public long getId() {
		return id;
	}




	public void setId(long id) {
		this.id = id;
	}




	public Double getLon() {
		return lon;
	}




	public void setLon(double lon) {
		this.lon = lon;
	}




	public Double getLat() {
		return lat;
	}




	public void setLat(double lat) {
		this.lat = lat;
	}




	public Map<String, String> getTags() {
		return tags;
	}




	public void setTags(Map<String, String> tags) {
		this.tags = tags;
	}




	@Override
	public String toString() {
		return this.id + "(" + this.tags + ") at " + this.lat + "," + this.lon;
	}

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public long[] getNodes() {
        return nodes;
    }

    public void setNodes(long[] nodes) {
        this.nodes = nodes;
    }
}
