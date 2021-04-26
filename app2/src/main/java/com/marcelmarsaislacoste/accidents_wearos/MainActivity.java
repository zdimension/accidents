package com.marcelmarsaislacoste.accidents_wearos;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.wearable.activity.WearableActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.app.NotificationCompat.WearableExtender;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;

import java.util.Locale;
import java.util.Objects;

import static com.marcelmarsaislacoste.accidents_wearos.Application.EXTRA_EVENT_ID;
import static com.marcelmarsaislacoste.accidents_wearos.Application.LAT_LNG1;
import static com.marcelmarsaislacoste.accidents_wearos.Application.LAT_LNG2;

// import static fr.ihm.accidents.DemarageAplication.*;

public class MainActivity extends WearableActivity implements TextToSpeech.OnInitListener, LocationListener
{

    private TextView mTextView;
    private PendingIntent pendingIntent;

    private TextToSpeech myTTS;
    private int MY_DATA_CHECK_CODE = 0;

    private LocationManager lm;

    private static final int PERMS_CALL_ID = 1234;

    // private LatLng oLatLng1 = new LatLng(37.422998333333335, -122.08500000000002);
    // private LatLng oLatLng2 = new LatLng(37.423998333333335, -122.08600000000002);

    long time = System.currentTimeMillis();

    private int isBegin = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextView = (TextView) findViewById(R.id.text);

        // Enables Always-on
        setAmbientEnabled();

        final Intent intentToGoogleMap = new Intent(MainActivity.this, Map.class);

        ((Button)findViewById(R.id.toGoogleMap)).setOnClickListener(click -> {
            startActivity(intentToGoogleMap);
        });

        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com"));
        pendingIntent = PendingIntent.getActivity(MainActivity.this,0,intent,0);

        /*
        Button speakButton = (Button)findViewById(R.id.speak);
        speakButton.setOnClickListener(this);*/

        Intent checkTTSIntent = new Intent();
        checkTTSIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
        startActivityForResult(checkTTSIntent, MY_DATA_CHECK_CODE);

