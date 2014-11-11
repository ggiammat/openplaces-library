package asd;

import org.openplaces.types.OPPlaceTypesLibrary;

/**
 * Created by ggiammat on 11/11/14.
 */
public class LibraryTest {

    public static void main(String[] args){

        OPPlaceTypesLibrary r = OPPlaceTypesLibrary.loadFromFile("/home/ggiammat/projects/P.OSMPlaces/workspace/openplaces-library/src/test/resources/default-types-library.json");
        System.out.println(r);
    }
}
