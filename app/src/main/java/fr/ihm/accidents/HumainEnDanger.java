package fr.ihm.accidents;

import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;

public class HumainEnDanger extends AppCompatActivity implements LocationListener
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_humain_en_danger);

        final TextView humainEnDangerTextView = findViewById(R.id.textView2);

        final Button button4 = findViewById(R.id.button4);
        button4.setBackgroundColor(Color.RED);
        button4.setTextColor(Color.BLACK);
        button4.setOnClickListener(v ->
        {
            Intent intentWitnessYesCallActivity = new Intent(HumainEnDanger.this,
                WitnessYesCallActivity.class);
            startActivity(intentWitnessYesCallActivity);
        });

        final Button button5 = findViewById(R.id.button5);
        button5.setBackgroundColor(Color.GREEN);
        button5.setTextColor(Color.BLACK);
        button5.setOnClickListener(v ->
        {
            Intent natureAccident = new Intent(HumainEnDanger.this, AccidentNature.class);
            HumainEnDanger.this.startActivity(natureAccident);
        });

        final Button button6 = findViewById(R.id.button6);
        button6.setBackgroundColor(Color.BLUE);
        button6.setTextColor(Color.WHITE);
        button6.setOnClickListener(v ->
        {
            Intent intentVictimeTemoin = new Intent(HumainEnDanger.this, VictimeTemoin.class);
            startActivity(intentVictimeTemoin);
        });
    }

    @Override
    protected void onResume()
    {
        super.onResume();

        DemarageAplication.checkPermissions(this);
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (DemarageAplication.lm != null) {
            DemarageAplication.lm.removeUpdates(this);
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
            DemarageAplication.locationChanged(this, location);
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
    }
}