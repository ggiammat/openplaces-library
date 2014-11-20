package org.openplaces.model;

import java.util.Map;

/**
 * Created by ggiammat on 11/12/14.
 */
public interface OPPlaceInterface {

    public String getName();

    public void setName(String name);

    public String getOsmType();

    public void setOsmType(String osmType);

    public Double getAverageRating();

    public void setAverageRating(Double averageRating);

    public Integer getNumReviews();

    public void setNumReviews(Integer numReviews);

    public long getId();

    public void setId(long id);


    public OPGeoPoint getPosition();

    public void setPosition(OPGeoPoint position);

    public String getAddressString();

    public void setAddressString(String addressString);

    public Map<String, String> getAddressTokens();

    public void setAddressTokens(Map<String, String> addressTokens);

    public String getType();

    public void setType(String type);

    public Map<String, String> getOsmTags();

    public void setOsmTags(Map<String, String> osmTags);
}
