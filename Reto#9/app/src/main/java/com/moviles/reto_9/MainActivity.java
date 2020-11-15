package com.moviles.reto_9;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends FragmentActivity implements OnMapReadyCallback {

    private int radius;
    private int type;
    private String title;
    private GoogleMap mMap;
    private EditText etSearch;
    private double latitude, longitude;
    private SharedPreferences sharedPreferences;
    private TextView mysearch;
    private int MY_PERMISSIONS_REQUEST_READ_CONTACTS;

    public static final ArrayList<String> places_type = new ArrayList<String>() {
        {
            add("ATM");
            add("Bakery");
            add("Bank");
            add("Bar");
            add("Cafe");
            add("Casino");
            add("Church");
            add("Gas Station");
            add("GYM");
            add("Hospital");
            add("Movie Theater");
            add("Park");
            add("Parking");
            add("Restaurant");
        }
    };

    public static final int RADIUS_MIN = 1;
    public static final int RADIUS_MAX = 30;
    public static final int RADIUS_TO_KM = 1000;
    public static final String RADIUS_NAME = "radius";
    public static final String TYPE_NAME = "bank";
    public static final String PREFERENCES = "preferences";

    public static final int ZOOM_DEFAULT = 12;
    public static final String MY_LOCATION_TITLE = "My Location";

    public static final String URL_PREFIX = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?key=YOUR_API_KEY";
    public static final String PARAMETER_LOCATION = "&location=";
    public static final String PARAMETER_LOCATION_SEPARATOR = ",";
    public static final String PARAMETER_RADIUS = "&radius=";
    public static final String PARAMETER_TYPE = "&type=";
    public static final String PARAMETER_PAGETOKEN = "&pagetoken=";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_READ_CONTACTS);
            return;
        }

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mysearch = findViewById(R.id.mysearch);
        sharedPreferences = getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE);
        radius = sharedPreferences.getInt(RADIUS_NAME, RADIUS_MIN * RADIUS_TO_KM);
        type = sharedPreferences.getInt(TYPE_NAME,0);

        etSearch = (EditText) findViewById(R.id.etSearch);
        etSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_SEARCH) {
                    mysearch.setText("");
                    searchLocation();
                }
                return false;
            }
        });
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMyLocationEnabled(true);
        setMyLocation();

        mMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
            @Override
            public boolean onMyLocationButtonClick() {
                etSearch.setText("");
                setMyLocation();
                return false;
            }
        });
    }

    private void setMyLocation() {
        LocationServices.getFusedLocationProviderClient(this).getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    title = MY_LOCATION_TITLE;
                    latitude = location.getLatitude();
                    longitude = location.getLongitude();
                    setMyMarker(title);
                }
            }
        });
    }

    private void setMyMarker(String title) {
        int zoom = ZOOM_DEFAULT;
        if(radius > 5 * RADIUS_TO_KM){
            zoom--;
        }
        if(radius > 15 * RADIUS_TO_KM){
            zoom--;
        }
        if(radius > 35 * RADIUS_TO_KM){
            zoom--;
        }

        mMap.clear();
        LatLng myLocation = new LatLng(latitude, longitude);
        mMap.addMarker(new MarkerOptions().position(myLocation).title(title).icon(BitmapDescriptorFactory.fromResource(R.drawable.man)));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myLocation, zoom));
        //findplaces();
    }

    private void searchLocation(){
        try {
            Geocoder geocoder = new Geocoder(MainActivity.this);
            List<Address> list = geocoder.getFromLocationName(etSearch.getText().toString(), 1);

            if (list.size() > 0) {
                Address address = list.get(0);
                title = address.getAddressLine(0);
                latitude = address.getLatitude();
                longitude = address.getLongitude();
                etSearch.setText(title);
                setMyMarker(title);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void findplaces(){
        mysearch.setText("Looking for "+places_type.get(type)+" in "+radius/RADIUS_TO_KM+" Km");
        if(!etSearch.getText().toString().isEmpty()) {
            searchLocation();
        }
        String place_type = places_type.get(type).replaceAll(" ","_").toLowerCase();
        StringBuilder nearbyPlacesURL = new StringBuilder(URL_PREFIX);
        nearbyPlacesURL.append(PARAMETER_LOCATION).append(latitude).append(PARAMETER_LOCATION_SEPARATOR).append(longitude);
        nearbyPlacesURL.append(PARAMETER_RADIUS).append(radius);
        nearbyPlacesURL.append(PARAMETER_TYPE).append(place_type);
        Object objAsyncTask[] = new Object[2];
        objAsyncTask[0] = mMap;
        objAsyncTask[1] = nearbyPlacesURL.toString();

        new GetNearbyPlaces().execute(objAsyncTask);
        setMyMarker(title);

        mMap.addCircle(new CircleOptions()
                .center(new LatLng(latitude, longitude))
                .radius(radius)
                .strokeColor(Color.MAGENTA));


    }

    public void onClick(View view) {

        switch (view.getId()) {
            /*
            case R.id.ibSearch:

                if(!etSearch.getText().toString().isEmpty()) {
                    searchLocation();
                }

                StringBuilder nearbyPlacesURL = new StringBuilder(URL_PREFIX);
                nearbyPlacesURL.append(PARAMETER_LOCATION).append(latitude).append(PARAMETER_LOCATION_SEPARATOR).append(longitude);
                nearbyPlacesURL.append(PARAMETER_RADIUS).append(radius);
                nearbyPlacesURL.append(PARAMETER_TYPE).append(places_type.get(type));
                Object objAsyncTask[] = new Object[2];
                objAsyncTask[0] = mMap;
                objAsyncTask[1] = nearbyPlacesURL.toString();

                new GetNearbyPlaces().execute(objAsyncTask);
                setMyMarker(title);

                mMap.addCircle(new CircleOptions()
                        .center(new LatLng(latitude, longitude))
                        .radius(radius)
                        .strokeColor(Color.MAGENTA));
                break;
            */
            case R.id.settings:
                final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
                LayoutInflater inflater = this.getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.settings, null);
                dialogBuilder.setTitle("Set your search parameters!");
                dialogBuilder.setView(dialogView);
                final NumberPicker numberPicker = (NumberPicker) dialogView.findViewById(R.id.dialog_number_picker);
                numberPicker.setMinValue(RADIUS_MIN);
                numberPicker.setMaxValue(RADIUS_MAX);
                numberPicker.setValue(radius / RADIUS_TO_KM);
                numberPicker.setWrapSelectorWheel(false);
                final Spinner types = (Spinner) dialogView.findViewById(R.id.type);
                types.setAdapter(new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item,places_type));
                types.setSelection(type);
                dialogBuilder.setPositiveButton(R.string.adAccept, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        radius = numberPicker.getValue() * RADIUS_TO_KM;
                        type = types.getSelectedItemPosition();
                        SharedPreferences.Editor preferencesEditor = sharedPreferences.edit();
                        preferencesEditor.putInt(RADIUS_NAME, radius);
                        preferencesEditor.putInt(TYPE_NAME,type);
                        preferencesEditor.commit();
                        findplaces();
                    }
                });
                dialogBuilder.setNegativeButton(R.string.adCancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });
                AlertDialog alertDialog = dialogBuilder.create();
                alertDialog.show();
                break;
        }
    }
}
