package com.cours.alexandre.podometre;

import android.app.Activity;
import android.app.usage.UsageEvents;
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

public class MainActivity extends Activity implements SensorEventListener, View.OnClickListener {


    private SensorManager mSensorManager;
    private Sensor flash ;
    private float seuil;
    private int nbPas;
    private boolean passageSeuil;
    private Date delay;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSensorManager = ( SensorManager ) getSystemService (Context.SENSOR_SERVICE);
        flash = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        seuil = 2.56F;
        nbPas = 0;
        passageSeuil = false;
        delay = new Date();

        Button resetbtn = (Button) findViewById(R.id.resetButton);
        resetbtn.setOnClickListener(this);

        Button VueBoussole = (Button) findViewById(R.id.boussole);
        VueBoussole.setOnClickListener(this);

        Button VuePDR = (Button) findViewById(R.id.pdr);
        VuePDR.setOnClickListener(this);

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        // On calcul la norme de l'accélération
        float normAccExt = calculNorme(event);

        // On récupére le temps courrant pour le calcul du delay
        Date delayUp = new Date();

        // On calcul un delay pour ne compter les pas que toutes les 500ms
        long diff = delayUp.getTime() - delay.getTime();

        if(passageSeuil == false && normAccExt > seuil) {
            passageSeuil = true;
        }
        if(passageSeuil == true && normAccExt < seuil && diff > 500) {
            nbPas++;
            passageSeuil = false;

            // On reinit le delay de prise
            delay = new Date();

            TextView Pas = (TextView) findViewById(R.id.nbPas);
            Pas.setText("" + this.nbPas);
        }
    }

    protected void onResume () {
        super.onResume();
        mSensorManager.registerListener(this, flash, SensorManager.SENSOR_DELAY_GAME);
    }

    protected void onPause () {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }

    public float calculNorme(SensorEvent event) {

        float x = event.values[0];
        float y = event.values[1];
        float z = event.values[2];


        float normAccExt = (float)(Math.sqrt((x*x) + (y*y) + (z*z)) - 9.8);

        return normAccExt;
    }

    public void ResetNbPas() {
        nbPas = 0;
        TextView Pas = (TextView) findViewById(R.id.nbPas);
        Pas.setText("" + this.nbPas);
        delay = new Date();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if(id == R.id.resetButton) {
            ResetNbPas();
        }

        if(id == R.id.boussole) {
            // Explicit Intent by specifying its class name
            Intent i = new Intent(MainActivity.this, Boussole.class);

            // Starts TargetActivity
            startActivity(i);
        }

        if(id == R.id.pdr) {
            // Explicit Intent by specifying its class name
            Intent i = new Intent(MainActivity.this, PDR.class);

            // Starts TargetActivity
            startActivity(i);
        }
    }
}
