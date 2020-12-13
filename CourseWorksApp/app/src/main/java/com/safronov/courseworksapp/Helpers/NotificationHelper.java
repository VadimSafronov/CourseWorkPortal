package com.safronov.courseworksapp.Helpers;

import android.app.Activity;
import android.app.DownloadManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;

import com.safronov.courseworksapp.R;

public class NotificationHelper {

    private Notification notification;
    private NotificationChannel channel;
    private NotificationManager notifManager;
    private NotificationCompat.Builder builder;

    private int notify_id = 0;              // id of notification
    private String defaultChannelId = "4";  // default channel id
    private String channelName = "4";       // default channel

    public NotificationHelper(Context context) {

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, getDownloadsIntent(((Activity) context)), 0);

        if (notifManager == null)
            notifManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            channel = notifManager.getNotificationChannel(defaultChannelId);
            if (channel == null) {
                channel = new NotificationChannel(defaultChannelId, channelName, importance);
                channel.enableVibration(true);
                channel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
                notifManager.createNotificationChannel(channel);
            }
            builder = new NotificationCompat.Builder(context, defaultChannelId);
            builder.setContentTitle("")
                    .setSmallIcon(R.drawable.baseline_cloud_download_24)
                    .setContentIntent(pendingIntent)
                    .setVibrate(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
        } else {
            builder = new NotificationCompat.Builder(context, defaultChannelId);
            builder.setContentTitle("")
                    .setSmallIcon(R.drawable.baseline_cloud_download_24)
                    .setContentIntent(pendingIntent)
                    .setVibrate(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400})
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        }
    }

    private static boolean isSamsung() {
        String manufacturer = Build.MANUFACTURER;
        if (manufacturer != null) return manufacturer.toLowerCase().equals("samsung");
        return false;
    }

    private static Intent getDownloadsIntent(@NonNull Activity activity) {
        if (isSamsung()) {
            Intent intent = activity.getPackageManager().getLaunchIntentForPackage("com.sec.android.app.myfiles");
            intent.setAction("samsung.myfiles.intent.action.LAUNCH_MY_FILES");
            intent.putExtra("samsung.myfiles.intent.extra.START_PATH", Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath());
            return intent;
        } else return new Intent(DownloadManager.ACTION_VIEW_DOWNLOADS);
    }

    public void createNotifiication() {
        builder.setContentTitle("Downloading...")
                .setAutoCancel(false)
                .setProgress(100, 0, true);
        notification = builder.build();
        notifManager.notify(notify_id, notification);
    }

    public void updateNotification(String message) {
        builder.setContentTitle(message)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setProgress(0, 0, false)
                .setContentText("Tap to go to the directory");
        notification = builder.build();
        notifManager.notify(notify_id, notification);
    }
}
