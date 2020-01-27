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

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Objects;

import fr.nelfdesign.go4lunch.R;
import fr.nelfdesign.go4lunch.ui.activity.MainActivity;

/**
 * Created by Nelfdesign at 21/01/2020
 * fr.nelfdesign.go4lunch.apiFirebase
 */
public class NotificationsServices extends FirebaseMessagingService {

    private static final String PREF_NOTIFICATION = "notification_firebase";

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        if (remoteMessage.getNotification() != null && sharedPreferences.getBoolean(PREF_NOTIFICATION, false)) {
            // 1 - Get message sent by Firebase
            String message = remoteMessage.getNotification().getBody();
            //2 - Show message in console
            this.sendVisualNotification(message);
        }
    }

    private void sendVisualNotification(String messageBody) {

        // 1 - Create an Intent that will be shown when user will click on the Notification
        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

        // 2 - Create a Style for the Notification
        NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
        inboxStyle.setBigContentTitle(getString(R.string.app_name));
        inboxStyle.addLine(messageBody);

        // 3 - Create a Channel (Android 8)
        String channelId = getString(R.string.default_notification_channel_id);

        // 4 - Build a Notification object
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(R.drawable.ic_bol)
                        .setContentTitle(getString(R.string.app_name))
                        .setContentText(getString(R.string.app_name))
                        .setAutoCancel(true)
                        .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                        .setContentIntent(pendingIntent)
                        .setStyle(inboxStyle);

        // 5 - Add the Notification to the Notification Manager and show it.
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // 6 - Support Version >= Android 8
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence channelName = getResources().getString(R.string.message_from_go4lunch);
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel(channelId, channelName, importance);
            Objects.requireNonNull(notificationManager).createNotificationChannel(mChannel);
        }

        // 7 - Show notification
        int NOTIFICATION_ID = 7;
        String NOTIFICATION_TAG = "Go4lunch";
        Objects.requireNonNull(notificationManager).notify(NOTIFICATION_TAG, NOTIFICATION_ID, notificationBuilder.build());
    }
}
