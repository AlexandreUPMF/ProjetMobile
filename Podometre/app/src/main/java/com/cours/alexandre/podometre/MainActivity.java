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
import java.util.Date;

public class MainActivity extends Activity implements SensorEventListener {


    private SensorManager mSensorManager;
    private Sensor flash ;
    private ArrayList<Float> tableauPics;
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
        tableauPics = new ArrayList<>();
        seuil = 2.5F;
        nbPas = 0;
        passageSeuil = false;
        delay = new Date();
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        // On affiche les valeurs sur x, y et z
        TextView axeX = (TextView) findViewById(R.id.axeX);
        axeX.setText( "" + event.values[0]);

        TextView axeY = (TextView) findViewById(R.id.axeY);
        axeY.setText( "" + event.values[1]);

        TextView axeZ = (TextView) findViewById(R.id.axeZ);
        axeZ.setText( "" + event.values[2]);

        // On calcul la norme de l'accélération
        float normAccExt = calculNorme(event);
        // On affiche l'accélération
        TextView normAcc = (TextView) findViewById(R.id.normAccExt);
        normAcc.setText( "" + normAccExt);

        // On affiche le seuil
        TextView textSeuil = (TextView) findViewById(R.id.seuil);
        textSeuil.setText("" + this.seuil);

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

}
