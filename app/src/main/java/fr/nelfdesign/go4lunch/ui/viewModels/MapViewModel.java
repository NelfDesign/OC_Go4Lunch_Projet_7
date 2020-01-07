package fr.nelfdesign.go4lunch.ui.viewModels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

import fr.nelfdesign.go4lunch.apiGoogleMap.RepositoryRestaurantList;
import fr.nelfdesign.go4lunch.models.DetailRestaurant;
import fr.nelfdesign.go4lunch.models.Poi;
import fr.nelfdesign.go4lunch.models.Restaurant;

/**
 * Created by Nelfdesign at 16/12/2019
 * fr.nelfdesign.go4lunch.ui.viewModels
 */
public class MapViewModel extends ViewModel {

    private RepositoryRestaurantList mRepositoryRestaurantList = new RepositoryRestaurantList();

    public Poi generateUserPoi(double lat, double lng){
        return new Poi(
                "My position",
                "",
                lat,
                lng
        );
    }


    public MutableLiveData<ArrayList<Restaurant>> getAllRestaurants(LatLng latLng){
        return this.mRepositoryRestaurantList.configureRetrofitCall(latLng);
    }

    public MutableLiveData<DetailRestaurant> getDetailRestaurant(String placeId){
        return this.mRepositoryRestaurantList.configureDetailRestaurant(placeId);
    }
}
