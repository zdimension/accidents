package fr.ihm.accidents;

import android.Manifest;
import android.app.Activity;
import android.app.Application;
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
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;
import java.util.UUID;

public class DemarageAplication extends Application
{
    public static final String CHANNEL_ID_LOW = "channel 1";
    public static final String CHANNEL_ID_DEFAULT = "channel 2";
    public static final String CHANNEL_ID_HIGH = "channel 3";
    public static final int PERMS_CALL_ID = 1234;
    public static int MY_DATA_CHECK_CODE = 0;
    public static LocationManager lm;
    public static long time = System.currentTimeMillis();

    public static int isBegin = 1;

    public static PendingIntent pendingIntent;

    public static ArrayList<JSONObject> accidentsNotications;

    public static final String ANDROID_ID = UUID.randomUUID().toString();

    public static void checkPermissions(Activity a)
    {
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
        if (lm.isProviderEnabled(LocationManager.PASSIVE_PROVIDER))
        {
            lm.requestLocationUpdates(LocationManager.PASSIVE_PROVIDER, 10000, 0,
                (LocationListener) a);
        }
        if (lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER))
        {
            lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 10000, 0, (LocationListener) a);
        }
    }

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
        accidentsNotications = new ArrayList<>();
    }

    public static void locationChanged(Activity a, Location location) throws JSONException
    {
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();

        // ArrayList<Double> distances = new ArrayList<>();
        double distance = 3000;
        JSONObject accidentToPotentiallyRemove = null;

        for (JSONObject accident: DemarageAplication.accidentsNotications)
        {
            // double distance = Math.acos(Math.sin(Math.toRadians(LAT_LNG1.latitude)) * Math.sin(Math.toRadians(LAT_LNG2.latitude)) + Math.cos(Math.toRadians(LAT_LNG1.latitude)) * Math.cos(Math.toRadians(LAT_LNG2.latitude)) * Math.cos(Math.toRadians(LAT_LNG2.longitude) - Math.toRadians(LAT_LNG1.longitude))) * 6371 * 1000;
            double temp = Math.acos(Math.sin(Math.toRadians(latitude)) * Math.sin(Math.toRadians(accident.getDouble("latitude"))) + Math.cos(Math.toRadians(latitude)) * Math.cos(Math.toRadians(accident.getDouble("latitude"))) * Math.cos(Math.toRadians(accident.getDouble("longitude")) - Math.toRadians(longitude))) * 6371 * 1000;
            if (temp < 3000 && temp < distance)
            {
                distance = temp;
                accidentToPotentiallyRemove = accident;
            }
        }

        if (accidentToPotentiallyRemove != null)
            NotificationHelper.sendAccidentNotif(a, distance, /*"pas d'informations"*/"", accidentToPotentiallyRemove);
    }
}
