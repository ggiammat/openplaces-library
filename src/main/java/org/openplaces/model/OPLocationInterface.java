package org.openplaces.model;

import java.util.Map;

/**
 * Created by ggiammat on 11/12/14.
 */
public interface OPLocationInterface {

    public String getType();

    public String getOsmType();
    public void setOsmType(String osmType);

    public void setOsmTags(Map<String, String> tags);

    public Map<String, String> getOsmTags();

    public void setType(String type);

    public String getDisplayName();

    public void setDisplayName(String displayName);

    public long getId();

    public void setId(long id);

    public OPBoundingBox getBoundingBox();

    public void setBoundingBox(OPBoundingBox boundingBox);

    public OPGeoPoint getPosition();

    public void setPosition(OPGeoPoint position);

    public OPPlaceInterface getAsPlace();
}
