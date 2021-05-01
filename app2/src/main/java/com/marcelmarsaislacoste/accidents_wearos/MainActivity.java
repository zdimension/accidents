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

    // private LatLng oLatLng1 = new LatLng(37.422998333333335, -122.08500000000002);
    // private LatLng oLatLng2 = new LatLng(37.423998333333335, -122.08600000000002);

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

        /* Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com"));
        pendingIntent = PendingIntent.getActivity(MainActivity.this,0,intent,0); */

        /*
        Button speakButton = (Button)findViewById(R.id.speak);
        speakButton.setOnClickListener(this);*/

        Intent checkTTSIntent = new Intent();
        checkTTSIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
        startActivityForResult(checkTTSIntent, Init.MY_DATA_CHECK_CODE);

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

    /*@Override
    public void onClick(View v) {
        //get the text entered
        String words = "hahahaha";
        speakWords(words);
    }*/

    @Override
    public void onInit(int initStatus) {
        if (initStatus == TextToSpeech.SUCCESS) {
            if(Init.myTTS.isLanguageAvailable(Locale.FRENCH)==TextToSpeech.LANG_AVAILABLE)
                Init.myTTS.setLanguage(Locale.FRENCH);
        }
        else if (initStatus == TextToSpeech.ERROR) {
            Toast.makeText(this, "Sorry! Text To Speech failed...", Toast.LENGTH_LONG).show();
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == Init.MY_DATA_CHECK_CODE) {
            if (resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS) {
                Init.myTTS = new TextToSpeech(this, this);
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

        Init.checkPermissions(this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == Init.PERMS_CALL_ID) {
            Init.checkPermissions(this);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (Init.lm != null) {
            Init.lm.removeUpdates(this);
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
        Init.locationChanged(this, location);
    }
}