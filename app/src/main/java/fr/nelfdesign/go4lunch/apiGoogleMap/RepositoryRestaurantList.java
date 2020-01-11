package fr.nelfdesign.go4lunch.apiGoogleMap;

import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import fr.nelfdesign.go4lunch.BuildConfig;
import fr.nelfdesign.go4lunch.apiFirebase.RepositoryFirebase;
import fr.nelfdesign.go4lunch.models.DetailRestaurant;
import fr.nelfdesign.go4lunch.models.Restaurant;
import fr.nelfdesign.go4lunch.models.Workers;
import fr.nelfdesign.go4lunch.pojos.Detail;
import fr.nelfdesign.go4lunch.pojos.DetailsResult;
import fr.nelfdesign.go4lunch.pojos.RestaurantsResult;
import fr.nelfdesign.go4lunch.utils.Utils;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import timber.log.Timber;

/**
 * Created by Nelfdesign at 16/12/2019
 * fr.nelfdesign.go4lunch.apiGoogleMap
 */
public class RepositoryRestaurantList implements NearbyPlaces {

    private MutableLiveData<ArrayList<Restaurant>> mRestaurantList;
    private MutableLiveData<DetailRestaurant> mDetailRestaurantLiveData;
    private Disposable disposable;

    @Override
    public MutableLiveData<ArrayList<Restaurant>> configureRetrofitCall(LatLng latLng) {

        mRestaurantList = new MutableLiveData<>();

        Map<String, String> parameters = new HashMap<>();
        parameters.put("location", latLng.latitude + ","+ latLng.longitude );
        parameters.put("key", BuildConfig.google_maps_key);

        disposable = PlaceStream.streamGetNearByRestaurant(parameters)
                .subscribeWith(new DisposableObserver<RestaurantsResult>() {
                    @Override
                    public void onNext(RestaurantsResult restaurantsResult) {

                        if (restaurantsResult != null) {
                            mRestaurantList.setValue(Utils.mapRestaurantResultToRestaurant(restaurantsResult));
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Timber.e("error : %s", e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        Timber.i("on Complete");
                    }
                });

        return this.mRestaurantList;
    }

    @Override
    public MutableLiveData<DetailRestaurant> configureDetailRestaurant(String placeId) {

        mDetailRestaurantLiveData = new MutableLiveData<>();

        Map<String, String> parameters = new HashMap<>();

        parameters.put("place_id", placeId);
        parameters.put("key", BuildConfig.google_maps_key);

        disposable = PlaceStream.streamGetDetailRestaurant(parameters)
                .subscribeWith(new DisposableObserver<Detail>() {
                    @Override
                    public void onNext(Detail detail) {
                        if (detail != null) {
                            DetailsResult detailsResult = detail.getResult();

                            String photo;
                            if (detailsResult.getPhotos() == null){
                                photo = "";
                            }else {
                                photo = detailsResult.getPhotos().get(0).getPhotoReference();
                            }

                          ArrayList<Workers>  workers = RepositoryFirebase.getQueryWorkersWithChoiceRestaurant(placeId);

                            DetailRestaurant restaurant = new DetailRestaurant(
                                    detailsResult.getFormattedAddress(),
                                    detailsResult.getFormattedPhoneNumber(),
                                    detailsResult.getName(),
                                    detailsResult.getPlaceId(),
                                    photo,
                                    (detailsResult.getRating() != null)? detailsResult.getRating() : 0,
                                    detailsResult.getWebsite(),
                                    workers
                            );
                            mDetailRestaurantLiveData.setValue(restaurant);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Timber.e("error : %s", e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        Timber.i("on Complete");
                    }
                });

        return mDetailRestaurantLiveData;
    }
}
