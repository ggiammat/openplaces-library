package org.openplaces.utils;

import com.google.gson.Gson;
import com.google.gson.InstanceCreator;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import org.openplaces.model.OPPlaceCategory;
import org.openplaces.model.OPPlaceCategoryInterface;
import org.openplaces.model.OPPlaceInterface;
import org.openplaces.model.impl.OPPlaceCategoryImpl;
import org.openplaces.model.impl.OPPlaceImpl;

import java.lang.reflect.Type;

/**
 * Created by ggiammat on 11/12/14.
 */
public class OPPlaceCategoryInstanceDeserializer implements JsonDeserializer<OPPlaceCategoryInterface> {

    @Override
    public OPPlaceCategoryInterface deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        return jsonDeserializationContext.deserialize(jsonElement, OPPlaceCategoryImpl.class);
    }
}
