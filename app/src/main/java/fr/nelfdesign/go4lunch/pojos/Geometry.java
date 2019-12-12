package fr.nelfdesign.go4lunch.pojos;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Nelfdesign at 09/12/2019
 * fr.nelfdesign.go4lunch.models
 */
public class Geometry {

    @SerializedName("location")
    @Expose
    private Location location;

    public Location getLocation() {
        return location;
    }
}
