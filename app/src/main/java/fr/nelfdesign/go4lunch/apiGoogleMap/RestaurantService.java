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

    @GET("nearbysearch/json?")
    Call<List<RestaurantsResult>> getNearByRestaurant(@Query("location") String location,
                                                      @Query("radius") int radius,
                                                      @Query("type") String type,
                                                      @Query("keyword") String keyword,
                                                      @Query("key") String key);

    @GET("nearbysearch/json?")
    Call<RestaurantsResult> getNearByRestaurant(@QueryMap Map<String, String> parameters);

    @GET("details/json?")
    Call<List<RestaurantsResult>> getRestaurantDetail(@Query("key") String apiKey,
                                               @Query("placeid") String restaurantId,
                                               @Query("fields") String fields);
}
