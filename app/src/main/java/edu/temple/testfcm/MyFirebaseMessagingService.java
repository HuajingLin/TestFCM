package edu.temple.testfcm;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import java.util.Random;

import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService {
    public MyFirebaseMessagingService() {
    }

    private static final String TAG = "FCMMessagingService =====>";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        String title = "";
        String content = "";
        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            title = remoteMessage.getData().get("title");
            content = remoteMessage.getData().get("message");
        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            title = remoteMessage.getNotification().getTitle();
            content = remoteMessage.getNotification().getBody();
        }

        Log.d(TAG, "From: " + remoteMessage.getFrom());
        Random random = new Random();
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        int notificationID = random.nextInt();
        String channel_id = "admin_channel";

        // you have to add this codes
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "New notification";
            String description = "Device to devie notification";


            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(channel_id, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this,channel_id);
        builder.setSmallIcon(R.drawable.ic_launcher_backgrounda);
        builder.setContentTitle(title);
        builder.setContentText(content);
        builder.setAutoCancel(true);
        builder.setPriority(NotificationCompat.PRIORITY_DEFAULT);
        builder.setContentIntent(pendingIntent);

        // Add as notification
        manager.notify(notificationID, builder.build());
    }
}