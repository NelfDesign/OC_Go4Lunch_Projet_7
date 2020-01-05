package fr.nelfdesign.go4lunch.apiGoogleMap;

import java.util.Map;

import fr.nelfdesign.go4lunch.pojos.Detail;
import fr.nelfdesign.go4lunch.pojos.RestaurantsResult;
import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

/**
 * Created by Nelfdesign at 08/12/2019
 * fr.nelfdesign.go4lunch.apiBase
 */

public interface RestaurantService {

    @GET("nearbysearch/json?radius=1000&type=restaurant")
    Observable<RestaurantsResult> getNearByRestaurant(@QueryMap Map<String, String> parameters);

    @GET("details/json?fields=name,rating,formatted_address,formatted_phone_number,photos,website")
    Observable<Detail> getDetailRestaurant(@QueryMap Map<String, String> parameters);
}
