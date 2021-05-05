package com.marcelmarsaislacoste.accidents_wearos;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;

import static com.marcelmarsaislacoste.accidents_wearos.Application.*;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MapFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MapFragment extends SupportMapFragment implements LocationListener
{

    private static final String TAG = "MARCEL_MapFragment";


    private LatLng currentLocation;
    private float zoomValue;

    // private Location location;

    public MapFragment() {}

    @Override
    /**
     * Call the method that creating callback after being attached to parent activity
     */
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // Create callback to parent activity
        try {
            OnMapReadyCallback mapActivity = (OnMapReadyCallback) getActivity();
            getMapAsync(mapActivity);
        } catch (ClassCastException e) {
            throw new ClassCastException(e.toString()+ " must implement mapActivity");
        }
    }


    @Override
    public void onStart() {
        super.onStart();
        // currentLocation = new LatLng(43.72679, 7.11952);
        // currentLocation = new LatLng(this.location.getLatitude(), this.location.getLongitude());
        if (Map.location == null)
            currentLocation = new LatLng(LAT_LNG1.latitude, LAT_LNG1.longitude);
        else
            currentLocation = new LatLng(Map.location.getLatitude(), Map.location.getLongitude());
        zoomValue = 12;
        Log.d(TAG,"onCreate()   -> currentLocation=("+currentLocation.latitude+","+currentLocation.longitude+")");
    }


    LatLng getPosition() {
        return currentLocation;
    }

    public void setCurrentLocation(LatLng currentLocation) {
        this.currentLocation = currentLocation;
    }

    float getZoom(){
        return zoomValue;
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
        // this.location = location;
    }
}
