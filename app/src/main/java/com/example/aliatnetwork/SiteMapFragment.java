package com.example.aliatnetwork;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


public class SiteMapFragment extends Fragment{

    private GpsTracker gpsTracker;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // initialize view
        View view= inflater.inflate(R.layout.fragment_site_map, container, false);

        //initialize map fragment
        SupportMapFragment supportMapFragment = (SupportMapFragment)
                getChildFragmentManager().findFragmentById(R.id.google_map);

        //Async map
        supportMapFragment.getMapAsync(new OnMapReadyCallback() {

            @Override
            public void onMapReady(GoogleMap googleMap) {
                // get the latitude and longitude
                gpsTracker = new GpsTracker (getActivity ( ));
                double latitude = gpsTracker.getLatitude ( );
                double longitude = gpsTracker.getLongitude ( );
                LatLng location = new LatLng(latitude, longitude);


                //set position and title for marker
                googleMap.addMarker(new MarkerOptions()
                        .position(location)
                        .title(latitude+" : "+longitude));

                //animating to zoom the marker
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(location,10));


            }




        });

        return view;
    }


}


