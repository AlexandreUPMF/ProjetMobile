package com.cours.alexandre.podometre;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import java.util.Date;

public class PDR implements SensorEventListener {

    private float[] mCurrentLocation;

    private SensorManager mSensorManager;
    private Sensor flash ;
    private float seuil;
    private boolean passageSeuil;
    private Date delay;

    private Boussole mBoussole;

    private float mYaw;

    private float mSizePas;

    private AccelerometerListener maccelerometerListener;

    public PDR(SensorManager sensorManager) {


        mCurrentLocation = new float[2];
        mCurrentLocation[0]=(float) 45.1927; //latitude
        mCurrentLocation[1]=(float) 5.7737; //longitude

        mSensorManager = sensorManager;
        flash = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        seuil = 2.56F;
        mYaw = 0;
        passageSeuil = false;
        delay = new Date();

        mSizePas = 0.7F;

        mBoussole = new Boussole(sensorManager);
        mBoussole.setBoussoleListener(mBoussoleListener);

    }

    public void setSizePas(float size) {
        mSizePas = size;
    }

    public float[] computeNextStep(float stepSize, float bearing) {

        float newPosition[] = new float[2];

        int R = 6371000;

        float latitude = (float) Math.asin( Math.sin( Math.toRadians(this.getLatitude()))*Math.cos(stepSize/R) +
                Math.cos(Math.toRadians(this.getLatitude()))*Math.sin(stepSize/R)*Math.cos(bearing) );

        float longitude = (float) (Math.toRadians(this.getLongitude()) + Math.atan2(Math.sin(bearing)*Math.sin(stepSize/R)*Math.cos(Math.toRadians(this.getLatitude())),
                Math.cos(stepSize/R)-Math.sin(Math.toRadians(this.getLatitude()))*Math.sin(latitude)));

        newPosition[0] = (float) Math.toDegrees(latitude);
        newPosition[1] = (float) Math.toDegrees(longitude);

        return newPosition;
    }

    public float getLatitude() {
        return mCurrentLocation[0];
    }

    public float getLongitude() {
        return mCurrentLocation[1];
    }

    public void setLattitude(float latt) {
        this.mCurrentLocation[0] = latt;
    }

    public void setLongitude(float longi) {
        this.mCurrentLocation[1] = longi;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            // On calcul la norme de l'accélération
            float normAccExt = calculNorme(event);

            // On récupére le temps courrant pour le calcul du delay
            Date delayUp = new Date();

            // On calcul un delay pour ne compter les pas que toutes les 500ms
            long diff = delayUp.getTime() - delay.getTime();

            if(!passageSeuil && normAccExt > seuil) {
                passageSeuil = true;
            }
            if(passageSeuil && normAccExt < seuil && diff > 500) {
                // On appelle la fonction pour le calcul des pas
                float sizepas = mSizePas;
                float bearing = mYaw;
                mCurrentLocation = computeNextStep(sizepas,bearing);

                passageSeuil = false;

                // On reinit le delay de prise
                delay = new Date();

                maccelerometerListener.mvtChangedDetected(mCurrentLocation);
            }
        }

    }


    public float calculNorme(SensorEvent event) {

        float x = event.values[0];
        float y = event.values[1];
        float z = event.values[2];

        float normAccExt = (float)(Math.sqrt((x*x) + (y*y) + (z*z)) - 9.8);

        return normAccExt;
    }

    public void setAccListener(AccelerometerListener listener) {
        maccelerometerListener = listener;
    }

    public interface AccelerometerListener {
        public void mvtChangedDetected(float[] lattLng);
    }


    public void start() {
        mSensorManager.registerListener(this, flash, SensorManager.SENSOR_DELAY_GAME);
        mBoussole.start();
    }

    public void stop() {
        mSensorManager.unregisterListener(this);
        mBoussole.stop();
    }

    private Boussole.BoussoleListener mBoussoleListener = new Boussole.BoussoleListener() {
        @Override
        public void onBoussoleChanged(float yaw) {
            mYaw = yaw;
        }
    };



}
