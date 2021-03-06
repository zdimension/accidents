package fr.ihm.accidents;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import org.json.JSONObject;

import static fr.ihm.accidents.DemarageAplication.CHANNEL_ID_DEFAULT;
import static fr.ihm.accidents.DemarageAplication.CHANNEL_ID_HIGH;
import static fr.ihm.accidents.DemarageAplication.CHANNEL_ID_LOW;

public class NotificationHelper
{
    private static final int[] PRIORITIES = { NotificationCompat.PRIORITY_LOW,
        NotificationCompat.PRIORITY_DEFAULT, NotificationCompat.PRIORITY_HIGH };
    private static final String[] CHANNELS = { CHANNEL_ID_LOW, CHANNEL_ID_DEFAULT,
        CHANNEL_ID_HIGH };
    private static final String[] HINTS = { "proche", "près", "très proche" };
    private static final NotificationHelper instance = new NotificationHelper();
    private int notificationNumber = 0;
    private final String groupeNotification = "GroupeNotification";

    public static NotificationHelper getInstance()
    {
        return instance;
    }

    public void sendAccidentNotif(Context sender, double progress, String description,
                                  JSONObject accidentToPotentiallyRemove)
    {
        // int i;
        if (2000 <= progress && progress < 3000)
        {
            // i = 0;
            String title = "Un accident " + HINTS[0] + " de vous ! " + description;
            String text = "Un accident a été signalé a une distance de " + (int)progress +
                "m de vous";
            NotificationHelper.this.sendNotificationOnChannel(sender, title, text, CHANNELS[0], PRIORITIES[0]);
            if (accidentToPotentiallyRemove != null)
                DemarageAplication.accidentsNotications.remove(accidentToPotentiallyRemove);
        }
        else if (1000 <= progress && progress < 2000)
        {
            // i = 1;
            String title = "Un accident " + HINTS[1] + " de vous ! " + description;
            String text = "Un accident a été signalé a une distance de " + (int)progress + "m de vous";
            NotificationHelper.this.sendNotificationOnChannel(sender, title, text, CHANNELS[1], PRIORITIES[1]);
            if (accidentToPotentiallyRemove != null)
                DemarageAplication.accidentsNotications.remove(accidentToPotentiallyRemove);
        }
        else if (progress < 1000)
        {
            // i = 2;
            String title = "Un accident " + HINTS[2] + " de vous ! " + description;
            String text = "Un accident a été signalé a une distance de " + (int)progress + "m de vous";
            NotificationHelper.this.sendNotificationOnChannel(sender, title, text, CHANNELS[2], PRIORITIES[2]);
            if (accidentToPotentiallyRemove != null)
                DemarageAplication.accidentsNotications.remove(accidentToPotentiallyRemove);
        }
    }

    public void sendNotificationOnChannel(Context sender, String title, String description
        , String channelID, int priority)
    {
        Intent intent = new Intent(sender.getApplicationContext(), MapsActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(sender.getApplicationContext(), 0, intent, 0);
        NotificationCompat.Builder notification;
        if (priority == NotificationCompat.PRIORITY_HIGH)
        {
            notification = new NotificationCompat.Builder(sender.getApplicationContext(), channelID)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle(title)
                .setContentText(description)
                .setPriority(priority)
                .setContentIntent(pendingIntent);
        }
        else
        {
            notification = new NotificationCompat.Builder(sender.getApplicationContext(), channelID)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle(title)
                .setContentText(description)
                .setPriority(priority)
                .setGroup(NotificationHelper.this.groupeNotification)
                .setContentIntent(pendingIntent);
        }
        NotificationManagerCompat.from(sender).notify(NotificationHelper.this.notificationNumber++, notification.build());
    }
}
