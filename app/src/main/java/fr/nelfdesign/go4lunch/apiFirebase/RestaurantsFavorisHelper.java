package fr.nelfdesign.go4lunch.apiFirebase;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.HashMap;
import java.util.Map;

import fr.nelfdesign.go4lunch.models.RestaurantFavoris;

/**
 * Created by Nelfdesign at 08/01/2020
 * fr.nelfdesign.go4lunch.apiFirebase
 */
public class RestaurantsFavorisHelper {

    private static final String COLLECTION_NAME = "restaurants_favoris";

    // --- COLLECTION REFERENCE ---

    public static CollectionReference getRestaurantFavoritesCollection(){
        return FirebaseFirestore.getInstance().collection(COLLECTION_NAME);
    }

    // --- CREATE ---

    public static Task<DocumentReference> createFavoriteRestaurant(String user,String uid, String name, String placeId,
                                                                   String address, String photoReference, double rating) {
        RestaurantFavoris restoToCreate = new RestaurantFavoris(uid,name, placeId, address, photoReference, rating);

        //Store Message to Firestore
        return WorkersHelper.getWorkersCollection()
                .document(user)
                .collection(COLLECTION_NAME)
                .add(restoToCreate);
    }

    // --- GET ---

    public static Query getAllRestaurantsFromWorkers(String name){
        return WorkersHelper.getWorkersCollection()
                .document(name)
                .collection(COLLECTION_NAME);
    }

    // --- UPDATE ---
    public static Task<Void> updateRestaurantFavorite(String id, String user) {

        Map<String, Object> data = new HashMap<>();
        data.put("uid", id);
        return WorkersHelper.getWorkersCollection()
                .document(user)
                .collection(COLLECTION_NAME)
                .document()
                .update(data);
    }

    // --- DELETE ---

    public static Task<Void> deleteRestaurant(String user, String uid) {
        return WorkersHelper.getWorkersCollection()
                .document(user)
                .collection(COLLECTION_NAME)
                .document(uid)
                .delete();
    }
}
