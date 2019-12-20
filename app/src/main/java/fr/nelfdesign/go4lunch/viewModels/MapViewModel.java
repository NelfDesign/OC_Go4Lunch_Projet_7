package fr.nelfdesign.go4lunch.viewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import fr.nelfdesign.go4lunch.apiGoogleMap.RepositoryRestaurantList;
import fr.nelfdesign.go4lunch.models.Restaurant;

/**
 * Created by Nelfdesign at 16/12/2019
 * fr.nelfdesign.go4lunch.viewModels
 */
public class MapViewModel extends ViewModel {

    private RepositoryRestaurantList mRepositoryRestaurantList;

    /*public LiveData<List<Restaurant>> getAllRestaurants(){
        return this.mRepositoryRestaurantList.configureRetrofitCall();
    }*/
}
