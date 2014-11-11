package org.openplaces.types;

import org.openplaces.model.OSMTagFilterGroup;

import java.util.List;
import java.util.Map;

/**
 * Created by ggiammat on 11/11/14.
 */
public class OPPlaceType {

    private String name;
    private String category;
    private List<OSMTagFilterGroup> osmTagFilterGroups;

    Map<String, String> localization;


    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return this.getName() + " (" + this.getCategory() + "), tags: " + this.osmTagFilterGroups + ", names: " + this.localization.values();
    }

    public void setOsmTagFilterGroups(List<OSMTagFilterGroup> osmTagFilterGroups) {
        this.osmTagFilterGroups = osmTagFilterGroups;
    }

    public List<OSMTagFilterGroup> getOsmTagFilterGroups(){
        return this.osmTagFilterGroups;
    }
}
