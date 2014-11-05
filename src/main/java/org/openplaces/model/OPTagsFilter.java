package org.openplaces.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ggiammat on 11/5/14.
 */
public class OPTagsFilter {

    private Map<String, TagFilterOperation> operations = new HashMap<String, TagFilterOperation>();
    private Map<String, String> values = new HashMap<String, String>();

    public enum TagFilterOperation{
        IS_EQUALS_TO, MATCHES, HAS_TAG
    }

    public OPTagsFilter setTagFilter(String tagName, TagFilterOperation op, String value){

        this.operations.put(tagName, op);
        this.values.put(tagName, value);

        return this;
    }

    public String buildOverpassScript(){
        String res = "";
        for(String tagName: this.operations.keySet()){
            if(TagFilterOperation.IS_EQUALS_TO.equals(this.operations.get(tagName))){
                res += "[\"" + tagName + "\"=" + "\"" + this.values.get(tagName) + "\"]";
            }
            if(TagFilterOperation.MATCHES.equals(this.operations.get(tagName))){
                res += "[\"" + tagName + "\"~" + "\"" + buildRegex(this.values.get(tagName)) + "\"]";
            }
            if(TagFilterOperation.HAS_TAG.equals(this.operations.get(tagName))){
                res += "[\"" + tagName + "\"]";
            }

        }
        return res;
    }


    //TODO: search for upper and lower case for first letters.
    //      E.g. Gioa mia and gioia mia -> "[gG]ioia [mM]ia"

    private String buildRegex(String name) {
        String res = "";
        String[] words = name.trim().split("\\s+");
        for (int i = 0; i < words.length; i++) {
            Character firstLetter = words[i].charAt(0);
            res = res + "["+firstLetter.toUpperCase(firstLetter)+firstLetter.toLowerCase(firstLetter)+"]"+words[i].substring(1)+" ";

        }
        System.out.println("Returning " + res);
        return res.trim();
    }
}
