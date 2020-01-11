package fr.nelfdesign.go4lunch.models;

import java.util.ArrayList;

/**
 * Created by Nelfdesign at 29/12/2019
 * fr.nelfdesign.go4lunch.models
 */
public class DetailRestaurant {

    private String formatted_address;
    private String formatted_phone_number;
    private String name;
    private String place_id;
    private String photo;
    private double rating;
    private String website;
    //private ArrayList<Workers> mWorkersArrayList;

    public DetailRestaurant(String formatted_address, String formatted_phone_number, String name, String place_id,
                            String photoReference, double rating, String website) {
        this.formatted_address = formatted_address;
        this.formatted_phone_number = formatted_phone_number;
        this.name = name;
        this.photo = photoReference;
        this.place_id = place_id;
        this.rating = rating;
        this.website = website;
        //this.mWorkersArrayList = workers;
    }

    public String getFormatted_address() {
        return formatted_address;
    }

    public String getFormatted_phone_number() {
        return formatted_phone_number;
    }

    public String getPhotoReference() {
        return photo;
    }

    public String getName() {
        return name;
    }

    public String getPlace_id() {
        return place_id;
    }

    public double getRating() {
        return rating;
    }

    public String getWebsite() {
        return website;
    }
}
