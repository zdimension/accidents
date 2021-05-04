package fr.ihm.accidents;

import android.content.Context;

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
    private static int notificationNumber = 0;

    public static void sendAccidentNotif(Context sender, double progress, String description, JSONObject accidentToPotentiallyRemove)
    {
        // int i;
        if (2000 <= progress && progress < 3000)
        {
            // i = 0;
            String title = "Un accident " + HINTS[0] + " de vous ! " + description;
            String text = "Un accident a été signalé a une distance de " + progress + "m de vous";
            sendNotificationOnChannel(sender, title, text, CHANNELS[0], PRIORITIES[0]);
            if (accidentToPotentiallyRemove != null)
                DemarageAplication.accidentsNotications.remove(accidentToPotentiallyRemove);
        }
        else if (1000 <= progress && progress < 2000)
        {
            // i = 1;
            String title = "Un accident " + HINTS[1] + " de vous ! " + description;
            String text = "Un accident a été signalé a une distance de " + progress + "m de vous";
            sendNotificationOnChannel(sender, title, text, CHANNELS[1], PRIORITIES[1]);
            if (accidentToPotentiallyRemove != null)
                DemarageAplication.accidentsNotications.remove(accidentToPotentiallyRemove);
        }
        else if (progress < 1000)
        {
            // i = 2;
            String title = "Un accident " + HINTS[2] + " de vous ! " + description;
            String text = "Un accident a été signalé a une distance de " + progress + "m de vous";
            sendNotificationOnChannel(sender, title, text, CHANNELS[2], PRIORITIES[2]);
            if (accidentToPotentiallyRemove != null)
                DemarageAplication.accidentsNotications.remove(accidentToPotentiallyRemove);
        }
    }

    public static void sendNotificationOnChannel(Context sender, String title, String description
        , String channelID, int priority)
    {
        NotificationCompat.Builder notification;
        if (priority == NotificationCompat.PRIORITY_HIGH)
        {
            notification = new NotificationCompat.Builder(sender.getApplicationContext(), channelID)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle(title)
                .setContentText(description)
                .setPriority(priority);
        }
        else
        {
            String groupeNotification = "GroupeNotification";
            notification = new NotificationCompat.Builder(sender.getApplicationContext(), channelID)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle(title)
                .setContentText(description)
                .setPriority(priority)
                .setGroup(groupeNotification);
        }
        NotificationManagerCompat.from(sender).notify(notificationNumber++, notification.build());
    }
}
