package com.marcelmarsaislacoste.accidents_wearos;

import android.Manifest;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;

import static com.marcelmarsaislacoste.accidents_wearos.Application.LAT_LNG1;
import static com.marcelmarsaislacoste.accidents_wearos.Application.LAT_LNG2;

// import static fr.ihm.accidents.DemarageAplication.*;

import android.app.Application;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;
import java.util.UUID;

public class Init extends Application {
    public static PendingIntent pendingIntent;

    public static  TextToSpeech myTTS;
    public static final int MY_DATA_CHECK_CODE = 0;

    public static LocationManager lm;

    public static final String CHANNEL_ID_LOW = "channel 1";
    public static final String CHANNEL_ID_DEFAULT = "channel 2";
    public static final String CHANNEL_ID_HIGH = "channel 3";
    public static final int PERMS_CALL_ID = 1234;

    // private LatLng oLatLng1 = new LatLng(37.422998333333335, -122.08500000000002);
    // private LatLng oLatLng2 = new LatLng(37.423998333333335, -122.08600000000002);

    public static long time = System.currentTimeMillis();

    public static int isBegin = 1;

    public static boolean isTts = false;

    public static ArrayList<JSONObject> accidents;
    public static ArrayList<JSONObject> accidentsNotications;

    public static final String ANDROID_ID = UUID.randomUUID().toString();

    private void createNotificationChannels()
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            NotificationChannel channelLow = new NotificationChannel(CHANNEL_ID_LOW, "Low " +
                "important notification", NotificationManager.IMPORTANCE_LOW);
            channelLow.setDescription("Notification channel for notifications of low importance");

            NotificationChannel channelDefault = new NotificationChannel(CHANNEL_ID_DEFAULT,
                "Medium important notification", NotificationManager.IMPORTANCE_DEFAULT);
            channelDefault.setDescription("Notification channel for notifications of medium " +
                "importance");

