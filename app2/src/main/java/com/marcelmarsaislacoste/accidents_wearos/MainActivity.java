package com.marcelmarsaislacoste.accidents_wearos;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.wearable.activity.WearableActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.Locale;

// import static fr.ihm.accidents.DemarageAplication.*;

public class MainActivity extends WearableActivity implements TextToSpeech.OnInitListener, LocationListener
{

    private PendingIntent pendingIntent;

    // private LatLng oLatLng1 = new LatLng(37.422998333333335, -122.08500000000002);
    // private LatLng oLatLng2 = new LatLng(37.423998333333335, -122.08600000000002);

    private Location location;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView mTextView = findViewById(R.id.text);

        // Enables Always-on
        setAmbientEnabled();

        final Intent intentToGoogleMap = new Intent(MainActivity.this, Map.class);

        findViewById(R.id.toGoogleMap).setOnClickListener(click -> startActivity(intentToGoogleMap));

        /* Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com"));
        pendingIntent = PendingIntent.getActivity(MainActivity.this,0,intent,0); */

        /*
        Button speakButton = (Button)findViewById(R.id.speak);
        speakButton.setOnClickListener(this);*/

        Intent checkTTSIntent = new Intent();
        checkTTSIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
        startActivityForResult(checkTTSIntent, Init.MY_DATA_CHECK_CODE);

        // findViewById(R.id.notif).setOnClickListener(click -> Init.notifyme(((Button)findViewById(R.id.notif)), this));

        findViewById(R.id.report).setOnClickListener(click -> {

            Intent intentCallActivity = new Intent(MainActivity.this, CallActivity.class);


            @SuppressLint("StaticFieldLeak") AsyncTask<Object, Object, Object> task = new AsyncTask<Object, Object, Object>()
            {
                @Override
                protected Object doInBackground(Object[] objects)
                {
                    BufferedReader reader = null;
                    String text = "";

                    // Send data
                    try
                    {
                        // Defined URL  where to send data
                        URL url = new URL("https://domino.zdimension.fr/web/ihm.php");

                        // Send POST data request

                        URLConnection conn = url.openConnection();
                        conn.setDoOutput(true);
                        // OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
                        BufferedWriter wr = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
                        wr.write("accident=0&distance=50&longitude=" + location.getLongitude() + "&latitude=" + location.getLatitude());
                        wr.flush();

                        // Get the server response

                        reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                        StringBuilder sb = new StringBuilder();
                        String line = null;

                        // Read Server Response
                        while((line = reader.readLine()) != null)
                        {
                            // Append server response in string
                            sb.append(line + "\n");
                        }
                        text = sb.toString();
                    }
                    catch(Exception ex)
                    {
                        ex.printStackTrace();
                    }
                    finally
                    {
                        try
                        {
                            reader.close();
                        }
                        catch(Exception ex) {}
                    }
                    return null;
                }
            };

            task.execute();

            // Show response on activity
            // Toast.makeText(this, text, Toast.LENGTH_SHORT);
            startActivity(intentCallActivity);
        });

        ToggleButton toggleTts = (ToggleButton)findViewById(R.id.tts);
        toggleTts.setOnClickListener(click ->
        {
            if (toggleTts.isChecked())
            {
                toggleTts.setChecked(true);
                // toggleTts.setBackgroundColor(Color.argb(0, 0, 255, 0));
                Init.isTts = true;
            }
            else
            {
                toggleTts.setChecked(false);
                // toggleTts.setBackgroundColor(Color.argb(0, 255, 0, 0));
                Init.isTts = false;
            }
        });

        startForegroundService(new Intent(this, WebService.class));

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
        this.location = location;
        try
        {
            Init.locationChanged(this, location);
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
    }
}