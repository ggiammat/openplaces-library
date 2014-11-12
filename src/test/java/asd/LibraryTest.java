package asd;

import org.openplaces.model.OPPlaceCategoriesLibrary;

/**
 * Created by ggiammat on 11/11/14.
 */
public class LibraryTest {

    public static void main(String[] args){

        OPPlaceCategoriesLibrary r = OPPlaceCategoriesLibrary.loadFromFile("/home/ggiammat/projects/P.OSMPlaces/workspace/openplaces-library/src/test/resources/default-categories-library.json");
        System.out.println(r);
    }
}
