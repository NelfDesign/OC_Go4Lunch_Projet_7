package fr.nelfdesign.go4lunch.models;

/**
 * Created by Nelfdesign at 30/11/2019
 * fr.nelfdesign.go4lunch.models
 */
public class Workers {

    private String uid;
    // Fullname
    private String name;
    // Avatar
    private String avatarUrl;
    // Restaurant chosen
    private boolean restaurantChoice;
    // Restaurant name
    private String restaurantName;
    //Restaurant placeId
    private String placeId;

    public Workers(){}

    public Workers (String name, String avatarUrl, String resto, String placeId) {
        this.name = name;
        this.avatarUrl = avatarUrl;
        this.restaurantName = resto;
        this.placeId = placeId;
        restaurantChoice = false;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    public String getRestaurantName() {
        return restaurantName;
    }

    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }

    public boolean isRestaurantChoice() {
        return restaurantChoice;
    }

    public void setRestaurantChoice(boolean restaurantChoice) {
        this.restaurantChoice = restaurantChoice;
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

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }
}
