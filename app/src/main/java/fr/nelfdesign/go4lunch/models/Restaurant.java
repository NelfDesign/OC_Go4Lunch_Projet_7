package fr.nelfdesign.go4lunch.models;

/**
 * Created by Nelfdesign at 28/11/2019
 * fr.nelfdesign.go4lunch
 */
public class Restaurant {

    private String name;
    private String category;
    private String hour;
    private String urlImage;
    private String distance;
    private int workers;
    private int stars;

    public Restaurant(String name, String category, String hour, String urlImage, String distance, int workers, int stars) {
        this.name = name;
        this.category = category;
        this.hour = hour;
        this.urlImage = urlImage;
        this.distance = distance;
        this.workers = workers;
        this.stars = stars;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    public String getUrlImage() {
        return urlImage;
    }

    public void setUrlImage(String urlImage) {
        this.urlImage = urlImage;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public int getWorkers() {
        return workers;
    }

    public void setWorkers(int workers) {
        this.workers = workers;
    }

    public int getStars() {
        return stars;
    }

    public void setStars(int stars) {
        this.stars = stars;
    }
}
