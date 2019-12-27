package fr.nelfdesign.go4lunch.apiGoogleMap;

import androidx.lifecycle.MutableLiveData;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
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
public class RepositoryRestaurantList implements NearbyPlaces {

    private MutableLiveData<ArrayList<Restaurant>> mRestaurantList;

    @Override
    public MutableLiveData<ArrayList<Restaurant>> configureRetrofitCall() {

        mRestaurantList = new MutableLiveData<>();

        Map<String, String> parameters = new HashMap<>();
        parameters.put("location", "47.21,-1.55");
        parameters.put("key", BuildConfig.google_maps_key);

        Call<RestaurantsResult> mListCall = App.retrofitCall().getNearByRestaurant(parameters);

        mListCall.enqueue(new Callback<RestaurantsResult>() {
            @Override
            public void onResponse(@NotNull Call<RestaurantsResult> call,
                                   @NotNull Response<RestaurantsResult> response) {

                if (!response.isSuccessful()) {
                    Timber.e("onResponse: erreur");
                    return;
                }

                RestaurantsResult resultsListRestaurants = response.body();

                if (resultsListRestaurants != null) {

                   mRestaurantList.setValue(Utils.mapRestaurantResultToRestaurant(resultsListRestaurants));
                }
            }

            @Override
            public void onFailure(@NotNull Call<RestaurantsResult> call, @NotNull Throwable t) {
                Timber.e("erreur on failure = %s", t.toString());
            }
        });

        return this.mRestaurantList;
    }
}
