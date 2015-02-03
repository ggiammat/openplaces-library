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

    private String id;
    private List<OSMTagFilterGroup> osmTagFilterGroups;
    private Map<String, String> names;


    private int priority;


    public String getFirstNameMatch(String text){
        if(this.getId().toLowerCase().contains(text)){
            return this.getId();
        }
        for(String n: this.getNames().values()){
            if(n.toLowerCase().contains(text)){
                return n;
            }
        }

        return null;
    }

    @Override
    public String toString() {
        return this.getId() +" (p:"+ this.priority + "), tags: " + this.osmTagFilterGroups + ", names: " + this.names.values();
    }

    public void setOsmTagFilterGroups(List<OSMTagFilterGroup> osmTagFilterGroups) {
        this.osmTagFilterGroups = osmTagFilterGroups;
    }

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public void setId(String name) {
        this.id = name;
    }

    @Override
    public List<OSMTagFilterGroup> getOsmTagFilterGroups(){
        return this.osmTagFilterGroups;
    }

    @Override
    public Map<String, String> getNames() {
        return this.names;
    }

    @Override
    public void setNames(Map<String, String> localization) {
        this.names = localization;
    }


    /**
     * return the number of filters of the first matching tag filter group.
     * -1 if no match is found
     * @param place
     * @return
     */
    @Override
    public boolean placeMatchesCategory(OPPlaceInterface place) {
        Map<String, String> tags = place.getOsmTags();

        //filter groups are in OR
        for(OSMTagFilterGroup fg: this.osmTagFilterGroups){
            if(fg.tagsMatchesFilters(tags)){
                return true;
            }
        }

        return false;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }


    public int getPriority() {
        return priority;
    }
}
