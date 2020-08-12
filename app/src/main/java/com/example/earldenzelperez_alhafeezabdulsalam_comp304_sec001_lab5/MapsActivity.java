package com.example.earldenzelperez_alhafeezabdulsalam_comp304_sec001_lab5;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    // API KEY --> AIzaSyDhkxivSb_0f_gTKE7_HvPsKySQCLsPAUI <-- API KEY //

    private GoogleMap mMap;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private final LatLng defaultLocation = new LatLng(43.6426, -79.3871);
    private static final int DEFAULT_ZOOM = 14;
    private Location lastKnownLocation;
    private Button btnWhere;
    private Button btnSearchSchools;
    private EditText searchText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        setContentView(R.layout.activity_maps);

        searchText = (EditText) findViewById(R.id.txtSearch);

        btnSearchSchools = (Button) findViewById(R.id.btnSearch);
        btnSearchSchools.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String place = searchText.getText().toString();
                if (place.isEmpty()){
                    Toast.makeText(MapsActivity.this, "Please input a place to search ", Toast.LENGTH_SHORT).show();
                }
                else{
                    showLocation(place);
                }
            }
        });

        btnWhere = (Button) findViewById(R.id.btnWhereAmI);
        btnWhere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDeviceLocation();
                Toast.makeText(MapsActivity.this, "This is where my device is located", Toast.LENGTH_SHORT).show();
            }
        });

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.maps_options, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Toast.makeText(this, "Selected Map Type: " + item.getTitle(), Toast.LENGTH_SHORT).show();
        switch (item.getItemId()) {
            case R.id.map_none:
                mMap.setMapType(GoogleMap.MAP_TYPE_NONE);
                return true;

            case R.id.map_normal:
                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                return true;

            case R.id.map_satellite:
                mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                return true;

            case R.id.map_terrain:
                mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                return true;

            case R.id.map_hybrid:
                mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        showDefaultLocation();
    }

    public void showLocation(String location){
        String loc  = location.toLowerCase();
        LatLng determinedLocation = new LatLng(0,0);
        boolean moved = false;
        if (loc.contains("centennial")){
            if (loc.contains("ashtonbee")){
                determinedLocation = new LatLng(43.7303, -79.2917);
                moved = true;
            }
            else if (loc.contains("story") && loc.contains("arts")){
                determinedLocation = new LatLng(43.7756, -79.2341);
                moved = true;
            }
            else if (loc.contains("morningside")){
                determinedLocation = new LatLng(43.7863, -79.1931);
                moved = true;
            }
            else{
                determinedLocation = new LatLng(43.7854, -79.2264);
                moved = true;
            }
        }
        else if (loc.contains("george") && loc.contains("brown")){
            determinedLocation = new LatLng(43.6761, -79.4105);
            moved = true;

        }
        else if (loc.contains("humber")){
            determinedLocation = new LatLng(43.7289, -79.6074);
            moved = true;

        }
        else if (loc.contains("seneca")){
            determinedLocation = new LatLng(43.7955, -79.3496);
            moved = true;

        }
        else if (loc.contains("ryerson")){
            determinedLocation = new LatLng(43.6577, -79.3788);
            moved = true;

        }
        else if (loc.contains("sheridan")){
            determinedLocation = new LatLng(43.4692, -79.6986);
            moved = true;
        }
        else if (loc.contains("mohawk")){
            determinedLocation = new LatLng(43.2387, -79.8881);
            moved = true;
        }

        if (moved){
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(determinedLocation, DEFAULT_ZOOM));
            Toast.makeText(MapsActivity.this, "Showing location that corresponds to " + location, Toast.LENGTH_SHORT).show();
            mMap.clear();
            mMap.addMarker(new MarkerOptions().position(determinedLocation).title(location));
        }
        else{
            Toast.makeText(MapsActivity.this, "Couldn't locate " + location, Toast.LENGTH_SHORT).show();
        }
    }

    public void getDeviceLocation() {
        try {
            fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
            Task<Location> locationResult = fusedLocationProviderClient.getLastLocation();
            locationResult.addOnCompleteListener(this, new OnCompleteListener<Location>() {
                @Override
                public void onComplete(@NonNull Task<Location> task) {
                    if (task.isSuccessful()) {
                        // Set the map's camera position to the current location of the device.
                        lastKnownLocation = task.getResult();
                        LatLng currentPos = new LatLng(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude());
                        if (lastKnownLocation != null) {
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentPos, DEFAULT_ZOOM));
                            mMap.clear();
                            mMap.addMarker(new MarkerOptions().position(currentPos).title("I am here"));
                        }
                    } else {
                    }
                }
            });
        } catch (SecurityException e)  {
        }
    }

    public void showDefaultLocation(){
        mMap.moveCamera(CameraUpdateFactory
                .newLatLngZoom(defaultLocation, DEFAULT_ZOOM));
        mMap.getUiSettings().setMyLocationButtonEnabled(false);
    }
}