package fr.nelfdesign.go4lunch.apiGoogleMap;

import androidx.lifecycle.LiveData;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.nelfdesign.go4lunch.BuildConfig;
import fr.nelfdesign.go4lunch.base.App;
import fr.nelfdesign.go4lunch.models.Restaurant;
import fr.nelfdesign.go4lunch.pojos.RestaurantsResult;
import fr.nelfdesign.go4lunch.utils.Utils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

/**
 * Created by Nelfdesign at 16/12/2019
 * fr.nelfdesign.go4lunch.apiGoogleMap
 */
public class RepositoryRestaurantList {

    private List<Restaurant> mRestaurantList;

   /* @Override
    public LiveData<List<Restaurant>> configureRetrofitCall() {
        mRestaurantList = new ArrayList<>();

        Map<String, String> parameters = new HashMap<>();
        parameters.put("location", "47.21,-1.55");
        parameters.put("type", "restaurant");
        parameters.put("key", BuildConfig.google_maps_key);

        Call<RestaurantsResult> mListCall;

        mListCall = App.retrofitCall().getNearByRestaurant(parameters);

        mListCall.enqueue(new Callback<RestaurantsResult>() {
            @Override
            public void onResponse(@NotNull Call<RestaurantsResult> call,
                                   @NotNull Response<RestaurantsResult> response) {

                if (!response.isSuccessful()) {
                    Timber.i("onResponse: erreur");
                    return;
                }

                RestaurantsResult resultsListRestaurants = response.body();

                if (resultsListRestaurants != null) {

                    Utils.mapRestaurantResultToRestaurant(resultsListRestaurants, mRestaurantList);

                    Timber.i("Restaurant = %s", mRestaurantList.get(0).getPhotoReference());
                }
            }

            @Override
            public void onFailure(@NotNull Call<RestaurantsResult> call, @NotNull Throwable t) {
                Timber.i(t.toString());
            }
        });
        return (LiveData<List<Restaurant>>) mRestaurantList;
    }*/
}
