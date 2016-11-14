package com.cours.alexandre.podometre;

import android.app.Activity;
import android.app.usage.UsageEvents;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends Activity implements SensorEventListener {


    private SensorManager mSensorManager;
    private Sensor flash ;
    private ArrayList<Float> tableauPics;
    private float seuil;
    private int nbPas;
    private boolean passageSeuil;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSensorManager = ( SensorManager ) getSystemService (Context.SENSOR_SERVICE);
        flash = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        tableauPics = new ArrayList<>();
        seuil = 5;
        nbPas = 0;
        passageSeuil = false;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        TextView axeX = (TextView) findViewById(R.id.axeX);
        axeX.setText( "" + event.values[0]);

        TextView axeY = (TextView) findViewById(R.id.axeY);
        axeY.setText( "" + event.values[1]);

        TextView axeZ = (TextView) findViewById(R.id.axeZ);
        axeZ.setText( "" + event.values[2]);

        float normAccExt = calculNorme(event);

        TextView normAcc = (TextView) findViewById(R.id.normAccExt);
        normAcc.setText( "" + normAccExt);

        // On affiche le seuil
        TextView textSeuil = (TextView) findViewById(R.id.seuil);
        textSeuil.setText("" + this.seuil);

        if(passageSeuil == false && normAccExt > seuil) {
            passageSeuil = true;
        }

        if(passageSeuil == true && normAccExt < seuil   ) {
            nbPas++;
            passageSeuil = false;

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

}
