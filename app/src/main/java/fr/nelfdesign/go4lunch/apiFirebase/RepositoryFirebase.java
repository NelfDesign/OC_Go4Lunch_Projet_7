package fr.nelfdesign.go4lunch.apiFirebase;

import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.List;
import java.util.Objects;

import fr.nelfdesign.go4lunch.R;
import fr.nelfdesign.go4lunch.models.RestaurantFavoris;
import fr.nelfdesign.go4lunch.models.Workers;
import timber.log.Timber;

/**
 * Created by Nelfdesign at 09/01/2020
 * fr.nelfdesign.go4lunch.apiFirebase
 */
public abstract class RepositoryFirebase {

    /**
     * create a query for workers BDD
     * @param mWorkers list of workers
     * @return query for Firebase recyclerView options
     */
    public static Query getQueryWorkers(List<Workers> mWorkers){
        Query query = WorkersHelper.getWorkersCollection().orderBy("restaurantName", Query.Direction.DESCENDING);
        query.get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                            Workers w = document.toObject(Workers.class);
                            mWorkers.add(w);
                            Timber.d(document.getId() + " => " + document.getData());
                        }
                    } else {
                        Timber.w(String.valueOf(R.string.error_query), Objects.requireNonNull(task.getException()).getMessage());
                    }
                });
        return query;
    }

    /**
     * create a query for favorite restaurant BDD
     * @param favorises list of restaurant
     * @param user user logged
     * @return query for firebase RV
     */
    public static Query getQueryFavoritesRestaurant(List<RestaurantFavoris> favorises, String user){
        Query query = RestaurantsFavorisHelper.getAllRestaurantsFromWorkers(user);
        query.get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                            RestaurantFavoris restaurantFavoris = document.toObject(RestaurantFavoris.class);

                            favorises.add(restaurantFavoris);
                        }
                    } else {
                        Timber.w(String.valueOf(R.string.error_query), Objects.requireNonNull(task.getException()).getMessage());
                    }
                });
        return query;
    }


}
