package com.example.taylorvskanye.Fragments;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.taylorvskanye.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.textview.MaterialTextView;

public class MapFragment extends Fragment{
    private MaterialTextView map_LBL_title;
    private GoogleMap mMap;

    public MapFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(@NonNull GoogleMap googleMap) {
                    mMap = googleMap;
                    // Add a default marker and move the camera
                    LatLng defaultLocation = new LatLng(	37.773972, 	-122.431297);
                    mMap.addMarker(new MarkerOptions().position(defaultLocation).title("Marker in Default Location"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(defaultLocation));
                }
            });
        }
        return view;
    }


    public void zoom(double lat, double lon) {
        if (mMap != null) {
            LatLng location = new LatLng(lat, lon);
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15));
            mMap.addMarker(new MarkerOptions().position(location).title("Selected Location"));
        }
    }

}