package fr.ihm.accidents;

import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;

public class AccidentNature extends AppCompatActivity implements LocationListener
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accident_nature);

        Button restartButton = findViewById(R.id.restart);
        restartButton.setOnClickListener(v ->
        {
            Intent intent = new Intent(AccidentNature.this, VictimeTemoin.class);
            startActivity(intent);
        });

        Button previousButton = findViewById(R.id.previous);
        previousButton.setOnClickListener(v ->
        {
            Intent intent = new Intent(AccidentNature.this, HumainEnDanger.class);
            startActivity(intent);
        });

        Button vehicleButton = findViewById(R.id.vehicle_nature);
        System.out.println(vehicleButton.getMeasuredWidth() + " -  " + vehicleButton.getWidth());
        vehicleButton.setHeight(vehicleButton.getWidth());
        vehicleButton.setOnClickListener(v ->
        {
            Intent intent = new Intent(AccidentNature.this, VehicleNature.class);
            startActivity(intent);
        });

        Button natureButton = findViewById(R.id.nature_nature);
        natureButton.setOnClickListener(v ->
        {
            Intent intent = new Intent(AccidentNature.this, MoreDetails.class);
            startActivity(intent);
        });

        Button pedestrianButton = findViewById(R.id.pedestrian_nature);
        pedestrianButton.setOnClickListener(v ->
        {
            Intent intent = new Intent(AccidentNature.this, MoreDetails.class);
            startActivity(intent);
        });

        Button otherButton = findViewById(R.id.other_nature);
        otherButton.setOnClickListener(v ->
        {
            Intent intent = new Intent(AccidentNature.this, MoreDetails.class);
            startActivity(intent);
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