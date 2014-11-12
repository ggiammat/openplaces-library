package org.openplaces.model;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.GsonBuilder;

import org.openplaces.utils.OPPlaceCategoryInstanceDeserializer;
import org.openplaces.utils.OSMTagFilterGroupJSONDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileReader;
import java.util.Date;
import java.util.List;

/**
 * Created by ggiammat on 11/11/14.
 */
public class OPPlaceCategoriesLibrary {

    private String libraryName;
    private String libraryVersion;
    private List<OPPlaceCategoryInterface> categories;
    private Date releasedOn;

    static Logger logger = LoggerFactory.getLogger(OPPlaceCategoriesLibrary.class);


    public static OPPlaceCategoriesLibrary loadFromFile(String jsonConfigFile){
        logger.debug("Loading library from file: " + jsonConfigFile);


        try {
            FileReader fileReader = new FileReader(jsonConfigFile);

            GsonBuilder gsonBuilder = new GsonBuilder();
            gsonBuilder.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_DASHES);
            gsonBuilder.registerTypeAdapter(OSMTagFilterGroup.class, new OSMTagFilterGroupJSONDeserializer());
            gsonBuilder.registerTypeAdapter(OPPlaceCategoryInterface.class, new OPPlaceCategoryInstanceDeserializer());
            gsonBuilder.setDateFormat("yyyy-MM-dd");
            return gsonBuilder.create().fromJson(fileReader, OPPlaceCategoriesLibrary.class);
        }
        catch (Exception ex){
            ex.printStackTrace();
        }

        return null;
    }

    public String getLibraryName() {
        return libraryName;
    }

    public void setLibraryName(String libraryName) {
        this.libraryName = libraryName;
    }

    public String getLibraryVersion() {
        return libraryVersion;
    }

    public void setLibraryVersion(String libraryVersion) {
        this.libraryVersion = libraryVersion;
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("=== Place Types Library===\n");
        sb.append("Name: " + this.getLibraryName() + ", Version: " + this.getLibraryVersion()+" (released on: "+this.releasedOn+")\n");
        sb.append("Types:\n");
        for(OPPlaceCategoryInterface type: this.getCategories()){
            sb.append("* " + type.toString() + "\n");
        }
        sb.append("==========================\n");
        return sb.toString();
    }

    public List<OPPlaceCategoryInterface> getCategories() {
        return categories;
    }

    public void setCategories(List<OPPlaceCategoryInterface> categories) {
        this.categories = categories;
    }

    public Date getReleasedOn() {
        return releasedOn;
    }

    public void setReleasedOn(Date releasedOn) {
        this.releasedOn = releasedOn;
    }
}
