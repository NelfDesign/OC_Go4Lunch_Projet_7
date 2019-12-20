package fr.nelfdesign.go4lunch.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Nelfdesign at 30/11/2019
 * fr.nelfdesign.go4lunch.models
 */
public class Workers {

    /** Fullname */
    private String name;
    /** Avatar */

    private String avatarUrl;
    /** Restaurant chosen */
    private boolean restaurantChoice;

    public Workers(){}

    public Workers (String name, String avatarUrl) {
        this.name = name;
        this.avatarUrl = avatarUrl;
        restaurantChoice = false;
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
