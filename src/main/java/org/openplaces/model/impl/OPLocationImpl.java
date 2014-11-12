package org.openplaces.model.impl;

import org.openplaces.internal.model.NominatimElement;
import org.openplaces.internal.model.OverpassElement;
import org.openplaces.model.OPBoundingBox;
import org.openplaces.model.OPGeoPoint;
import org.openplaces.model.OPLocationInterface;

/**
 * Created by ggiammat on 11/5/14.
 */
public class OPLocationImpl implements OPLocationInterface {

    private long id;
    private String displayName;
    private String type;
    private OPGeoPoint position;
    private OPBoundingBox boundingBox;


    public OPLocationImpl(OverpassElement el){
        this.setId(el.getId());
        this.setDisplayName(el.getTag("name", null));
        this.setType(el.getTag("place", null));
        this.boundingBox = null;
        if(el.getLat() != null && el.getLon() != null){
            this.setPosition(new OPGeoPoint(el.getLat(), el.getLon()));
        }
    }

    public OPLocationImpl(NominatimElement el){
        this.setId(el.getOsm_id());
        this.setDisplayName(el.getDisplay_name());
        this.setType(el.getType());

        OPBoundingBox bb = new OPBoundingBox(
            Math.max(el.getBoundingbox()[0], el.getBoundingbox()[1]),
            Math.max(el.getBoundingbox()[2], el.getBoundingbox()[3]),
            Math.min(el.getBoundingbox()[0], el.getBoundingbox()[1]),
            Math.min(el.getBoundingbox()[2], el.getBoundingbox()[3])
        );
        this.setBoundingBox(bb);
        if(el.getLat() != null && el.getLon() != null) {
            this.setPosition(new OPGeoPoint(el.getLat(), el.getLon()));
        }
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public OPBoundingBox getBoundingBox() {
        return boundingBox;
    }

    public void setBoundingBox(OPBoundingBox boundingBox) {
        this.boundingBox = boundingBox;
    }

    @Override
    public String toString() {
        return this.getType() + " " + this.getDisplayName() + " at " + this.getPosition();
    }

    public OPGeoPoint getPosition() {
        return position;
    }

    public void setPosition(OPGeoPoint position) {
        this.position = position;
    }
}
