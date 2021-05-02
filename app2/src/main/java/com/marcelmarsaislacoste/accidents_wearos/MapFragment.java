package com.marcelmarsaislacoste.accidents_wearos;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

import static com.marcelmarsaislacoste.accidents_wearos.Application.*;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MapFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MapFragment extends SupportMapFragment
{

    private static final String TAG = "FRED_MapFragment";


    private LatLng currentLocation;
    private float zoomValue;

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
        currentLocation = new LatLng(LAT_LNG1.latitude, LAT_LNG1.longitude);
        zoomValue = 15;
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

}
