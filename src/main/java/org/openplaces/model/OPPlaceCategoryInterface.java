package org.openplaces.model;

import java.util.List;

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
}
