package fr.nelfdesign.go4lunch.apiGoogleMap;

import java.util.List;
import java.util.Map;

import fr.nelfdesign.go4lunch.pojos.RestaurantsResult;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

/**
 * Created by Nelfdesign at 08/12/2019
 * fr.nelfdesign.go4lunch.apiBase
 */

public interface RestaurantService {

    @GET("nearbysearch/json?radius=1500&type=restaurant")
    Call<RestaurantsResult> getNearByRestaurant(@Query("location") String location,
                                                @Query("key") String key);

    @GET("nearbysearch/json?radius=1500")
    Call<RestaurantsResult> getNearByRestaurant(@QueryMap Map<String, String> parameters);

    @GET("details/json?")
    Call<RestaurantsResult> getRestaurantDetail(@Query("key") String apiKey,
                                               @Query("placeid") String restaurantId,
                                               @Query("fields") String fields);
}
