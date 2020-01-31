package fr.nelfdesign.go4lunch.models;

/**
 * Created by Nelfdesign at 30/11/2019
 * fr.nelfdesign.go4lunch.models
 */
public class Workers {

    // FIELDS
    private String name;
    private String avatarUrl;
    private String restaurantName;
    private String placeId;

    //constructors
    public Workers() {
    }

    public Workers(String name, String avatarUrl, String resto, String placeId) {
        this.name = name;
        this.avatarUrl = avatarUrl;
        this.restaurantName = resto;
        this.placeId = placeId;
    }

    //GETTER AND SETTER
    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    public String getRestaurantName() {
        return restaurantName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

}
