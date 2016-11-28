package com.cours.alexandre.podometre;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import java.util.Date;

public class MainActivity extends Activity implements  View.OnClickListener {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button VueBoussole = (Button) findViewById(R.id.boussole);
        VueBoussole.setOnClickListener(this);

        Button VuePDR = (Button) findViewById(R.id.pdr);
        VuePDR.setOnClickListener(this);

    }

    protected void onResume () {
        super.onResume();
    }

    protected void onPause () {
        super.onPause();
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();

        if(id == R.id.boussole) {
            // Explicit Intent by specifying its class name
            Intent i = new Intent(this, Boussole.class);

            // Starts TargetActivity
            startActivity(i);
        }

        if(id == R.id.pdr) {
            // Explicit Intent by specifying its class name
            Intent i = new Intent(this, PDR.class);

            // Starts TargetActivity
            startActivity(i);
        }
    }
}
