package fr.nelfdesign.go4lunch.apiFirebase;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.Query;

import fr.nelfdesign.go4lunch.models.RestaurantFavoris;

/**
 * Created by Nelfdesign at 08/01/2020
 * fr.nelfdesign.go4lunch.apiFirebase
 */
public class RestaurantsFavorisHelper {

    ///FIELD
    private static final String COLLECTION_NAME = "restaurants_favoris";

    // --- CREATE ---

    public static Task<DocumentReference> createFavoriteRestaurant(String user, String uid, String name, String placeId,
                                                                   String address, String photoReference, double rating) {
        RestaurantFavoris restoToCreate = new RestaurantFavoris(uid, name, placeId, address, photoReference, rating);

        //Store RestaurantFavoris to Firestore
        return WorkersHelper.getWorkersCollection()
                .document(user)
                .collection(COLLECTION_NAME)
                .add(restoToCreate);
    }

    // --- GET ---

    public static Query getAllRestaurantsFromWorkers(String name) {
        return WorkersHelper.getWorkersCollection()
                .document(name)
                .collection(COLLECTION_NAME);
    }

    // --- DELETE ---

    public static void deleteRestaurant(String user, String uid) {
        WorkersHelper.getWorkersCollection()
                .document(user)
                .collection(COLLECTION_NAME)
                .document(uid)
                .delete();
    }
}
