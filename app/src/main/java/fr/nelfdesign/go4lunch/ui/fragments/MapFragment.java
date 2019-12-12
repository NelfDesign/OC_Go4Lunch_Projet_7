package fr.nelfdesign.go4lunch.ui.fragments;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

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
import timber.log.Timber;

/**
 * A simple {@link Fragment} subclass.
 */
public class MapFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMapOptions mapOptions;

    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 100;
    private static final float DEFAULT_ZOOM = 13f;

    public MapFragment() { }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mapOptions = new GoogleMapOptions()
                .mapType(GoogleMap.MAP_TYPE_NORMAL)
                .zoomControlsEnabled(true)
                .zoomGesturesEnabled(true)
                .minZoomPreference(DEFAULT_ZOOM);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        updateLocation();
        return view;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        //Add marker on Nantes
        LatLng nantes = new LatLng(47.21,-1.55);
        googleMap.addMarker(new MarkerOptions().position(nantes).title("Marker on Nantes"));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(nantes));
    }

    private void updateLocation() {
        if (!checkLocationPermission()){
            return;
        }
        initMap();
    }

    private boolean checkLocationPermission() {
        Timber.d("getLocationPermission: getting location permissions");
        String[] permissions = {FINE_LOCATION};

        if (ContextCompat.checkSelfPermission(Objects.requireNonNull(this.getContext()),
                FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            requestPermissions(
                    permissions,
                    LOCATION_PERMISSION_REQUEST_CODE);
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NotNull String[] permissions, @NotNull int[] grantResults) {
        if (grantResults[0] != PackageManager.PERMISSION_GRANTED){
            Timber.i("need to access location");
        }else if (requestCode == LOCATION_PERMISSION_REQUEST_CODE){
            updateLocation();
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
