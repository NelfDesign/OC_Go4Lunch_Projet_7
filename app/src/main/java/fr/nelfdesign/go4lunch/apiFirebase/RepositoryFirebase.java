package fr.nelfdesign.go4lunch.apiFirebase;

import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
                            //String uid = document.getString("uid");
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
        ArrayList<Workers> finalMWorkers = new ArrayList<>();
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
                                finalMWorkers.add(w);
                                Timber.i("Workers list : %s", finalMWorkers.size());
                            }
                        }
                    } else {
                        Timber.w("Error getting documents."+ task.getException());
                    }
                });
        return finalMWorkers;
    }
}
