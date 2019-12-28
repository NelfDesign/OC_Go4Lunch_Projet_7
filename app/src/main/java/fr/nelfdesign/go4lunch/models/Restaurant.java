package fr.nelfdesign.go4lunch.models;

import com.google.android.libraries.places.api.model.OpeningHours;

import fr.nelfdesign.go4lunch.pojos.Location;

/**
 * Created by Nelfdesign at 28/11/2019
 * fr.nelfdesign.go4lunch
 */
public class Restaurant {

    private Location mLocation;
    private String name;
    private String icon;
    private String address;
    private Boolean hour;
    private String photoReference;
    private String distance;
    private int workers;
    private double rating;

    public Restaurant(Location location, String name, String address, String icon, boolean hour, String urlImage,
                      String distance, int workers, double rating) {
        this.mLocation = location;
        this.name = name;
        this.icon = icon;
        this.address = address;
        this.hour = hour;
        this.photoReference = urlImage;
        this.distance = distance;
        this.workers = workers;
        this.rating = rating;
    }

    public Location getLocation() {
        return mLocation;
    }

    public void setLocation(Location location) {
        mLocation = location;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getAddress() {
        return address;
    }

    public boolean getHour() {
        return hour;
    }

    public String getPhotoReference() {
        return photoReference;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public int getWorkers() {
        return workers;
    }

    public void setWorkers(int workers) {
        this.workers = workers;
    }

    public double getRating() {
        return rating;
    }

}
