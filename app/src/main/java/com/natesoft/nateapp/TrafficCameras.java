package com.natesoft.nateapp;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public final class TrafficCameras extends AppCompatActivity {

    private String url = "https://web6.seattle.gov/Travelers/api/Map/Data?zoomId=13&type=2";
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager recyclerViewLayoutManager;
    private ArrayList<Camera> cameras;
    private RecyclerView.Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_traffic_cameras);
        cameras = new ArrayList<>();
        adapter = new CameraAdapter(getApplicationContext(), cameras);
        recyclerView = findViewById(R.id.cameraRecycler);
        recyclerViewLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(recyclerViewLayoutManager);
        recyclerView.setAdapter(adapter);

        if (NetworkCheck.checkNetworkConnection(this)) {
            getCameraData();
        } else {
            Toast toast = Toast.makeText(this, R.string.network_error, Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    private void getCameraData() {
        JsonObjectRequest jsonArrayRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
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
                    adapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Volley", error.toString());
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonArrayRequest);
    }

}