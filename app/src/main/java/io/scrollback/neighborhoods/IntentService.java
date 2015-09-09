package io.scrollback.neighborhoods;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import io.scrollback.library.ScrollbackIntentService;

public class IntentService extends ScrollbackIntentService {
    public static final int NOTIFICATION_ID = 1;

    @Override
    public void sendNotification(Notification n) {
        if (MainActivity.appOpen) {
            // Don't show notification when app is open
            return;
        }

        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        Log.d(Constants.TAG, "Sending notification");

        Intent i = new Intent(this, MainActivity.class);

        i.putExtra("scrollback_path", n.getPath());

        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, i, PendingIntent.FLAG_CANCEL_CURRENT);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.mipmap.ic_status)
                        .setColor(getResources().getColor(R.color.primary))
                        .setTicker(n.getTitle())
                        .setContentTitle(n.getTitle())
                        .setStyle(new NotificationCompat.BigTextStyle().bigText(n.getText()))
                        .setContentText(n.getText())
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                        .setAutoCancel(true);

        mBuilder.setContentIntent(contentIntent);

        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
    }
}

