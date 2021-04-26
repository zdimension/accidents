package com.marcelmarsaislacoste.accidents_wearos;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class Map extends FragmentActivity implements OnMapReadyCallback {
    private static final String TAG = "MARCEL_MapsActivity";
    private GoogleMap mMap;
    private MapFragment mapFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        Intent intentQuit = new Intent(getApplicationContext(), MainActivity.class);
        ((TextView)findViewById(R.id.quitGoogleMap)).setOnClickListener(click -> {
            startActivity(intentQuit);
        });

        mapFragment = (MapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        if (mapFragment == null) {
            mapFragment = new MapFragment();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.map, mapFragment);
            transaction.addToBackStack(null);
            transaction.commit();
        }

    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnCameraMoveListener((GoogleMap.OnCameraMoveListener) () -> {
            mapFragment.setCurrentLocation( mMap.getCameraPosition().target );
        });
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mapFragment.getPosition(),mapFragment.getZoom()));
    }
}