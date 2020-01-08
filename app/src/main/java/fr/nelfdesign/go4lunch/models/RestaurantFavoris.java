package fr.nelfdesign.go4lunch.models;

/**
 * Created by Nelfdesign at 08/01/2020
 * fr.nelfdesign.go4lunch.models
 */
public class RestaurantFavoris {

    private String name;
    private String placeId;
    private String address;
    private String photoReference;
    private double rating;

    public RestaurantFavoris(String name, String placeId, String address, String photoReference, double rating) {
        this.name = name;
        this.placeId = placeId;
        this.address = address;
        this.photoReference = photoReference;
        this.rating = rating;
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

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhotoReference() {
        return photoReference;
    }

    public void setPhotoReference(String photoReference) {
        this.photoReference = photoReference;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }
}
