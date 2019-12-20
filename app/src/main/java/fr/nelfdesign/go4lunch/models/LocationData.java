package fr.nelfdesign.go4lunch.models;

import android.location.Location;

/**
 * Created by Nelfdesign at 16/12/2019
 * fr.nelfdesign.go4lunch.models
 */
public class LocationData {

    private Location mLocation;
    private Exception mException;

    public LocationData(Location location, Exception exception) {
        mLocation = location;
        mException = exception;
    }

    public Exception getException() {
        return mException;
    }

    public void setException(Exception exception) {
        mException = exception;
    }

    public Location getLocation() {
        return mLocation;
    }

    public void setLocation(Location location) {
        mLocation = location;
    }
}
