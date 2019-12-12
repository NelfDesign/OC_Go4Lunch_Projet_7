package fr.nelfdesign.go4lunch.utils;

import android.view.View;

import com.google.android.material.snackbar.Snackbar;

import java.util.List;

import fr.nelfdesign.go4lunch.models.Restaurant;
import fr.nelfdesign.go4lunch.pojos.RestaurantsResult;
import fr.nelfdesign.go4lunch.pojos.Result;

/**
 * Created by Nelfdesign at 01/12/2019
 * fr.nelfdesign.go4lunch.utils
 */
public abstract class Utils {

    //Show Snack Bar with a message
    public static void showSnackBar(View view, String message){
        Snackbar.make(view, message, Snackbar.LENGTH_SHORT).show();
    }

    public static List<Restaurant> mapRestaurantResultToRestaurant(RestaurantsResult restaurantsResult, List<Restaurant> restaurants){

        for (int i = 0; i < restaurantsResult.getResults().size(); i++){
            Result restaurantFirst = restaurantsResult.getResults().get(i);
            Restaurant r = new Restaurant(
                                restaurantFirst.getName(),
                                restaurantFirst.getVicinity(),
                                restaurantFirst.getIcon(),
                                null,
                                restaurantFirst.getPhotos().get(0).getPhotoReference(),
                                null,
                                2,
                                0
                             );
            restaurants.add(r);
        }
        return restaurants;
    }
}
