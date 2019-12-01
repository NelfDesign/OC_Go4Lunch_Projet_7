package fr.nelfdesign.go4lunch.apiFirebase;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

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

    public static Task<Void> createWorker(String uid, String name, String avatar) {
        Workers workerToCreate = new Workers(uid, name, avatar);
        return WorkersHelper.getWorkersCollection().document(uid).set(workerToCreate);
    }

    // --- GET ---

    public static Task<DocumentSnapshot> getWorker(String uid){
        return WorkersHelper.getWorkersCollection().document(uid).get();
    }

    // --- UPDATE ---

    public static Task<Void> updateRestaurantChoice(String uid, Boolean isChosen) {
        return WorkersHelper.getWorkersCollection().document(uid).update("restaurantChoice", isChosen);
    }

    // --- DELETE ---

    public static Task<Void> deleteWorker(String uid) {
        return WorkersHelper.getWorkersCollection().document(uid).delete();
    }
}