            NotificationChannel channelHigh = new NotificationChannel(CHANNEL_ID_HIGH, "High " +
                "important notification", NotificationManager.IMPORTANCE_HIGH);
            channelHigh.setDescription("Notification channel for notifications of high importance");

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            Objects.requireNonNull(notificationManager).createNotificationChannel(channelLow);
            Objects.requireNonNull(notificationManager).createNotificationChannel(channelDefault);
            Objects.requireNonNull(notificationManager).createNotificationChannel(channelHigh);
        }
    }

    @Override
    public void onCreate()
    {
        super.onCreate();
        createNotificationChannels();
        accidents = new ArrayList<>();
        accidentsNotications = new ArrayList<>();
    }

    public static void notifyme(View view, Activity a) {
        /*NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setContentTitle("Title on Page 1");
        builder.setContentText("Contents on Page 1");
        builder.setSmallIcon(R.drawable.ic_launcher_background);

        Notification notification = builder.build();

        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(this);
        managerCompat.notify(1, notification);*/

        NotificationManager manager = (NotificationManager)a.getSystemService(NOTIFICATION_SERVICE);
        if(Build.VERSION.SDK_INT >= 26)
        {
            //When sdk version is larger than26
            String id = "channel_1";
            String description = "143";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID_HIGH, description, importance);
            //                     channel.enableLights(true);
            //                     channel.enableVibration(true);
            manager.createNotificationChannel(channel);
            Notification notification = new Notification.Builder(a, CHANNEL_ID_HIGH)
                .setCategory(Notification.CATEGORY_MESSAGE)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Attention !")
                .setContentText("Accident ?? 200 m??tres.")
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .build();
            manager.notify(1, notification);
            try {
                Uri ring_uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                Ringtone r = RingtoneManager.getRingtone(a.getApplicationContext(), ring_uri);
                r.play();
                Init.speakWords("Attention. Un accident ?? ??t?? signal?? a une distance de 200m de vous");
            } catch (Exception e) {
                // Error playing sound
            }
        }
        else
        {
            //When sdk version is less than26
            Notification notification = new NotificationCompat.Builder(a, CHANNEL_ID_HIGH)
                .setContentTitle("Attention !")
                .setContentText("Accident ?? 200 m??tres.")
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.mipmap.ic_launcher)
                .build();
            manager.notify(1, notification);
            try {
                Uri ring_uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                Ringtone r = RingtoneManager.getRingtone(a.getApplicationContext(), ring_uri);
                r.play();
                Init.speakWords("Attention. Un accident ?? ??t?? signal?? a une distance de 200m de vous");
            } catch (Exception e) {
                // Error playing sound
            }
        }
    }

    public static void notifyme(int importance, double distance, Activity a) {
        /*NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setContentTitle("Title on Page 1");
        builder.setContentText("Contents on Page 1");
        builder.setSmallIcon(R.drawable.ic_launcher_background);

        Notification notification = builder.build();

        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(this);
        managerCompat.notify(1, notification);*/

        NotificationManager manager = (NotificationManager)a.getSystemService(NOTIFICATION_SERVICE);
        if(Build.VERSION.SDK_INT >= 26)
        {
            //When sdk version is larger than26
            String id = "channel_1";
            String description = "143";
            // int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID_HIGH, description, importance);
            //                     channel.enableLights(true);
            //                     channel.enableVibration(true);
            manager.createNotificationChannel(channel);
            Notification notification = new Notification.Builder(a, CHANNEL_ID_HIGH)
                .setCategory(Notification.CATEGORY_MESSAGE)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Attention !")
                .setContentText("Accident ?? " + (int)Math.floor(distance) + " m??tres.")
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .build();
            manager.notify(1, notification);
            try {
                Uri ring_uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                Ringtone r = RingtoneManager.getRingtone(a.getApplicationContext(), ring_uri);
                r.play();
                if (importance == NotificationManager.IMPORTANCE_LOW) {
                    Init.speakWords("Un accident ?? ??t?? signal?? a une distance de " + (int)Math.floor(distance) + "m de vous");
                } else if (importance == NotificationManager.IMPORTANCE_DEFAULT) {
                    Init.speakWords("Redoublez de vigilance. Un accident ?? ??t?? signal?? a une distance de " + (int)Math.floor(distance) + "m de vous");
                } else if (importance == NotificationManager.IMPORTANCE_HIGH) {
                    Init.speakWords("Attention. Un accident ?? ??t?? signal?? a une distance de " + (int)Math.floor(distance) + "m de vous");
                } else {
                    Init.speakWords("Attention. Un accident ?? ??t?? signal?? a une distance de " + (int)Math.floor(distance) + "m de vous");
                }
            } catch (Exception e) {
                // Error playing sound
            }
        }
        else
        {
            //When sdk version is less than26
            Notification notification = new NotificationCompat.Builder(a, CHANNEL_ID_HIGH)
                .setContentTitle("Attention !")
                .setContentText("Accident ?? " + (int)Math.floor(distance) + " m??tres.")
                .setContentIntent(Init.pendingIntent)
                .setSmallIcon(R.mipmap.ic_launcher)
                .build();
            manager.notify(1, notification);
            try {
                Uri ring_uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                Ringtone r = RingtoneManager.getRingtone(a.getApplicationContext(), ring_uri);
                r.play();
                if (importance == NotificationManager.IMPORTANCE_LOW) {
                    Init.speakWords("Un accident ?? ??t?? signal?? a une distance de " + (int)Math.floor(distance) + "m de vous");
                } else if (importance == NotificationManager.IMPORTANCE_DEFAULT) {
                    Init.speakWords("Redoublez de vigilance. Un accident ?? ??t?? signal?? a une distance de " + (int)Math.floor(distance) + "m de vous");
                } else if (importance == NotificationManager.IMPORTANCE_HIGH) {
                    Init.speakWords("Attention. Un accident ?? ??t?? signal?? a une distance de " + (int)Math.floor(distance) + "m de vous");
                } else {
                    Init.speakWords("Attention. Un accident ?? ??t?? signal?? a une distance de " + (int)Math.floor(distance) + "m de vous");
                }
            } catch (Exception e) {
                // Error playing sound
            }
        }
    }

    /*@Override
    public void onClick(View v) {
        //get the text entered
        String words = "hahahaha";
        speakWords(words);
    }*/

    public static void speakWords(String speech) {
        // speaker
        if (isTts)
            Init.myTTS.speak(speech, TextToSpeech.QUEUE_FLUSH, null);
    }

    public static void checkPermissions(Activity a) {
        if (ActivityCompat.checkSelfPermission(a, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(a, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(a, new String[] {
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            }, PERMS_CALL_ID);
            return;
        }

        lm = (LocationManager) a.getSystemService(LOCATION_SERVICE);

        if (lm.isProviderEnabled(LocationManager.GPS_PROVIDER))
        {
            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 0, (LocationListener) a);
        }
        if (lm.isProviderEnabled(LocationManager.PASSIVE_PROVIDER)) {
            lm.requestLocationUpdates(LocationManager.PASSIVE_PROVIDER, 10000, 0, (LocationListener) a);
        }
        if (lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 10000, 0, (LocationListener) a);
        }
    }

    public static void locationChanged(Activity a, Location location) throws JSONException
    {
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();

        // ArrayList<Double> distances = new ArrayList<>();
        double distance = 3000;
        JSONObject accidentToPotentiallyRemove = null;

        for (JSONObject accident: Init.accidentsNotications)
        {
            // double distance = Math.acos(Math.sin(Math.toRadians(LAT_LNG1.latitude)) * Math.sin(Math.toRadians(LAT_LNG2.latitude)) + Math.cos(Math.toRadians(LAT_LNG1.latitude)) * Math.cos(Math.toRadians(LAT_LNG2.latitude)) * Math.cos(Math.toRadians(LAT_LNG2.longitude) - Math.toRadians(LAT_LNG1.longitude))) * 6371 * 1000;
            double temp = Math.acos(Math.sin(Math.toRadians(latitude)) * Math.sin(Math.toRadians(accident.getDouble("latitude"))) + Math.cos(Math.toRadians(latitude)) * Math.cos(Math.toRadians(accident.getDouble("latitude"))) * Math.cos(Math.toRadians(accident.getDouble("longitude")) - Math.toRadians(longitude))) * 6371 * 1000;
            if (temp < 3000 && temp < distance)
            {
                distance = temp;
                accidentToPotentiallyRemove = accident;
            }
        }

        // if (Init.isBegin == 1 || Init.time + 200 * 60 < System.currentTimeMillis()) {
        if (distance < 1000) {
            // Toast.makeText(a, "Location: " + latitude + "/" + longitude + ", distance : " + distance, Toast.LENGTH_LONG).show();
            Init.notifyme(NotificationManager.IMPORTANCE_HIGH, distance, a);
            Init.accidentsNotications.remove(accidentToPotentiallyRemove);
        }
        else if (1000 <= distance && distance < 2000) {
            // Toast.makeText(a, "Location: " + latitude + "/" + longitude + ", distance : " + distance, Toast.LENGTH_LONG).show();
            Init.notifyme(NotificationManager.IMPORTANCE_DEFAULT, distance, a);
            Init.accidentsNotications.remove(accidentToPotentiallyRemove);
        }
        else if (2000 <= distance && distance < 3000) {
            // Toast.makeText(a, "Location: " + latitude + "/" + longitude + ", distance : " + distance, Toast.LENGTH_LONG).show();
            Init.notifyme(NotificationManager.IMPORTANCE_LOW, distance, a);
            Init.accidentsNotications.remove(accidentToPotentiallyRemove);
        }
        // Init.time = System.currentTimeMillis();
        // Init.isBegin = 0;
        // }
    }
}