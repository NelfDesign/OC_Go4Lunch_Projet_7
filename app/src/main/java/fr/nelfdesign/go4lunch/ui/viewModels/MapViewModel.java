package fr.nelfdesign.go4lunch.ui.viewModels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

import fr.nelfdesign.go4lunch.R;
import fr.nelfdesign.go4lunch.apiGoogleMap.RepositoryRestaurantList;
import fr.nelfdesign.go4lunch.models.DetailRestaurant;
import fr.nelfdesign.go4lunch.models.Poi;
import fr.nelfdesign.go4lunch.models.Restaurant;
import fr.nelfdesign.go4lunch.models.Workers;
import fr.nelfdesign.go4lunch.utils.Utils;

/**
 * Created by Nelfdesign at 16/12/2019
 * fr.nelfdesign.go4lunch.ui.viewModels
 */
public class MapViewModel extends ViewModel {

    //FIELD
    private RepositoryRestaurantList mRepositoryRestaurantList = new RepositoryRestaurantList();

    /**
     * generate user poi
     *
     * @param lat latitude
     * @param lng longitude
     * @return Poi
     */
    public Poi generateUserPoi(double lat, double lng) {
        return new Poi(
                String.valueOf(R.string.my_position),
                "",
                lat,
                lng
        );
    }

    /**
     * generate list Poi with restaurant list
     *
     * @param restaurants       list
     * @param mWorkersArrayList list workers
     * @return list of poi
     */
    public List<Poi> generatePois(ArrayList<Restaurant> restaurants, ArrayList<Workers> mWorkersArrayList) {
        List<Poi> pois = new ArrayList<>();
        List<Restaurant> restaurants1 = Utils.getChoicedRestaurants(restaurants, mWorkersArrayList);

        for (Restaurant resto : restaurants1) {
            Poi p = new Poi(
                    resto.getName(),
                    resto.getPlaceId(),
                    resto.getLocation().getLat(),
                    resto.getLocation().getLng()
            );

            if (resto.isChoice()) {
                p.setChoosen(true);
            }

            pois.add(p);
        }
        return pois;
    }

    /**
     * retrofit call to get all restaurants
     *
     * @param latLng latlng
     * @param radius for nearbysearch
     * @param type   of search
     * @return MutableLiveData
     */
    public MutableLiveData<ArrayList<Restaurant>> getAllRestaurants(LatLng latLng, String radius, String type) {
        return this.mRepositoryRestaurantList.configureRetrofitCall(latLng, radius, type);
    }

    /**
     * retrofit call to get detail restaurant
     *
     * @param placeId placeId
     * @return MutableLiveData
     */
    public MutableLiveData<DetailRestaurant> getDetailRestaurant(String placeId) {
        return this.mRepositoryRestaurantList.configureDetailRestaurant(placeId);
    }
}
