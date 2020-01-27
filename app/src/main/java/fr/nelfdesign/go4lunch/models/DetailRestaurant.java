package fr.nelfdesign.go4lunch.models;

/**
 * Created by Nelfdesign at 29/12/2019
 * fr.nelfdesign.go4lunch.models
 */
public class DetailRestaurant {

    private String formatted_address;
    private String formatted_phone_number;
    private String name;
    private String photo;
    private double rating;
    private String website;

    public DetailRestaurant(String formatted_address, String formatted_phone_number, String name,
                            String photoReference, double rating, String website) {
        this.formatted_address = formatted_address;
        this.formatted_phone_number = formatted_phone_number;
        this.name = name;
        this.photo = photoReference;
        this.rating = rating;
        this.website = website;
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

    public double getRating() {
        return rating;
    }

    public String getWebsite() {
        return website;
    }
}
