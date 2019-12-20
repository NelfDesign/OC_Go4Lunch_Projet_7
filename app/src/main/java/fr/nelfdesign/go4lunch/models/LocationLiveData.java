package fr.nelfdesign.go4lunch.models;

import android.content.Context;
import android.location.Location;

import androidx.lifecycle.LiveData;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.Task;

/**
 * Created by Nelfdesign at 16/12/2019
 * fr.nelfdesign.go4lunch.models
 */
public class LocationLiveData extends LiveData<LocationData> {

    // FIELDS --------------------------------------------------------------------------------------

    private Context appContext;

    private FusedLocationProviderClient mFusedLocationProviderClient;
    private LocationRequest mLocationRequest;
    private LocationCallback mLocationCallback;

    private boolean isFirstSubscriber = true;

    // CONSTRUCTORS --------------------------------------------------------------------------------

    /**
     * Constructor
     * @param context a {@link Context}
     */
    public LocationLiveData(Context context) {
        this.appContext = context.getApplicationContext();

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this.appContext);
        this.configureLocationRequest();
        this.configureLocationCallback();
    }

    @Override
    protected void onActive() {
        super.onActive();

        if (isFirstSubscriber) {
           requestLastLocation();
           requestLocation();

            // For the other subscribers
            isFirstSubscriber = false;
        }
    }

    @Override
    protected void onInactive() {
        super.onInactive();
        mFusedLocationProviderClient.removeLocationUpdates(this.mLocationCallback);

        // For the other subscribers
        isFirstSubscriber = true;
    }

    /**
     * Creates the locationRequest
     */
    private void configureLocationRequest() {
        mLocationRequest = LocationRequest.create();
        mLocationRequest.setInterval(10000)
                            .setFastestInterval(5000)
                            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    /**
     * Configures the LocationCallback
     */
    private void configureLocationCallback() {
        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }

                for (Location location : locationResult.getLocations()) {
                    setValue(new LocationData(location, null));
                }
            }
        };
    }

    /**
     * Requests the last location
     */
    private void requestLastLocation() {
        mFusedLocationProviderClient.getLastLocation()
                .addOnSuccessListener( location ->
                        this.setValue(new LocationData(location, null))
                )
                .addOnFailureListener( exception ->
                        this.setValue(new LocationData(null, exception))
                );
    }

    /**
     * Requests the update location
     */
    public void requestUpdateLocation() {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(this.mLocationRequest);

        SettingsClient client = LocationServices.getSettingsClient(appContext);

        Task<LocationSettingsResponse> task = client.checkLocationSettings(builder.build());

               task.addOnSuccessListener( locationSettingsResponse ->
                        requestLocation()
                )
                .addOnFailureListener( exception ->
                        setValue(new LocationData(null, exception))
                );
    }

    /**
     * Requests the location
     */
    private void requestLocation() {
        try {
            mFusedLocationProviderClient.requestLocationUpdates(mLocationRequest,
                    mLocationCallback,
                    null);
        }catch (SecurityException e){
            setValue(new LocationData(null, e));
        }
    }
}
