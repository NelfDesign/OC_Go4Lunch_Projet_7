package fr.nelfdesign.go4lunch.utils;

import android.view.View;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

import fr.nelfdesign.go4lunch.models.Restaurant;
import fr.nelfdesign.go4lunch.pojos.RestaurantsResult;
import fr.nelfdesign.go4lunch.pojos.Result;
import timber.log.Timber;

/**
 * Created by Nelfdesign at 01/12/2019
 * fr.nelfdesign.go4lunch.utils
 */
public abstract class Utils {

    //Show Snack Bar with a message
    public static void showSnackBar(View view, String message){
        Snackbar.make(view, message, Snackbar.LENGTH_SHORT).show();
    }

    /**
     * Create an arraylist of restaurant with the json result
     * @param restaurantsResult json result
     * @return arrayList of restaurant
     */
    public static ArrayList<Restaurant> mapRestaurantResultToRestaurant(RestaurantsResult restaurantsResult){

        ArrayList<Restaurant> resto = new ArrayList<>();

        for (int i = 0; i < restaurantsResult.getResults().size(); i++){
            Result restaurantFirst = restaurantsResult.getResults().get(i);

            Restaurant r = new Restaurant(
                                restaurantFirst.getGeometry().getLocation(),
                                restaurantFirst.getName(),
                                restaurantFirst.getVicinity(),
                                restaurantFirst.getIcon(),
                                null,
                                restaurantFirst.getPhotos().get(0).getPhotoReference(),
                                null,
                                2,
                                0
                             );
            Timber.i("Restaurant 1 : %s", r.getLocation().getLat() + " "+ r.getAddress());
           resto.add(r);

        }
        return resto;
    }

}
