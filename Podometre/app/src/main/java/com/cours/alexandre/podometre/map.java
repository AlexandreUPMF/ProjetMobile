package com.cours.alexandre.podometre;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import com.google.android.gms.maps.GoogleMap;
import com.mapbox.mapboxsdk.MapboxAccountManager;
import com.mapbox.mapboxsdk.annotations.Marker;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.annotations.MarkerViewOptions;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;

public class map extends Activity {

    private MapView mapView;

    private Marker marque;

    private PDR mPDR;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MapboxAccountManager.start(this, getString(R.string.access_token));
        setContentView(R.layout.activity_map);

        // Create a mapView
        mapView = (MapView) findViewById(R.id.mapview);
        mapView.onCreate(savedInstanceState);

        mapView.getMapAsync(new OnMapReadyCallback() {

            MarkerViewOptions markerViewOptions;

            @Override
            public void onMapReady(MapboxMap mapboxMap) {

                if (marque == null) {
                    markerViewOptions = new MarkerViewOptions()
                            .position(new LatLng(45.19351624574748, 5.773658752441406));

                    mapboxMap.addMarker(markerViewOptions);

                    marque = mapboxMap.getMarkers().get(0);
                }

                mapboxMap.setOnMapClickListener(new MapboxMap.OnMapClickListener() {
                    @Override
                    public void onMapClick(@NonNull LatLng point) {
                        marque.setPosition(new LatLng(point.getLatitude(), point.getLongitude()));
                        mPDR.setLattitude((float)point.getLatitude());
                        mPDR.setLongitude((float)point.getLongitude());
                    }
                });
            }
        });

        Intent intent = getIntent();

        // On récupère le PDR
        SensorManager sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        mPDR = new PDR(sensorManager);
        mPDR.setAccListener(mAccListener);

    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
        mPDR.start();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
        mPDR.stop();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }


    private PDR.AccelerometerListener mAccListener = new PDR.AccelerometerListener() {
        @Override
        public void mvtChangedDetected(float[] lattLng) {
            marque.setPosition(new LatLng(lattLng[0], lattLng[1]));
        }
    };


}
