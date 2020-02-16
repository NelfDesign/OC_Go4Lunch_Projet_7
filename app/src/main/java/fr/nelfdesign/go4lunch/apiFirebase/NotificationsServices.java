package fr.nelfdesign.go4lunch.apiFirebase;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.preference.PreferenceManager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.ArrayList;
import java.util.Objects;

import fr.nelfdesign.go4lunch.R;
import fr.nelfdesign.go4lunch.models.Workers;
import fr.nelfdesign.go4lunch.ui.activity.MainActivity;

/**
 * Created by Nelfdesign at 21/01/2020
 * fr.nelfdesign.go4lunch.apiFirebase
 */
public class NotificationsServices extends FirebaseMessagingService {

    //FIELDS
    private static final String PREF_NOTIFICATION = "notification_firebase";
    private String restaurant;
    private ArrayList<Workers> mWorkers;

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        //get restaurant name
        getRestaurantUser();
        //get workers list
        mWorkers = getRestaurantUserAndWorkersAdd();
        //put Thread on sleep for 2 sec to make sure that the query are finish
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // send message if able
        if (remoteMessage.getNotification() != null &&
                sharedPreferences.getBoolean(PREF_NOTIFICATION, true) &&
                restaurant != null) {
            // Get message sent by Firebase
            String message = remoteMessage.getNotification().getBody();
             message += "It's time to go to " + restaurant + " with ";
            String message2 = createMessageNotification();
            // Show message
            this.sendVisualNotification(message, message2);
        }
    }

    /**
     * Create Notification
     *
     * @param messageBody message receive by firebase
     */
    private void sendVisualNotification(String messageBody, String message) {

        //Create an Intent that will be shown when user will click on the Notification
        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this.getApplicationContext(),
                                                        0, intent, PendingIntent.FLAG_ONE_SHOT);

        //Create a Style for the Notification
        NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
        inboxStyle.setBigContentTitle(getString(R.string.app_name));
        inboxStyle.addLine(messageBody).addLine(message);

        // Create a Channel (Android 8)
        String channelId = getString(R.string.default_notification_channel_id);

        //Build a Notification object
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(R.drawable.ic_bol)
                        .setContentTitle(getString(R.string.app_name))
                        .setContentText(getString(R.string.app_name))
                        .setAutoCancel(true)
                        .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                        .setContentIntent(pendingIntent)
                        .setStyle(inboxStyle);

        //Add the Notification to the Notification Manager and show it.
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Support Version >= Android 8
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence channelName = getResources().getString(R.string.message_from_go4lunch);
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel(channelId, channelName, importance);
            Objects.requireNonNull(notificationManager).createNotificationChannel(mChannel);
        }

        //Show notification
        int NOTIFICATION_ID = 7;
        String NOTIFICATION_TAG = "Go4lunch";
        Objects.requireNonNull(notificationManager).notify(NOTIFICATION_TAG, NOTIFICATION_ID, notificationBuilder.build());
    }

    /**
     * get restaurant choice
     */
    private void getRestaurantUser(){
        Query query = WorkersHelper.getAllWorkers().whereEqualTo("name",
                Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getDisplayName());
        query.get().addOnCompleteListener(task -> {
            restaurant = "";
            if (task.isSuccessful()){
                for (QueryDocumentSnapshot documentSnapshot : Objects.requireNonNull(task.getResult())){
                    restaurant = Objects.requireNonNull(documentSnapshot.get("restaurantName")).toString();
                }
            }
        });
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * get ArrayList of workers who are chosen the same restaurant
     * @return Workers ArrayList
     */
    private ArrayList<Workers> getRestaurantUserAndWorkersAdd(){
        Query query = WorkersHelper.getAllWorkers();
        ArrayList<Workers> workers = new ArrayList<>();
         query.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot data : Objects.requireNonNull(task.getResult())) {
                    if (Objects.requireNonNull(data.get("restaurantName")).toString().equals(restaurant)) {
                        Workers w = data.toObject(Workers.class);
                        workers.add(w);
                    }
                }
            }
        });
         return workers;
    }

    /**
     * Create a string with the workers name
     * @return String message
     */
    private String createMessageNotification(){
        StringBuilder message = new StringBuilder();
        for (int i = 0; i< mWorkers.size(); i++){
            if (!mWorkers.get(i).getName().
                    equals(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getDisplayName())){
                message.append(mWorkers.get(i).getName());
                if (i < mWorkers.size()-1){
                    message.append(" and ");
                }
            }

        }
        return message.toString();
    }
}
