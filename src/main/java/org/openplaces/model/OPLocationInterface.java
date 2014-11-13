package org.openplaces.model;

/**
 * Created by ggiammat on 11/12/14.
 */
public interface OPLocationInterface {

    public String getType();

    public void setType(String type);

    public String getDisplayName();

    public void setDisplayName(String displayName);

    public long getId();

    public void setId(long id);

    public OPBoundingBox getBoundingBox();

    public void setBoundingBox(OPBoundingBox boundingBox);

    public OPGeoPoint getPosition();

    public void setPosition(OPGeoPoint position);
}