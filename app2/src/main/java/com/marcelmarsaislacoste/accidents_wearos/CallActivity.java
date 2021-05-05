package com.marcelmarsaislacoste.accidents_wearos;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.net.Uri;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.wearable.activity.WearableActivity;
import android.telecom.Call;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CallActivity extends WearableActivity implements TextToSpeech.OnInitListener, LocationListener
{

    private TextView mTextView;

    private Geocoder geocoder;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call);

        mTextView = (TextView) findViewById(R.id.text);

        // Enables Always-on
        setAmbientEnabled();

        Intent intentQuit = new Intent(getApplicationContext(), MainActivity.class);
        findViewById(R.id.quitCall).setOnClickListener(click ->
            startActivity(intentQuit));

        geocoder = new Geocoder(this, Locale.getDefault());

        findViewById(R.id.call).setOnClickListener(click -> {
            /*Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:" + "5550123"));
            startActivity(callIntent);*/
            startActivity(new Intent(Intent.ACTION_DIAL).setData(Uri.parse("tel:" + "15")));
        });
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
        try
        {
            Init.locationChanged(this, location);
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        List<Address> adresses = null;
        String addressStr = "";
        try
        {
            adresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
        }
        catch (IOException ioException)
        {
            Log.e("GPS", "erreur", ioException);
        }
        catch (IllegalArgumentException illegalArgumentException)
        {
            String coordonnees = String.format("Latitude : %f - Longitude : %f\n",
                location.getLatitude(), location.getLongitude());
            Log.e("GPS", "Error: " + coordonnees, illegalArgumentException);
        }

        if (adresses == null || adresses.size() == 0)
        {
            Log.e("GPS", "Error: no address!!");
        }
        else
        {
            Address adresse = adresses.get(0);
            ArrayList<String> addressFragments = new ArrayList<>();

            String strAdresse = adresse.getAddressLine(0) + ", " + adresse.getLocality();
            Log.d("GPS", "address : " + strAdresse);

            for (int i = 0; i <= adresse.getMaxAddressLineIndex(); i++)
            {
                addressFragments.add(adresse.getAddressLine(i));
            }
            Log.d("GPS", TextUtils.join(System.getProperty("line.separator"), addressFragments));
            addressStr = TextUtils.join(System.getProperty("line.separator"), addressFragments);
        }
        ((TextView) findViewById(R.id.locationInfos)).setText("Longitude : " + ((location == null) ? "NaN" : location.getLongitude()) + "\nLatitude : " + ((location == null) ? "NaN" : location.getLatitude()) + ((addressStr.equals("")) ? "" : "\n" + addressStr));
    }
}