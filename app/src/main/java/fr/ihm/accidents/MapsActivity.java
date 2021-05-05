package fr.ihm.accidents;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, LocationListener, Observer
{

    private GoogleMap mMap;
    private LocationManager locManager;
    private AccidentModel model;
    private AccidentController controller;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        this.locManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        this.model = new AccidentModel();
        this.controller = new AccidentController(this.model);
        this.controller.addObserver(this);
        if (!this.locManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
        {
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            this.startActivity(intent);
        }
        else
        {
            mapFragment.getMapAsync(this);
        }
        Button refresh = this.findViewById(R.id.refresh);
        refresh.setOnClickListener(v -> this.controller.refresh());
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap)
    {
        mMap = googleMap;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            return;
        }

        if (this.locManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
        {
            this.locManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
            FusedLocationProviderClient fusedLocationClient =
                LocationServices.getFusedLocationProviderClient(this);
            fusedLocationClient.getLastLocation().addOnSuccessListener(location ->
            {
                if (location != null)
                {
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 15));
                    mMap.animateCamera(CameraUpdateFactory.zoomTo(10), 2000, null);
                }
            });
        }
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);
        for (Accident accident: this.model.getAccidents()) {
            mMap.addMarker(new MarkerOptions().position(new LatLng(accident.getLatitude(), accident.getLongitude())).icon(BitmapDescriptorFactory.fromResource(R.drawable.warning_small)));
        }
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        Log.i("GoogleMapsActivity", "Location changed");
        try {
            DemarageAplication.locationChanged(this, location);
        } catch (JSONException e) {
            e.printStackTrace();
        }
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
    public void update(Observable o, Object arg) {
        if(arg instanceof List) {
            List<Accident> accidents = (List<Accident>) arg;
            for (Accident accident : accidents)
                mMap.addMarker(new MarkerOptions()
                        .position(new LatLng(accident.getLatitude(), accident.getLongitude()))
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.warning_small)));
        }
    }
}