        /*int notificationId = 001;
        // The channel ID of the notification.
        String id = "my_channel_01";
        // Build intent for notification content
        Intent viewIntent = new Intent(this, MainActivity.class);
        viewIntent.putExtra(EXTRA_EVENT_ID, id);
        PendingIntent viewPendingIntent =
            PendingIntent.getActivity(this, 0, viewIntent, 0);

        // Notification channel ID is ignored for Android 7.1.1
        // (API level 25) and lower.
        NotificationCompat.Builder notificationBuilder =
            new NotificationCompat.Builder(this, id)
                .setSmallIcon(R.drawable.ic_event)
                .setContentTitle("yes")
                .setContentText("ouahou")
                .setContentIntent(viewPendingIntent);

        // Get an instance of the NotificationManager service
        NotificationManagerCompat notificationManager =
            NotificationManagerCompat.from(this);

        // Issue the notification with notification manager.
        notificationManager.notify(notificationId, notificationBuilder.build());*/

    }

        /*Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Action action = new NotificationCompat.Action.Builder(
            R.mipmap.ic_launcher, getString(R.string.wearTitle), pendingIntent).build();

        createNotificationChannel();

        Notification notification = new NotificationCompat.Builder(getApplicationContext(), "001")
            .setContentText(getString(R.string.content))
            .setContentTitle(getString(R.string.title))
            .setSmallIcon(R.mipmap.ic_launcher)
            .extend(new NotificationCompat.WearableExtender().addAction(action))
            .build();

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(this);
        notificationManagerCompat.notify(001, notification);

       *//* int notificationId = 001;
        // The channel ID of the notification.
        String id = "channel 1";
        // Build intent for notification content
        Intent intentMainActivity = new Intent(this, MainActivity.class);
        boolean eventId = true;
        PendingIntent viewPendingIntent = PendingIntent.getActivity(this, 0, intentMainActivity, 0);

        CharSequence eventTitle = "test";
        CharSequence eventLocation = "ici";
        NotificationCompat.Builder notificationBuilder =
            new NotificationCompat.Builder(this, id)
                .setSmallIcon(R.drawable.ic_event)
                .setContentTitle(eventTitle)
                .setContentText(eventLocation)
                .setContentIntent(viewPendingIntent);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

        notificationManager.notify(notificationId, notificationBuilder.build());*//*
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.app_name);
            String description = getString(R.string.app_name);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("001", name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }*/

    public void notifyme(View view) {
        /*NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setContentTitle("Title on Page 1");
        builder.setContentText("Contents on Page 1");
        builder.setSmallIcon(R.drawable.ic_launcher_background);

        Notification notification = builder.build();

        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(this);
        managerCompat.notify(1, notification);*/

        NotificationManager manager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        if(Build.VERSION.SDK_INT >= 26)
        {
            //When sdk version is larger than26
            String id = "channel_1";
            String description = "143";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(id, description, importance);
            //                     channel.enableLights(true);
            //                     channel.enableVibration(true);
            manager.createNotificationChannel(channel);
            Notification notification = new Notification.Builder(MainActivity.this, id)
                .setCategory(Notification.CATEGORY_MESSAGE)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Attention !")
                .setContentText("Accident à 200 mètres.")
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .build();
            manager.notify(1, notification);
            try {
                Uri ring_uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), ring_uri);
                r.play();
                speakWords("Attention. Un accident à été signalé a une distance de 200m de vous");
            } catch (Exception e) {
                // Error playing sound
            }
        }
        else
        {
            //When sdk version is less than26
            Notification notification = new NotificationCompat.Builder(MainActivity.this)
                .setContentTitle("Attention !")
                .setContentText("Accident à 200 mètres.")
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.mipmap.ic_launcher)
                .build();
            manager.notify(1, notification);
            try {
                Uri ring_uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), ring_uri);
                r.play();
                speakWords("Attention. Un accident à été signalé a une distance de 200m de vous");
            } catch (Exception e) {
                // Error playing sound
            }
        }
    }

    public void notifyme(int importance, double distance) {
        /*NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setContentTitle("Title on Page 1");
        builder.setContentText("Contents on Page 1");
        builder.setSmallIcon(R.drawable.ic_launcher_background);

        Notification notification = builder.build();

        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(this);
        managerCompat.notify(1, notification);*/

        NotificationManager manager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        if(Build.VERSION.SDK_INT >= 26)
        {
            //When sdk version is larger than26
            String id = "channel_1";
            String description = "143";
            // int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(id, description, importance);
            //                     channel.enableLights(true);
            //                     channel.enableVibration(true);
            manager.createNotificationChannel(channel);
            Notification notification = new Notification.Builder(MainActivity.this, id)
                .setCategory(Notification.CATEGORY_MESSAGE)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Attention !")
                .setContentText("Accident à " + (int)Math.floor(distance) + " mètres.")
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .build();
            manager.notify(1, notification);
            try {
                Uri ring_uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), ring_uri);
                r.play();
                if (importance == NotificationManager.IMPORTANCE_LOW) {
                    speakWords("Un accident à été signalé a une distance de " + (int)Math.floor(distance) + "m de vous");
                } else if (importance == NotificationManager.IMPORTANCE_DEFAULT) {
                    speakWords("Redoublez de vigilance. Un accident à été signalé a une distance de " + (int)Math.floor(distance) + "m de vous");
                } else if (importance == NotificationManager.IMPORTANCE_HIGH) {
                    speakWords("Attention. Un accident à été signalé a une distance de " + (int)Math.floor(distance) + "m de vous");
                } else {
                    speakWords("Attention. Un accident à été signalé a une distance de " + (int)Math.floor(distance) + "m de vous");
                }
            } catch (Exception e) {
                // Error playing sound
            }
        }
        else
        {
            //When sdk version is less than26
            Notification notification = new NotificationCompat.Builder(MainActivity.this)
                .setContentTitle("Attention !")
                .setContentText("Accident à " + (int)Math.floor(distance) + " mètres.")
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.mipmap.ic_launcher)
                .build();
            manager.notify(1, notification);
            try {
                Uri ring_uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), ring_uri);
                r.play();
                if (importance == NotificationManager.IMPORTANCE_LOW) {
                    speakWords("Un accident à été signalé a une distance de " + (int)Math.floor(distance) + "m de vous");
                } else if (importance == NotificationManager.IMPORTANCE_DEFAULT) {
                    speakWords("Redoublez de vigilance. Un accident à été signalé a une distance de " + (int)Math.floor(distance) + "m de vous");
                } else if (importance == NotificationManager.IMPORTANCE_HIGH) {
                    speakWords("Attention. Un accident à été signalé a une distance de " + (int)Math.floor(distance) + "m de vous");
                } else {
                    speakWords("Attention. Un accident à été signalé a une distance de " + (int)Math.floor(distance) + "m de vous");
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

    private void speakWords(String speech) {
        // speaker
        myTTS.speak(speech, TextToSpeech.QUEUE_FLUSH, null);
    }

    @Override
    public void onInit(int initStatus) {
        if (initStatus == TextToSpeech.SUCCESS) {
            if(myTTS.isLanguageAvailable(Locale.FRENCH)==TextToSpeech.LANG_AVAILABLE)
                myTTS.setLanguage(Locale.FRENCH);
        }
        else if (initStatus == TextToSpeech.ERROR) {
            Toast.makeText(this, "Sorry! Text To Speech failed...", Toast.LENGTH_LONG).show();
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == MY_DATA_CHECK_CODE) {
            if (resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS) {
                myTTS = new TextToSpeech(this, this);
            }
            else {
                Intent installTTSIntent = new Intent();
                installTTSIntent.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
                startActivity(installTTSIntent);
            }
        }
    }

    @Override
    protected void onResume()
    {
        super.onResume();

        checkPermissions();
    }

    private void checkPermissions() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this, new String[] {
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            }, PERMS_CALL_ID);
            return;
        }

        lm = (LocationManager) getSystemService(LOCATION_SERVICE);

        if (lm.isProviderEnabled(LocationManager.GPS_PROVIDER))
        {
            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 0, this);
        }
        if (lm.isProviderEnabled(LocationManager.PASSIVE_PROVIDER)) {
            lm.requestLocationUpdates(LocationManager.PASSIVE_PROVIDER, 10000, 0, this);
        }
        if (lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 10000, 0, this);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMS_CALL_ID) {
            checkPermissions();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (lm != null) {
            lm.removeUpdates(this);
        }
    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {

    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();

        double distance = Math.acos(Math.sin(Math.toRadians(LAT_LNG1.latitude))*Math.sin(Math.toRadians(LAT_LNG2.latitude))+Math.cos(Math.toRadians(LAT_LNG1.latitude))*Math.cos(Math.toRadians(LAT_LNG2.latitude))*Math.cos(Math.toRadians(LAT_LNG2.longitude)-Math.toRadians(LAT_LNG1.longitude)))*6371*1000;

        Toast.makeText(this, "Location: " + latitude + "/" + longitude + ", distance : " + distance, Toast.LENGTH_LONG).show();

        if (isBegin == 1 || time + 200 * 60 < System.currentTimeMillis()) {
            if (distance < 1000) {
                notifyme(NotificationManager.IMPORTANCE_HIGH, distance);
            }
            else if (1000 <= distance && distance < 2000) {
                notifyme(NotificationManager.IMPORTANCE_DEFAULT, distance);
            }
            else if (2000 <= distance && distance < 3000) {
                notifyme(NotificationManager.IMPORTANCE_LOW, distance);
            }
            time = System.currentTimeMillis();
            isBegin = 0;
        }
    }
}