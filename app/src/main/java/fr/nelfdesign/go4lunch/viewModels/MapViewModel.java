package fr.nelfdesign.go4lunch.viewModels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

import fr.nelfdesign.go4lunch.apiGoogleMap.RepositoryRestaurantList;
import fr.nelfdesign.go4lunch.models.Restaurant;

/**
 * Created by Nelfdesign at 16/12/2019
 * fr.nelfdesign.go4lunch.viewModels
 */
public class MapViewModel extends ViewModel {

    private RepositoryRestaurantList mRepositoryRestaurantList = new RepositoryRestaurantList();

    public MutableLiveData<ArrayList<Restaurant>> getAllRestaurants(){
        return this.mRepositoryRestaurantList.configureRetrofitCall();
    }
}
