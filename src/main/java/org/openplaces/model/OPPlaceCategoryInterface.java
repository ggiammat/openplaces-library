package org.openplaces.model;

import java.util.List;
import java.util.Map;

/**
 * Created by ggiammat on 11/12/14.
 */
public interface OPPlaceCategoryInterface {


    public String getId();

    public void setId(String name);

    public List<OSMTagFilterGroup> getOsmTagFilterGroups();

    public void setOsmTagFilterGroups(List<OSMTagFilterGroup> osmTagFilterGroups);

    public void setNames(Map<String, String> localization);

    public Map<String, String> getNames();

    public boolean placeMatchesCategory(OPPlaceInterface place);

    public String getFirstNameMatch(String text);

    public int getPriority();

    public void setPriority(int priority);
}
