package fr.nelfdesign.go4lunch.models;

import fr.nelfdesign.go4lunch.pojos.Location;

/**
 * Created by Nelfdesign at 28/11/2019
 * fr.nelfdesign.go4lunch
 */
public class Restaurant {

    private Location mLocation;
    private String name;
    private String placeId;
    private String address;
    private Boolean hour;
    private String photoReference;
    private int distance;
    private int workers;
    private double rating;
    private boolean choice;

    public Restaurant(Location location, String name, String address, String placeId, boolean hour, String urlImage,
                      int distance, int workers, double rating) {
        this.mLocation = location;
        this.name = name;
        this.placeId = placeId;
        this.address = address;
        this.hour = hour;
        this.photoReference = urlImage;
        this.distance = distance;
        this.workers = workers;
        this.rating = rating;
        this.choice = false;
    }

    public boolean isChoice() {
        return choice;
    }

    public void setChoice(boolean choice) {
        this.choice = choice;
    }

    public Location getLocation() {
        return mLocation;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
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

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
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
