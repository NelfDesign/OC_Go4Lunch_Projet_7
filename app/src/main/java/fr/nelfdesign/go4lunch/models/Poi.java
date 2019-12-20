package fr.nelfdesign.go4lunch.models;

/**
 * Created by Nelfdesign at 16/12/2019
 * fr.nelfdesign.go4lunch.models
 */
public class Poi {

    private String title;
    private double lat;
    private double mLong;
    private int iconId;

    public Poi(String title, double lat, double aLong, int iconId) {
        this.title = title;
        this.lat = lat;
        mLong = aLong;
        this.iconId = iconId;
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

    public int getIconId() {
        return iconId;
    }

    public void setIconId(int iconId) {
        this.iconId = iconId;
    }
}
