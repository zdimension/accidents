package fr.ihm.accidents;

import android.content.Intent;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.text.HtmlCompat;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class WitnessNoCallActivity extends AppCompatActivity implements LocationListener
{

    ToggleButton toggleButtonFirstAidGestures;
    ImageView firstAidGesturesImg;
    private ToggleButton toggleButtonInfosToGive;
    private TextView infosToGiveText;
    private Geocoder geocoder;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_witness_no_call);

        toggleButtonInfosToGive = findViewById(R.id.infosToGiveToggleWitnessNoCall);
        infosToGiveText = findViewById(R.id.infosToGiveTextWitnessNoCall);
        infosToGiveText.setText(Html.fromHtml("<h2>Où vous trouvez-vous ?</h2><p>Indiquez le lieu" +
            " le plus précisément " +
            "possible pour permettre aux secours de vous trouver rapidement (ville, rue, numéro, " +
            " etc.).</p>" +
            "<h2>Que se passe-t-il ?</h2><p>Indiquez la nature du problème (feu, malaise, " +
            "accident, etc.), " +
            "le nombre et l'état des victimes.</p>" +
            "<h2>Y a-t-il un risque que les choses s’aggravent ?</h2><p>Evoquez les risques " +
            "éventuels " +
            "d’incendie, d’explosion ou d’effondrement ;<br>" +
            "Répondez aux questions qui vous seront posées par la personne que vous aurez au " +
            "téléphone.</p>" +
            "<h3>Ne raccrochez jamais le premier !</h3><p>La personne qui a pris en charge votre " +
            "appel vous " +
            "dira quand elle a toutes les informations nécessaires. Donnez votre numéro de " +
            "téléphone et si possible, restez sur place, en sécurité, pour guider les secours" +
            ".</p>", HtmlCompat.FROM_HTML_MODE_LEGACY));
        toggleButtonInfosToGive.setOnClickListener(click ->
        {
            if (toggleButtonInfosToGive.isChecked())
            {
                toggleButtonInfosToGive.setChecked(true);
                infosToGiveText.setVisibility(View.VISIBLE);
            }
            else
            {
                toggleButtonInfosToGive.setChecked(false);
                infosToGiveText.setVisibility(View.GONE);
            }
        });

        geocoder = new Geocoder(this, Locale.getDefault());

        toggleButtonFirstAidGestures =
            findViewById(R.id.firsttAidGesturesToggleWitnessNoCall);
        firstAidGesturesImg = findViewById(R.id.firsttAidGesturesPngWitnessNoCall);
        toggleButtonFirstAidGestures.setOnClickListener(click ->
        {
            if (toggleButtonFirstAidGestures.isChecked())
            {
                toggleButtonFirstAidGestures.setChecked(true);
                firstAidGesturesImg.setVisibility(View.VISIBLE);
            }
            else
            {
                toggleButtonFirstAidGestures.setChecked(false);
                firstAidGesturesImg.setVisibility(View.GONE);
            }
        });

        final Button previous = findViewById(R.id.previousWitnessNoCall);
        previous.setBackgroundColor(Color.BLUE);
        previous.setTextColor(Color.WHITE);
        previous.setOnClickListener(v ->
        {
            Intent intentVictimeTemoin = new Intent(WitnessNoCallActivity.this,
                MoreDetails.class);
            startActivity(intentVictimeTemoin);
        });

        final Button cancel = findViewById(R.id.cancelWitnessNoCall);
        cancel.setBackgroundColor(Color.BLUE);
        cancel.setTextColor(Color.WHITE);
        cancel.setOnClickListener(v ->
        {
            Intent intentVictimeTemoin = new Intent(WitnessNoCallActivity.this,
                VictimeTemoin.class);
            startActivity(intentVictimeTemoin);
        });

        findViewById(R.id.rescueWitnessNoCall).setOnClickListener(click -> {
            startActivity(new Intent(Intent.ACTION_DIAL).setData(Uri.parse("tel:" + "15")));
        });
    }

    @Override
    protected void onResume()
    {
        super.onResume();

        DemarageAplication.checkPermissions(this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == DemarageAplication.PERMS_CALL_ID)
        {
            DemarageAplication.checkPermissions(this);
        }
    }

    @Override
    protected void onPause()
    {
        super.onPause();

        if (DemarageAplication.lm != null)
        {
            DemarageAplication.lm.removeUpdates(this);
        }
    }

    @Override
    public void onProviderEnabled(@NonNull String provider)
    {

    }

    @Override
    public void onProviderDisabled(@NonNull String provider)
    {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras)
    {

    }

    @Override
    public void onLocationChanged(@NonNull Location location)
    {
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
        ((TextView) findViewById(R.id.locationInfosWitnessNoCall)).setText("Longitude : " + ((location == null) ? "NaN" : location.getLongitude()) + "\nLatitude : " + ((location == null) ? "NaN" : location.getLatitude()) + ((addressStr.equals("")) ? "" : "\n" + addressStr));
    }
}