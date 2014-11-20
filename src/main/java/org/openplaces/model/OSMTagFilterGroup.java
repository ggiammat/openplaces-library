package org.openplaces.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ggiammat on 11/5/14.
 */
public class OSMTagFilterGroup {

    Logger logger = LoggerFactory.getLogger(OSMTagFilterGroup.class);


    private Map<String, OSMTagFilterPredicate> operations;
    private Map<String, String> values;

    public void setOperations(Map<String, OSMTagFilterPredicate> operations) {
        this.operations = operations;
    }

    public Map<String, OSMTagFilterPredicate> getOperations(){
        return this.operations;
    }

    public void setValues(Map<String, String> values) {
        this.values = values;
    }

    public Map<String, String> getValues(){
        return this.values;
    }

    public OSMTagFilterGroup(){
        this.operations = new HashMap<String, OSMTagFilterPredicate>();
        this.values = new HashMap<String, String>();
    }

    public enum OSMTagFilterPredicate {
        IS_EQUALS_TO, MATCHES, HAS_TAG, HAS_NOT_TAG
    }

    public OSMTagFilterGroup setTagFilter(String tagName, OSMTagFilterPredicate op, String value){

        this.operations.put(tagName, op);
        this.values.put(tagName, value);

        return this;
    }

    public boolean tagsMatchesFilters(Map<String, String> tags){
        if(tags == null){
            logger.debug("tagset is null. Cannot perform the match. Returning false");
            return false;
        }
        for(String tagName: this.operations.keySet()){
            if(OSMTagFilterPredicate.IS_EQUALS_TO.equals(this.operations.get(tagName))){
                if(!tags.containsKey(tagName)){
                    return false;
                }
                if(!tags.get(tagName).equals(this.values.get(tagName))){
                    return false;
                }
            }
            if(OSMTagFilterPredicate.MATCHES.equals(this.operations.get(tagName))){
                if(!tags.containsKey(tagName)){
                    return false;
                }
                if(!tags.get(tagName).matches(buildRegex(this.values.get(tagName)))){
                    return false;
                }
            }
            if(OSMTagFilterPredicate.HAS_TAG.equals(this.operations.get(tagName))){
                if(!tags.containsKey(tagName)){
                    return false;
                }            }
            if(OSMTagFilterPredicate.HAS_NOT_TAG.equals(this.operations.get(tagName))){
                if(tags.containsKey(tagName)){
                    return false;
                }
            }
        }

        return true;
    }

    public String buildOverpassScript(){
        String res = "";
        for(String tagName: this.operations.keySet()){
            if(OSMTagFilterPredicate.IS_EQUALS_TO.equals(this.operations.get(tagName))){
                res += "[\"" + tagName + "\"=" + "\"" + this.values.get(tagName) + "\"]";
            }
            if(OSMTagFilterPredicate.MATCHES.equals(this.operations.get(tagName))){
                res += "[\"" + tagName + "\"~" + "\"" + buildRegex(this.values.get(tagName)) + "\"]";
            }
            if(OSMTagFilterPredicate.HAS_TAG.equals(this.operations.get(tagName))){
                res += "[\"" + tagName + "\"]";
            }
            if(OSMTagFilterPredicate.HAS_NOT_TAG.equals(this.operations.get(tagName))){
                res += "[~\"" + tagName + "\"]";
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
        res = ".*"+res.trim()+".*";
        logger.debug("Building rexeg: " + res + " (original text: " + name + ")");
        return res;
    }

    @Override
    public String toString() {
        return this.buildOverpassScript();
    }
}
