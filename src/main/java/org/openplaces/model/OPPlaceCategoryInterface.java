package org.openplaces.model;

import java.util.List;
import java.util.Map;

/**
 * Created by ggiammat on 11/12/14.
 */
public interface OPPlaceCategoryInterface {

    public String getType();

    public void setType(String category);

    public String getName();

    public void setName(String name);

    public void setOsmTagFilterGroups(List<OSMTagFilterGroup> osmTagFilterGroups);

    public List<OSMTagFilterGroup> getOsmTagFilterGroups();

    public Map<String, String> getLocalizedNames();

    public void setLocalization(Map<String, String> localization);

    public String getFirstNameMatch(String text);

    public String getSymbol();

    public void setSymbol(String symbol);

    public int placeMatchesCategory(OPPlaceInterface place);

}
