package fr.nelfdesign.go4lunch.apiFirebase;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

import fr.nelfdesign.go4lunch.models.Workers;

/**
 * Created by Nelfdesign at 30/11/2019
 * fr.nelfdesign.go4lunch.apiFirebase
 */
public class WorkersHelper {

    private static final String COLLECTION_NAME = "workers";

    // --- COLLECTION REFERENCE ---

    public static CollectionReference getWorkersCollection(){
        return FirebaseFirestore.getInstance().collection(COLLECTION_NAME);
    }

    // --- CREATE ---

    public static Task<Void> createWorker(String uid,String name, String avatar, String resto, String placeId) {
        Workers workerToCreate = new Workers(uid, name, avatar, resto, placeId);
        return WorkersHelper.getWorkersCollection().document().set(workerToCreate);
    }

    // --- GET ---

    public static Task<QuerySnapshot> getWorker(String restaurant){
        return WorkersHelper.getWorkersCollection().document().collection(restaurant).get();
    }

    // -- GET ALL Workers --
    public static Query getAllWorkers(){
        return WorkersHelper.getWorkersCollection();
    }

    // --- UPDATE ---

    public static Task<Void> updateRestaurantChoice(String uid, String restoName, String placeId) {
        return WorkersHelper.getWorkersCollection().document(uid).update("restaurantName", restoName,
                                                                "placeId", placeId);
    }

    // --- DELETE ---

    public static Task<Void> deleteWorker(String uid) {
        return WorkersHelper.getWorkersCollection().document(uid).delete();
    }
}
