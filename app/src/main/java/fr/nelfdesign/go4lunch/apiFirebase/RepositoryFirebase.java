package fr.nelfdesign.go4lunch.apiFirebase;

import androidx.lifecycle.MutableLiveData;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import fr.nelfdesign.go4lunch.models.RestaurantFavoris;
import fr.nelfdesign.go4lunch.models.Workers;
import timber.log.Timber;

/**
 * Created by Nelfdesign at 09/01/2020
 * fr.nelfdesign.go4lunch.apiFirebase
 */
public abstract class RepositoryFirebase {


    public static Query getQueryWorkers(List<Workers> mWorkers){
        Query query = WorkersHelper.getWorkersCollection().orderBy("name");
        query.get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                            String name = document.getString("name");
                            String url = document.getString("avatarUrl");
                            String resto = document.getString("restaurantName");
                            String placeId = document.getString("placeId");
                            Workers w = new Workers(name,url,resto, placeId);
                            mWorkers.add(w);
                            Timber.d(document.getId() + " => " + document.getData());
                        }
                    } else {
                        Timber.w("Error getting documents."+ task.getException());
                    }
                });
        return query;
    }

    public static ArrayList<Workers> getQueryWorkersWithChoiceRestaurant(String placeIdData){

        Query query = WorkersHelper.getWorkersCollection().orderBy("name");
        ArrayList<Workers> workers = new ArrayList<>();
        query.get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                            String name = document.getString("name");
                            String url = document.getString("avatarUrl");
                            String resto = document.getString("restaurantName");
                            String placeId = document.getString("placeId");
                            Workers w = new Workers(name,url,resto, placeId);

                            if (Objects.equals(w.getPlaceId(), placeIdData)){
                                workers.add(w);
                                Timber.i("Workers list : %s", workers.size());
                            }

                        }
                    } else {
                        Timber.w("Error getting documents."+ task.getException());
                    }
                });
        return workers;
    }

    public static ArrayList<RestaurantFavoris> getFavoritesRestaurant(){
        ArrayList<RestaurantFavoris> restoList = new ArrayList<>();
        Query query = RestaurantsFavorisHelper.getAllRestaurants();
        query.get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                            String name = document.getString("name");
                            String address = document.getString("address");
                            String photoReference = document.getString("photoReference");
                            String placeId = document.getString("placeId");
                            Double rating = document.getDouble("rating");
                            RestaurantFavoris resto = new RestaurantFavoris(name, placeId, address, photoReference,rating);

                            restoList.add(resto);
                        }
                    } else {
                        Timber.w("Error getting documents : %s", task.getException());
                    }
                });
        return restoList;
    }

}
