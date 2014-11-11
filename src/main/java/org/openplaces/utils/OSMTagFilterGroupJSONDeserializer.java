package org.openplaces.utils;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import org.openplaces.model.OSMTagFilterGroup;

import java.lang.reflect.Type;
import java.util.Map;
import java.util.Set;

/**
 * Created by ggiammat on 11/11/14.
 */
public class OSMTagFilterGroupJSONDeserializer implements JsonDeserializer<OSMTagFilterGroup> {
    public OSMTagFilterGroup deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {

        OSMTagFilterGroup obj = new OSMTagFilterGroup();
        Set<Map.Entry<String, JsonElement>> entries = json.getAsJsonObject().entrySet();
        for(Map.Entry entry: entries){
            String tagName = entry.getKey().toString();
            JsonElement predicate = (JsonElement) entry.getValue();
            Set<Map.Entry<String, JsonElement>> predicateEntries = predicate.getAsJsonObject().entrySet();
            //only one is allowed
            if(predicateEntries.size() != 1){
                throw new JsonParseException("Only one predicate is allowed for a tag name. " + predicateEntries.size() + " found.");
            }
            String predicateName = predicateEntries.iterator().next().getKey();
            String predicateValue = predicateEntries.iterator().next().getValue().toString();
            predicateValue = predicateValue.replaceAll("\"", "");

            obj.setTagFilter(tagName, OSMTagFilterGroup.OSMTagFilterPredicate.valueOf(predicateName), predicateValue);
        }
        return obj;
    }
}