package org.openplaces;

import org.openplaces.model.OPLocationInterface;
import org.openplaces.model.OPPlaceInterface;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ggiammat on 1/28/15.
 */
public class OPProviderResultObject {

    public static final int NETWORK_ERROR = 1;
    public static final int RADIUS_ERROR = 2;


    public int errorCode = 0;
    public String errorMessage = "";
    public List<OPLocationInterface> locations = new ArrayList<OPLocationInterface>();
    public List<OPPlaceInterface> places = new ArrayList<OPPlaceInterface>();


    public static OPProviderResultObject getNetworkErrorRO(String message){
        OPProviderResultObject rs = new OPProviderResultObject();
        rs.errorCode = NETWORK_ERROR;
        rs.errorMessage = message;
        return rs;
    }

}
