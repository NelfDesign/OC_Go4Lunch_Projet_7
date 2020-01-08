package fr.nelfdesign.go4lunch.apiFirebase;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import fr.nelfdesign.go4lunch.models.RestaurantFavoris;

/**
 * Created by Nelfdesign at 08/01/2020
 * fr.nelfdesign.go4lunch.apiFirebase
 */
public class RestaurantsFavorisHelper {

    private static final String COLLECTION_NAME = "restaurants_favoris";

    // --- COLLECTION REFERENCE ---

    public static CollectionReference getRestaurantsCollection(){
        return FirebaseFirestore.getInstance().collection(COLLECTION_NAME);
    }

    // --- CREATE ---

    public static Task<Void> createFavoriteRestaurant(String name, String placeId, String adress, String photoReference, double rating) {
        RestaurantFavoris restoToCreate = new RestaurantFavoris(name, placeId,adress, photoReference, rating);
        return RestaurantsFavorisHelper.getRestaurantsCollection().document().set(restoToCreate);
    }

    // -- GET ALL Workers --
    public static Query getAllRestaurants(){
        return RestaurantsFavorisHelper.getRestaurantsCollection();
    }

    // --- DELETE ---

    public static Task<Void> deleteRestaurant(String uid) {
        return RestaurantsFavorisHelper.getRestaurantsCollection().document(uid).delete();
    }
}
