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

public class MainActivity extends Activity implements SensorEventListener {


    private SensorManager mSensorManager;
    private Sensor flash ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSensorManager = ( SensorManager ) getSystemService (Context.SENSOR_SERVICE) ;
        flash = mSensorManager.getDefaultSensor (Sensor.TYPE_ACCELEROMETER) ;

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


    }

    protected void onResume () {
        super.onResume();
        mSensorManager.registerListener(this, flash, SensorManager.SENSOR_DELAY_NORMAL);
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
