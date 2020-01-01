package fr.nelfdesign.go4lunch.viewModels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

import fr.nelfdesign.go4lunch.apiGoogleMap.RepositoryRestaurantList;
import fr.nelfdesign.go4lunch.models.DetailRestaurant;
import fr.nelfdesign.go4lunch.models.Restaurant;

/**
 * Created by Nelfdesign at 16/12/2019
 * fr.nelfdesign.go4lunch.viewModels
 */
public class MapViewModel extends ViewModel {

    private RepositoryRestaurantList mRepositoryRestaurantList = new RepositoryRestaurantList();

    public MutableLiveData<ArrayList<Restaurant>> getAllRestaurants(double lat, double lng){
        return this.mRepositoryRestaurantList.configureRetrofitCall(lat, lng);
    }

    public MutableLiveData<DetailRestaurant> getDetailRestaurant(String placeId){
        return this.mRepositoryRestaurantList.configureDetailRestaurant(placeId);
    }
}
