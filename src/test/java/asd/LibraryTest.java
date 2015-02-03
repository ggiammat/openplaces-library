package asd;

import org.openplaces.model.OPPlaceCategoriesLibrary;

/**
 * Created by ggiammat on 11/11/14.
 */
public class LibraryTest {

    public static void main(String[] args){

        OPPlaceCategoriesLibrary r = OPPlaceCategoriesLibrary.loadFromFile("/home/ggiammat/projects/P.OSMPlaces/workspace/openplaces-android/app/src/main/res/raw/food_standard_library.json");
        System.out.println(r);
    }
}
