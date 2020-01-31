package fr.nelfdesign.go4lunch.models;

/**
 * Created by Nelfdesign at 16/12/2019
 * fr.nelfdesign.go4lunch.models
 */
public class Poi {

    //FILEDS
    private String title;
    private String placeId;
    private double lat;
    private double mLong;
    private boolean isChoosen;

    //Constructor
    public Poi(String title, String placeId, double lat, double aLong) {
        this.title = title;
        this.placeId = placeId;
        this.lat = lat;
        mLong = aLong;
        this.isChoosen = false;
    }

    //GETTERS AND SETTER

    public boolean isChoosen() {
        return isChoosen;
    }

    public void setChoosen(boolean choosen) {
        isChoosen = choosen;
    }

    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    public String getTitle() {
        return title;
    }

    public double getLat() {
        return lat;
    }

    public double getLong() {
        return mLong;
    }

}
