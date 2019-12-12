package fr.nelfdesign.go4lunch.models;

import com.google.android.libraries.places.api.model.OpeningHours;

/**
 * Created by Nelfdesign at 28/11/2019
 * fr.nelfdesign.go4lunch
 */
public class Restaurant {

    private String name;
    private String icon;
    private String address;
    private OpeningHours hour;
    private String photoReference;
    private String distance;
    private int workers;
    private int stars;

    public Restaurant(String name, String address, String icon, OpeningHours hour, String urlImage, String distance, int workers, int stars) {
        this.name = name;
        this.icon = icon;
        this.address = address;
        this.hour = hour;
        this.photoReference = urlImage;
        this.distance = distance;
        this.workers = workers;
        this.stars = stars;
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

    public void setAddress(String address) {
        this.address = address;
    }

    public OpeningHours getHour() {
        return hour;
    }

    public void setHour(OpeningHours hour) {
        this.hour = hour;
    }

    public String getPhotoReference() {
        return photoReference;
    }

    public void setPhotoReference(String photoReference) {
        this.photoReference = photoReference;
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

    public int getStars() {
        return stars;
    }

    public void setStars(int stars) {
        this.stars = stars;
    }
}
