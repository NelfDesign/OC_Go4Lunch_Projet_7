package fr.nelfdesign.go4lunch.apiGoogleMap;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

import fr.nelfdesign.go4lunch.models.DetailRestaurant;
import fr.nelfdesign.go4lunch.models.Restaurant;

/**
 * Created by Nelfdesign at 10/12/2019
 * fr.nelfdesign.go4lunch.apiBase
 */
public interface NearbyPlaces {

    MutableLiveData<ArrayList<Restaurant>> configureRetrofitCall(LatLng latLng);

    LiveData<DetailRestaurant> configureDetailRestaurant(String placeId);

}
