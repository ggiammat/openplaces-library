package org.openplaces.model;

public class SearchSuggestionElement {
	
	public enum SuggestionType {
		HISTORY, LOCATION, PLACE
	}
	
	private String name;
	private String osmAmenity;
	private SuggestionType suggestionType;
	private long osmId;
	
	

	public SearchSuggestionElement(SuggestionType suggestionType, OverpassElement el){
		this.suggestionType = suggestionType;
		this.name = el.getTag("name");
		this.osmAmenity = el.getTag("amenity");
		this.osmId = el.getId();
	}
	
	
	

	public long getOsmId() {
		return osmId;
	}




	public void setOsmId(long osmId) {
		this.osmId = osmId;
	}




	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getOsmAmenity() {
		return osmAmenity;
	}

	public void setOsmAmenity(String osmAmenity) {
		this.osmAmenity = osmAmenity;
	}

	public SuggestionType getSuggestionType() {
		return suggestionType;
	}

	public void setSuggestionType(SuggestionType suggestionType) {
		this.suggestionType = suggestionType;
	}
	
	
	@Override
	public String toString() {
		return this.suggestionType + "> " + this.name + " (" + this.osmAmenity + ")";
	}
	
}
