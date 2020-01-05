package fr.nelfdesign.go4lunch.utils;

import android.view.View;
import android.widget.ImageView;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

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

    /**
     * Create an arraylist of restaurant with the json result
     * @param restaurantsResult json result
     * @return arrayList of restaurant
     */
    public static ArrayList<Restaurant> mapRestaurantResultToRestaurant(RestaurantsResult restaurantsResult){

        ArrayList<Restaurant> resto = new ArrayList<>();

        for (int i = 0; i < restaurantsResult.getResults().size(); i++){
            Result restaurantFirst = restaurantsResult.getResults().get(i);

            Boolean openNow;
            if (restaurantFirst.getOpeningHours() == null){
                openNow = false;
            }else {
                openNow = restaurantFirst.getOpeningHours().getOpenNow();
            }

            String photo;
            if (restaurantFirst.getPhotos() == null){
                photo = "";
            }else {
                photo = restaurantFirst.getPhotos().get(0).getPhotoReference();
            }
            Restaurant r = new Restaurant(
                                restaurantFirst.getGeometry().getLocation(),
                                restaurantFirst.getName(),
                                restaurantFirst.getVicinity(),
                                restaurantFirst.getPlaceId(),
                                openNow,
                                photo,
                                null,
                                2,
                                restaurantFirst.getRating()
                             );
           resto.add(r);
        }
        return resto;
    }

    public static int starsAccordingToRating(double rating){
        if (rating == 0){
            return 0;
        }else if ( rating > 0 && rating <= 2){
            return 1;
        }else if (rating > 2 && rating < 3.7){
            return 2;
        }else {
            return 3;
        }
    }

    public static void starsView(int rating, ImageView s1, ImageView s2, ImageView s3){
        switch (rating){
            case 0 :
                s1.setVisibility(View.GONE);
                s2.setVisibility(View.GONE);
                s3.setVisibility(View.GONE);
                break;
            case 1 :
                s1.setVisibility(View.VISIBLE);
                s2.setVisibility(View.GONE);
                s3.setVisibility(View.GONE);
                break;
            case 2:
                s1.setVisibility(View.VISIBLE);
                s2.setVisibility(View.VISIBLE);
                s3.setVisibility(View.GONE);
                break;
            case 3:
                s1.setVisibility(View.VISIBLE);
                s2.setVisibility(View.VISIBLE);
                s3.setVisibility(View.VISIBLE);
                break;
        }
    }
}
