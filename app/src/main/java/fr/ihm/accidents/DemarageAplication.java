package fr.ihm.accidents;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import java.util.Objects;

public class DemarageAplication extends Application {
    public static final String CHANNEL_ID_LOW = "channel 1";
    public static final String CHANNEL_ID_DEFAULT = "channel 2";
    public static final String CHANNEL_ID_HIGH = "channel 3";

    private void createNotificationChannels(){
        if (Build.VERSION.SDK_INT>= Build.VERSION_CODES.O)
        {
            NotificationChannel channelLow = new NotificationChannel(CHANNEL_ID_LOW, "Low important notification", NotificationManager.IMPORTANCE_LOW);
            channelLow.setDescription("Notification channel for notifications of low importance");

            NotificationChannel channelDefault = new NotificationChannel(CHANNEL_ID_DEFAULT, "Medium important notification", NotificationManager.IMPORTANCE_DEFAULT);
            channelDefault.setDescription("Notification channel for notifications of medium importance");

            NotificationChannel channelHigh = new NotificationChannel(CHANNEL_ID_HIGH, "High important notification", NotificationManager.IMPORTANCE_HIGH);
            channelHigh.setDescription("Notification channel for notifications of high importance");

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            Objects.requireNonNull(notificationManager).createNotificationChannel(channelLow);
            Objects.requireNonNull(notificationManager).createNotificationChannel(channelDefault);
            Objects.requireNonNull(notificationManager).createNotificationChannel(channelHigh);
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannels();
    }
}
