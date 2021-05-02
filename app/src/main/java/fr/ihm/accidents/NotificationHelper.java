package fr.ihm.accidents;

import android.content.Context;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import static fr.ihm.accidents.DemarageAplication.CHANNEL_ID_DEFAULT;
import static fr.ihm.accidents.DemarageAplication.CHANNEL_ID_HIGH;
import static fr.ihm.accidents.DemarageAplication.CHANNEL_ID_LOW;

public class NotificationHelper
{
    private static final int[] PRIORITIES = {NotificationCompat.PRIORITY_LOW, NotificationCompat.PRIORITY_DEFAULT, NotificationCompat.PRIORITY_HIGH};
    private static final String[] CHANNELS = {CHANNEL_ID_LOW, CHANNEL_ID_DEFAULT, CHANNEL_ID_HIGH};
    private static final String[] HINTS = {"proche", "près", "très proche"};

    public static void sendAccidentNotif(Context sender, int progress, String description)
    {
        int i ;
        if (progress >= 250)
            i = 0;
        else if (progress >= 100)
            i = 1;
        else
            i = 2;
        String title = "Un accident " + HINTS[i] + " de vous ! " + description;
        String text = "Un accident a été signalé a une distance de " + progress + "m de vous";
        sendNotificationOnChannel(sender, title, text, CHANNELS[i], PRIORITIES[i]);
    }

    private static int notificationNumber =0;
    private static String GroupeNotification = "GroupeNotification";

    public static void sendNotificationOnChannel(Context sender, String title, String description, String channelID, int priority){
        NotificationCompat.Builder notification;
        if(priority == NotificationCompat.PRIORITY_HIGH){
            notification = new NotificationCompat.Builder(sender.getApplicationContext(), channelID)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle(title)
                .setContentText(description)
                .setPriority(priority);
        }
        else{
            notification = new NotificationCompat.Builder(sender.getApplicationContext(), channelID)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle(title)
                .setContentText(description)
                .setPriority(priority)
                .setGroup(GroupeNotification);
        }
        NotificationManagerCompat.from(sender).notify(notificationNumber++ , notification.build());
    }
}
