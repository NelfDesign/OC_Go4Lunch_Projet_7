package fr.nelfdesign.go4lunch.models;

/**
 * Created by Nelfdesign at 16/12/2019
 * fr.nelfdesign.go4lunch.models
 */
public class Poi {

    private String title;
    private String placeId;
    private double lat;
    private double mLong;

    public Poi(String title, String placeId, double lat, double aLong) {
        this.title = title;
        this.placeId = placeId;
        this.lat = lat;
        mLong = aLong;
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

    public void setTitle(String title) {
        this.title = title;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLong() {
        return mLong;
    }

    public void setLong(double aLong) {
        mLong = aLong;
    }

}
