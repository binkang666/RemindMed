package com.example.cecs491project.ui.map;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.cecs491project.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Map;
import java.util.Objects;


//TODO: button to recenter the user location
//TODO: show nearby pharmacy or hospital (probably need another array list to store the place) (toggle button to switch between them maybe)
//TODO: ability to show above store within 10 miles, 15 miles, 20 miles etc.
//TODO: show the map fragment at top of the screen and store nearby at the bot of the fragment.(ability to hide the place will be ideal but not required I think)

public class MapFragment extends Fragment {

    SupportMapFragment supportMapFragment;
    FusedLocationProviderClient client;

    public MapFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);

        supportMapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.google_map);

        client = LocationServices.getFusedLocationProviderClient(container.getContext());

        getCurrentLocation();
        return view;
    }

    private void getCurrentLocation() {

        if (ActivityCompat.checkSelfPermission(getContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

        } else {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
        }
        Task<Location> task = client.getLastLocation();

        task.addOnSuccessListener(location -> {
            if(location !=null){
                supportMapFragment.getMapAsync(googleMap -> {
                    LatLng latLng = new LatLng(location.getLatitude(),
                            location.getLongitude());
                    MarkerOptions options = new MarkerOptions().position(latLng).title("You are here");

                    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 14));
                    googleMap.addMarker(options);
                });
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == 44){
            if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                getCurrentLocation();
            }
        }
    }
}