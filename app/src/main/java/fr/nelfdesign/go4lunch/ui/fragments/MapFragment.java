package fr.nelfdesign.go4lunch.ui.fragments;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.preference.PreferenceManager;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.ListenerRegistration;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import fr.nelfdesign.go4lunch.R;
import fr.nelfdesign.go4lunch.apiFirebase.WorkersHelper;
import fr.nelfdesign.go4lunch.models.Poi;
import fr.nelfdesign.go4lunch.models.Restaurant;
import fr.nelfdesign.go4lunch.models.Workers;
import fr.nelfdesign.go4lunch.ui.activity.RestaurantDetail;
import fr.nelfdesign.go4lunch.ui.viewModels.MapViewModel;
import timber.log.Timber;

/**
 * A simple {@link Fragment} subclass.
 */
public class MapFragment extends Fragment implements OnMapReadyCallback {

    //Fields
    private GoogleMap mGoogleMap;
    private GoogleMapOptions mapOptions;
    private FusedLocationProviderClient mFusedLocationClient;
    private MapViewModel mMapViewModel;
    private LatLng lastPosition;
    private ArrayList<Workers> mWorkersArrayList;
    private ListenerRegistration mListenerRegistration = null;
    //Constant
    private static final String PREF_ZOOM = "zoom_key";
    private static final String PREF_RADIUS = "radius_key";
    private static final String PREF_TYPE = "type_key";

    //constructor
    public MapFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mapOptions = new GoogleMapOptions()
                .mapType(GoogleMap.MAP_TYPE_NORMAL)
                .zoomControlsEnabled(true)
                .zoomGesturesEnabled(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);

        // Location Services
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(Objects.requireNonNull(getContext()));

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        //init map
        initMap();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mListenerRegistration != null) {
            mListenerRegistration.remove();
        }
    }

    /**
     * Initialize liveData and viewModel after map is ready
     *
     * @param googleMap for map
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.mGoogleMap = googleMap;
        mGoogleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(Objects.requireNonNull(this.getContext()), R.raw.maps_style));
        Timber.i("Map ready");

        final CollectionReference workersRef = WorkersHelper.getWorkersCollection();
        mListenerRegistration = workersRef.addSnapshotListener((queryDocumentSnapshots, e) -> {
            mWorkersArrayList = new ArrayList<>();
            if (queryDocumentSnapshots != null) {
                for (DocumentSnapshot data : Objects.requireNonNull(queryDocumentSnapshots).getDocuments()) {

                    if (data.get("placeId") != null) {
                        Workers workers = data.toObject(Workers.class);
                        mWorkersArrayList.add(workers);
                        Timber.i("snap workers : %s", mWorkersArrayList.size());
                    }
                }
            }
        });

        getLocationPermission();

        mMapViewModel = ViewModelProviders.of(Objects.requireNonNull(this.getActivity())).get(MapViewModel.class);

    }

    /**
     * get user location
     */
    private void getUserLocation() {
        mFusedLocationClient.getLastLocation()
                .addOnSuccessListener(Objects.requireNonNull(getActivity()), location -> {
                    if (location != null) {
                        // get the location phone
                        lastPosition = new LatLng(location.getLatitude(), location.getLongitude());
                        Timber.i("LastLocation : %s", lastPosition.latitude);
                        //Update UI with information
                        updateUiMap(lastPosition);
                    }
                });
    }

    /**
     * check permission with dexter library
     */
    private void getLocationPermission() {
        Dexter.withActivity(getActivity())
                .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        //Get last location and update UI
                        getUserLocation();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        Toast.makeText(getContext(), R.string.permission_denied, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {

                    }
                }).check();
    }

    //init map
    private void initMap() {
        Timber.d("initMap: initializing map");
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map_Fragment);

        if (mapFragment == null) {
            //add option to the map
            mapFragment = SupportMapFragment.newInstance(mapOptions);
            if (getFragmentManager() != null) {
                getFragmentManager().beginTransaction().replace(R.id.map_Fragment, mapFragment).commit();
            }
        }
        mapFragment.getMapAsync(this);
    }

    /**
     * generate marker with restaurant list
     *
     * @param restaurants list
     */
    private void generatePoisRestaurant(ArrayList<Restaurant> restaurants) {
        List<Poi> listPoi = mMapViewModel.generatePois(restaurants, mWorkersArrayList);
        for (Poi p : listPoi) {
            createRestaurantsMarker(p, mGoogleMap);
            mGoogleMap.setOnMarkerClickListener(marker -> {
                launchRestaurantDetail(marker);
                return true;
            });
        }
    }

    /**
     * create user marker
     *
     * @param poi poi
     * @param map map
     */
    private void createUserMarker(Poi poi, GoogleMap map) {
        setMarkerPoi(poi, map, R.drawable.ic_marquer);
    }

    /**
     * create marker for all restaurant
     *
     * @param poi poi
     * @param map map
     */
    private void createRestaurantsMarker(Poi poi, GoogleMap map) {
        if (poi.isChoosen()) {
            setMarkerPoi(poi, map, R.drawable.ic_resto_green2);
        } else {
            setMarkerPoi(poi, map, R.drawable.ic_resto_red2);
        }
    }

    /**
     * create custom marker
     *
     * @param poi  poi
     * @param map  map
     * @param icon icon
     */
    private void setMarkerPoi(Poi poi, GoogleMap map, int icon) {
        MarkerOptions markerOptions = new MarkerOptions()
                .position(new LatLng(poi.getLat(), poi.getLong()))
                .title(poi.getTitle())
                .icon(BitmapDescriptorFactory.fromResource(icon));
        Marker marker = map.addMarker(markerOptions);
        //Add tag to save restaurant placeId for earlier
        if ((poi.getPlaceId() != null)) {
            marker.setTag(poi.getPlaceId());
            marker.setTitle(poi.getTitle());
        }
    }

    /**
     * update UI with settings
     *
     * @param latLng user LatLng
     */
    private void updateUiMap(LatLng latLng) {
        //create user marker
        createUserMarker(mMapViewModel.generateUserPoi(latLng.latitude, latLng.longitude),
                mGoogleMap);
        //move camera to user position
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(Objects.requireNonNull(this.getContext()));
        float zoom;
        switch (sharedPreferences.getString(PREF_ZOOM, "")) {
            case "High":
                zoom = 15f;
                break;
            case "Medium":
                zoom = 13f;
                break;
            case "Less":
                zoom = 9f;
                break;

            default:
                zoom = 10f;
        }

        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
        //set my position and enable position button
        mGoogleMap.setMyLocationEnabled(true);
        //observe ViewModel restaurants data
        mMapViewModel.getAllRestaurants(latLng,
                sharedPreferences.getString(PREF_RADIUS, ""),
                sharedPreferences.getString(PREF_TYPE, ""))
                .observe(Objects.requireNonNull(this.getActivity()), this::generatePoisRestaurant);
    }

    /**
     * launch detail restaurant page on marker click
     *
     * @param marker position of a restaurant
     */
    private void launchRestaurantDetail(Marker marker) {
        String ref = (String) marker.getTag();
        String name = marker.getTitle();
        Intent intent = new Intent(getContext(), RestaurantDetail.class);
        intent.putExtra("placeId", ref);
        intent.putExtra("restaurantName", name);
        startActivity(intent);
    }
}
