package fr.nelfdesign.go4lunch.apiGoogleMap;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import fr.nelfdesign.go4lunch.base.App;
import fr.nelfdesign.go4lunch.pojos.Detail;
import fr.nelfdesign.go4lunch.pojos.RestaurantsResult;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.http.QueryMap;

/**
 * Created by Nelfdesign at 05/01/2020
 * fr.nelfdesign.go4lunch.apiGoogleMap
 */
class PlaceStream {

    /**
     * create observable data for nearby restaurant
     * @param parameters for search restaurants
     * @return Restaurant Result of the request
     */
    static Observable<RestaurantsResult> streamGetNearByRestaurant(@QueryMap Map<String, String> parameters){
        RestaurantService restaurantService = App.retrofitCall();
        return restaurantService.getNearByRestaurant(parameters)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .timeout(10, TimeUnit.SECONDS);
    }

    /**
     * create observable data for restaurant detail
     * @param parameters for restaurant search
     * @return Detail of the restaurant chosen
     */
    static Observable<Detail> streamGetDetailRestaurant(@QueryMap Map<String, String> parameters){
        RestaurantService restaurantService = App.retrofitCall();
        return restaurantService.getDetailRestaurant(parameters)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .timeout(10, TimeUnit.SECONDS);
    }
}
