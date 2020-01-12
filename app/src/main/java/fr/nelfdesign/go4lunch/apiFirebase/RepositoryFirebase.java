package fr.nelfdesign.go4lunch.apiFirebase;

import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

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
                            Workers w = document.toObject(Workers.class);
                            mWorkers.add(w);
                            Timber.d(document.getId() + " => " + document.getData());
                        }
                    } else {
                        Timber.w("Error getting documents : %s", Objects.requireNonNull(task.getException()).getMessage());
                    }
                });
        return query;
    }

    public static Query getQueryFavoritesRestaurant(List<RestaurantFavoris> favorises){
        Query query = RestaurantsFavorisHelper.getAllRestaurants();
        query.get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                            RestaurantFavoris restaurantFavoris = document.toObject(RestaurantFavoris.class);

                            favorises.add(restaurantFavoris);
                        }
                    } else {
                        Timber.w("Error getting documents : %s", Objects.requireNonNull(task.getException()).getMessage());
                    }
                });
        return query;
    }



}
