package fr.nelfdesign.go4lunch.ui.fragments;

import android.Manifest;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import fr.nelfdesign.go4lunch.R;
import fr.nelfdesign.go4lunch.models.LocationData;
import fr.nelfdesign.go4lunch.models.LocationLiveData;
import fr.nelfdesign.go4lunch.viewModels.MapViewModel;
import timber.log.Timber;

/**
 * A simple {@link Fragment} subclass.
 */
public class MapFragment extends Fragment implements OnMapReadyCallback {

    //Fields Google map
    private GoogleMap mGoogleMap;
    private GoogleMapOptions mapOptions;
    private LocationLiveData mLocationLiveData;
    private MapViewModel mMapViewModel;

    //Constant
    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final int LOCATION_START_UPDATE_REQUEST_CODE = 200;
    private static final float DEFAULT_ZOOM = 13f;
    private static final int REQUEST_CODE_LOCATION_SETTING = 100;

    public MapFragment() { }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mapOptions = new GoogleMapOptions()
                .mapType(GoogleMap.MAP_TYPE_NORMAL)
                .zoomControlsEnabled(true)
                .zoomGesturesEnabled(true)
                .minZoomPreference(DEFAULT_ZOOM);

        mLocationLiveData = new LocationLiveData(Objects.requireNonNull(this.getContext()));
        mLocationLiveData.observe(this, locationData -> {
            handleLocationData(locationData);
        });

        mMapViewModel = ViewModelProviders.of(Objects.requireNonNull(this.getActivity())).get(MapViewModel.class);

        initMap();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        return view;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case REQUEST_CODE_LOCATION_SETTING:
                mLocationLiveData.requestUpdateLocation();
        }
    }

    private void handleLocationData(LocationData locationData) {
        if (handleLocationException(locationData.getException()) || locationData.getLocation() == null){
            return;
        }
        if (mGoogleMap != null){
            //Add marker on Nantes
            LatLng nantes = new LatLng(47.21,-1.55);
            mGoogleMap.addMarker(new MarkerOptions().position(nantes).title("Marker on Nantes"));
            mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(nantes));
        }

    }

    private boolean handleLocationException(@Nullable Exception exception) {
        if (exception == null) {
            return false;
        }

        Timber.e(exception + "handleLocationException()");
        if (exception instanceof SecurityException) {
            this.checkLocationPermission(LOCATION_START_UPDATE_REQUEST_CODE);
        }

        if (exception instanceof ResolvableApiException) {
            // Location settings are not satisfied, but this can be fixed
            // by showing the user a dialog.
            try {
                // Show the dialog by calling startResolutionForResult(),
                // and check the result in onActivityResult().
                ResolvableApiException resolvable = (ResolvableApiException) exception;
                resolvable.startResolutionForResult(getActivity(),
                        REQUEST_CODE_LOCATION_SETTING);
            } catch (IntentSender.SendIntentException sendEx) {
                // Ignore the error.
            }
        }

        return true;
    }

    private boolean checkLocationPermission(int requestCode) {
        Timber.d("getLocationPermission: getting location permissions");
        String[] permissions = {FINE_LOCATION};

        if (ContextCompat.checkSelfPermission(Objects.requireNonNull(this.getContext()),
                FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(Objects.requireNonNull(this.getActivity()),
                    permissions,
                    requestCode);
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NotNull String[] permissions, @NotNull int[] grantResults) {
        if (grantResults[0] != PackageManager.PERMISSION_GRANTED){
            Timber.i("need to access location");
            return;
        }

        switch (requestCode){
            case LOCATION_START_UPDATE_REQUEST_CODE :
                mLocationLiveData.requestUpdateLocation();
                break;
        }
    }

    //initialisation de la carte
    private void initMap() {
        Timber.d( "initMap: initializing map");
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map_Fragment);

        if(mapFragment == null){
            mapFragment = SupportMapFragment.newInstance(mapOptions);
            if (getFragmentManager() != null) {
                getFragmentManager().beginTransaction().replace(R.id.map_Fragment, mapFragment).commit();
            }
        }
        mapFragment.getMapAsync(this);
    }

}
