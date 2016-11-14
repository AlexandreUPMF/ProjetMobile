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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSensorManager = ( SensorManager ) getSystemService (Context.SENSOR_SERVICE);
        flash = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        tableauPics = new ArrayList<>();
        seuil = 0;
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

        if(seuil < normAccExt) {
            tableauPics.add(normAccExt);
            seuil = normAccExt;
        }
        else
        {
            TextView tableauPic = (TextView) findViewById(R.id.tableauPic);
            for (int i = tableauPics.size() - 1 ; i == tableauPics.size() - 11; i--) {
                tableauPic.append("" +tableauPics.get(i));
            }
        }


        TextView textSeuil = (TextView) findViewById(R.id.seuil);
        textSeuil.setText("" + this.seuil);




    }

    protected void onResume () {
        super.onResume();
        mSensorManager.registerListener(this, flash, 500);
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
