package com.cours.alexandre.podometre;

import android.app.Activity;
import android.os.Bundle;

import com.mapbox.mapboxsdk.MapboxAccountManager;

public class map extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MapboxAccountManager.start(this, getString(R.string.access_token));
        setContentView(R.layout.activity_map);

        
    }
}
