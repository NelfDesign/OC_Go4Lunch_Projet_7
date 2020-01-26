package fr.nelfdesign.go4lunch;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import fr.nelfdesign.go4lunch.models.Restaurant;
import fr.nelfdesign.go4lunch.models.Workers;
import fr.nelfdesign.go4lunch.utils.Utils;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;

/**
 * Created by Nelfdesign at 24/01/2020
 * fr.nelfdesign.go4lunch
 */
public class UtilsTest {

    private ArrayList<Restaurant> mRestaurants = generatorOfRestaurant();
    private ArrayList<Workers> mWorkers = generatorOfWorkers();

   private static List<Restaurant> mRestaurantsGenerate = Arrays.asList(
            new Restaurant(null,"le Zinc", "2 boulevard poissonnière",
                    "000", false,null,0,0, 2.5),
            new Restaurant(null,"La cigale", "21 place Graslin",
                    "021", true,null,0,2, 4.5),
            new Restaurant(null,"mac donald", "place graslin",
                    "00025", false,null,0,0, 1.5),
           new Restaurant(null,"Grillpizz", "place graslin",
                   "00028", false,null,0,0, 3)
    );

    private static List<Workers> mWorkersGenerate = Arrays.asList(
            new Workers("Carole", null, "La cigale", "021"),
            new Workers("Cyril", null, "", ""),
            new Workers("Fabrice", null, "le Zinc", "000"),
            new Workers("Virgile", null, "La cigale", "021")
    );

    private static ArrayList<Restaurant> generatorOfRestaurant() {
        return new ArrayList<>(mRestaurantsGenerate);
    }

    private static ArrayList<Workers> generatorOfWorkers() {
        return new ArrayList<>(mWorkersGenerate);
    }

    @Test
    public void getChoiceRestaurantWithSuccess(){
        //create list restaurant
        ArrayList<Restaurant> restaurantArrayList = Utils.getChoicedRestaurants(mRestaurants, mWorkers);
        //assert first restaurant is choice
        assertTrue(restaurantArrayList.get(0).isChoice());
        //assert first restaurant has a worker
        assertEquals(1, restaurantArrayList.get(0).getWorkers());
        //assert third restaurant haven't worker
        assertEquals(0, restaurantArrayList.get(2).getWorkers());
        //assert second restaurant has 2 workers
        assertEquals(2, restaurantArrayList.get(1).getWorkers());
    }

    @Test
    public void getFilterRestaurantWithSuccess(){
        //create list restaurant
        ArrayList<Restaurant> restaurantArrayList = Utils.getChoicedRestaurants(mRestaurants, mWorkers);
        //assert there is 2 restaurants with 3 stars
        assertEquals(2, Utils.filterRestaurantList(restaurantArrayList, 2).size());
        //assert there is no restaurant with 0 stars
        assertEquals(0, Utils.filterRestaurantList(restaurantArrayList, 0).size());
    }

}
