package fr.nelfdesign.go4lunch.pojos;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Nelfdesign at 10/12/2019
 * fr.nelfdesign.go4lunch.pojos
 */
public class RestaurantsResult {

    @SerializedName("html_attributions")
    @Expose
    private List<Object> htmlAttributions = null;
    @SerializedName("results")
    @Expose
    private List<Result> results = null;
    @SerializedName("status")
    @Expose
    private String status;

    //GETTERS
    public List<Object> getHtmlAttributions() {
        return htmlAttributions;
    }

    public List<Result> getResults() {
        return results;
    }

    public String getStatus() {
        return status;
    }

}
