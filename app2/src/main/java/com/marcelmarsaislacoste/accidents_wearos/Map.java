package com.marcelmarsaislacoste.accidents_wearos;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

import android.app.PendingIntent;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.net.Uri;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;

import static com.marcelmarsaislacoste.accidents_wearos.Application.*;

public class Map extends FragmentActivity implements OnMapReadyCallback, TextToSpeech.OnInitListener, LocationListener
{
    private static final String TAG = "MARCEL_MapsActivity";
    private GoogleMap mMap;
    private MapFragment mapFragment;

    /*private double lat1 = 37.422998333333335;
    private double lon1 = -122.08500000000002;*/
    // private LatLng oLatLng1 = new LatLng(37.422998333333335, -122.08500000000002);

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        Intent intentQuit = new Intent(getApplicationContext(), MainActivity.class);
        findViewById(R.id.quitGoogleMap).setOnClickListener(click ->
            startActivity(intentQuit));

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
        PendingIntent pendingIntent = PendingIntent.getActivity(Map.this, 0, intent, 0);

        Intent checkTTSIntent = new Intent();
        checkTTSIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
        startActivityForResult(checkTTSIntent, Init.MY_DATA_CHECK_CODE);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     */
    @Override
    @SuppressWarnings("MissingPermission")
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnCameraMoveListener(() ->
            mapFragment.setCurrentLocation(mMap.getCameraPosition().target));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mapFragment.getPosition(), mapFragment.getZoom()));
        mMap.setMyLocationEnabled(true);
        /*mMap.addMarker(new MarkerOptions().position(new LatLng(43.72679, 7.11952))
                                            .title("Polytech"));*/
        // mMap.addMarker(new MarkerOptions().position(LAT_LNG1).icon(BitmapDescriptorFactory.fromResource(R.drawable.icons8cercle48)));
        // mMap.addMarker(new MarkerOptions().position(LAT_LNG2).icon(BitmapDescriptorFactory.fromResource(R.drawable.warning_small)));
        mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.style_json));
        for (JSONObject accident: Init.accidents) {
            try
            {
                mMap.addMarker(new MarkerOptions().position(new LatLng(accident.getDouble("latitude"), accident.getDouble("longitude"))).icon(BitmapDescriptorFactory.fromResource(R.drawable.warning_small)));
                // Toast.makeText(this, accident.getDouble("latitude") + "" + accident.getDouble("longitude") + "", Toast.LENGTH_SHORT).show();
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
        }
    }

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
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Init.MY_DATA_CHECK_CODE)
        {
            if (resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS)
            {
                Init.myTTS = new TextToSpeech(this, this);
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