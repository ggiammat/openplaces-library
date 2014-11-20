package org.openplaces.model.impl;

import org.openplaces.model.OPPlaceCategoryInterface;
import org.openplaces.model.OPPlaceInterface;
import org.openplaces.model.OSMTagFilterGroup;

import java.util.List;
import java.util.Map;

/**
 * Created by ggiammat on 11/11/14.
 */
public class OPPlaceCategoryImpl implements OPPlaceCategoryInterface {

    private String name;
    private String type;
    private List<OSMTagFilterGroup> osmTagFilterGroups;

    private Map<String, String> localization;
    private String symbol;




    public String getFirstNameMatch(String text){
        if(this.getName().toLowerCase().contains(text)){
            return this.getName();
        }
        for(String n: this.getLocalizedNames().values()){
            if(n.toLowerCase().contains(text)){
                return n;
            }
        }

        return null;
    }


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return this.getName() + " (" + this.getType() + "), tags: " + this.osmTagFilterGroups + ", names: " + this.localization.values();
    }

    public void setOsmTagFilterGroups(List<OSMTagFilterGroup> osmTagFilterGroups) {
        this.osmTagFilterGroups = osmTagFilterGroups;
    }

    public List<OSMTagFilterGroup> getOsmTagFilterGroups(){
        return this.osmTagFilterGroups;
    }

    public void setLocalization(Map<String, String> localization) {
        this.localization = localization;
    }

    public Map<String, String> getLocalizedNames(){
        return this.localization;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    /**
     * return the number of filters of the first matching tag filter group.
     * -1 if no match is found
     * @param place
     * @return
     */
    @Override
    public int placeMatchesCategory(OPPlaceInterface place) {
        Map<String, String> tags = place.getOsmTags();

        //filter groups are in OR
        for(OSMTagFilterGroup fg: this.osmTagFilterGroups){
            if(fg.tagsMatchesFilters(tags)){
                return fg.getOperations().size();
            }
        }

        return -1;
    }

}
