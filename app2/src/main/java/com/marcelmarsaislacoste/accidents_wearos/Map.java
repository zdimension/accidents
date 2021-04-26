package com.marcelmarsaislacoste.accidents_wearos;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

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
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Locale;

import static com.marcelmarsaislacoste.accidents_wearos.Application.*;

public class Map extends FragmentActivity implements OnMapReadyCallback, TextToSpeech.OnInitListener, LocationListener
{
    private static final String TAG = "MARCEL_MapsActivity";
    private GoogleMap mMap;
    private MapFragment mapFragment;

    private PendingIntent pendingIntent;

    private TextToSpeech myTTS;
    private int MY_DATA_CHECK_CODE = 0;

    private LocationManager lm;

    private static final int PERMS_CALL_ID = 1234;

    /*private double lat1 = 37.422998333333335;
    private double lon1 = -122.08500000000002;*/
    // private LatLng oLatLng1 = new LatLng(37.422998333333335, -122.08500000000002);

    long time = System.currentTimeMillis();

    private int isBegin = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        Intent intentQuit = new Intent(getApplicationContext(), MainActivity.class);
        ((TextView) findViewById(R.id.quitGoogleMap)).setOnClickListener(click ->
        {
            startActivity(intentQuit);
        });

        mapFragment = (MapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        if (mapFragment == null)
        {
            mapFragment = new MapFragment();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.map, mapFragment);
            transaction.addToBackStack(null);
            transaction.commit();
        }

        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com"));
        pendingIntent = PendingIntent.getActivity(Map.this,0,intent,0);

        Intent checkTTSIntent = new Intent();
        checkTTSIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
        startActivityForResult(checkTTSIntent, MY_DATA_CHECK_CODE);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     */
    @Override
    @SuppressWarnings("MissingPermission")
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnCameraMoveListener((GoogleMap.OnCameraMoveListener) () ->
        {
            mapFragment.setCurrentLocation(mMap.getCameraPosition().target);
        });
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mapFragment.getPosition(), mapFragment.getZoom()));
        mMap.setMyLocationEnabled(true);
        /*mMap.addMarker(new MarkerOptions().position(new LatLng(43.72679, 7.11952))
                                            .title("Polytech"));*/
        // mMap.addMarker(new MarkerOptions().position(LAT_LNG1).icon(BitmapDescriptorFactory.fromResource(R.drawable.icons8cercle48)));
        mMap.addMarker(new MarkerOptions().position(LAT_LNG2).icon(BitmapDescriptorFactory.fromResource(R.drawable.warning_small)));
        mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.style_json));
    }

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
            Notification notification = new Notification.Builder(Map.this, id)
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
            Notification notification = new NotificationCompat.Builder(Map.this)
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
            Notification notification = new Notification.Builder(Map.this, id)
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
            Notification notification = new NotificationCompat.Builder(Map.this)
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
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == MY_DATA_CHECK_CODE)
        {
            if (resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS)
            {
                myTTS = new TextToSpeech(this, this);
            }
            else
            {
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