package com.natesoft.nateapp;

import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.Task;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class TrafficCamMap extends AppCompatActivity implements OnMapReadyCallback {

    private static final String TAG = TrafficCamMap.class.getSimpleName();
    private FusedLocationProviderClient fusedLocationProviderClient;
    private final LatLng defaultLocation = new LatLng(47.60600, -122.33177);
    private static final int DEFAULT_ZOOM = 12;
    private GoogleMap googleMap;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private Location lastKnownLocation;
    private boolean locationPermissionGranted;
    private ArrayList<Camera> cameras;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_traffic_cam_map);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        cameras = new ArrayList<>();
        getCameraData();
    }

    private void showMarkers() {
        for (Camera cam : cameras) {
            Double[] pointCoordinates = cam.getCoordinates();
            double lat = pointCoordinates[0];
            double lng = pointCoordinates[1];
            googleMap.addMarker(new MarkerOptions()
                    .position(new LatLng(lat, lng))
                    .title(cam.getDescription())
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        this.googleMap = googleMap;
        getLocationPermission();
        updateLocationUI();
        getDeviceLocation();
    }

    private void getCameraData() {
        String url = "https://web6.seattle.gov/Travelers/api/Map/Data?zoomId=13&type=2";
        JsonObjectRequest jsonArrayRequest = new JsonObjectRequest(Request.Method.GET, url, null, response -> {
            try {
                JSONArray features = response.getJSONArray("Features");

                for (int i = 0; i < features.length(); i++) {
                    JSONObject point = features.getJSONObject(i);
                    JSONArray pointCoordinates = point.getJSONArray("PointCoordinate");
                    Double[] coordinates = new Double[2];

                    for (int j = 0; j < pointCoordinates.length(); j++) {
                        coordinates[j] = pointCoordinates.getDouble(j);
                    }
                    JSONArray multiple = point.getJSONArray("Cameras");

                    for (int k = 0; k < multiple.length(); k++) {
                        JSONObject c = multiple.getJSONObject(k);
                        Camera cam = new Camera();
                        cam.setType(c.getString("Type"));
                        cam.setDescription(c.getString("Description"));
                        if (cam.getType().equals("sdot")) {
                            cam.setImageUrl("https://www.seattle.gov/trafficcams/images/" + c.getString("ImageUrl"));
                        }
                        if (cam.getType().equals("wsdot")) {
                            cam.setImageUrl("https://images.wsdot.wa.gov/nw/" + c.getString("ImageUrl"));
                        }
                        cam.setCoordinates(coordinates);
                        cameras.add(cam);
                    }
                }
                showMarkers();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> Log.e("Volley", error.toString()));
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonArrayRequest);
    }

    private void getLocationPermission() {
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            locationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        locationPermissionGranted = false;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    locationPermissionGranted = true;
                }
            }
        }
        updateLocationUI();
    }

    private void updateLocationUI() {
        if (googleMap == null) {
            return;
        }
        try {
            if (locationPermissionGranted) {
                googleMap.setMyLocationEnabled(true);
                googleMap.getUiSettings().setMyLocationButtonEnabled(true);
            } else {
                googleMap.setMyLocationEnabled(false);
                googleMap.getUiSettings().setMyLocationButtonEnabled(false);
                lastKnownLocation = null;
                getLocationPermission();
            }
        } catch (SecurityException e) {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    private void getDeviceLocation() {
        try {
            if (locationPermissionGranted) {
                Task<Location> locationResult = fusedLocationProviderClient.getLastLocation();
                locationResult.addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        lastKnownLocation = task.getResult();
                        if (lastKnownLocation != null) {
                            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                    new LatLng(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude()), DEFAULT_ZOOM));
                            googleMap.addMarker(new MarkerOptions()
                                    .position(new LatLng(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude()))
                                    .title("Current Location: " + lastKnownLocation.getLatitude() + ", " + lastKnownLocation.getLongitude())
                                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
                        }
                    } else {
                        Log.d(TAG, "Current location is null. Using defaults.");
                        Log.e(TAG, "Exception: %s", task.getException());
                        googleMap.moveCamera(CameraUpdateFactory
                                .newLatLngZoom(defaultLocation, DEFAULT_ZOOM));
                        googleMap.getUiSettings().setMyLocationButtonEnabled(false);
                    }
                });
            }
        } catch (SecurityException e) {
            Log.e("Exception: %s", e.getMessage(), e);
        }
    }

}