package com.cours.alexandre.podometre;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.opengl.Matrix;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Date;

public class PDR implements SensorEventListener {

    private float[] mCurrentLocation;

    private SensorManager mSensorManager;
    private Sensor flash ;
    private float seuil;
    private int nbPas;
    private boolean passageSeuil;
    private Date delay;

    private Sensor boubou ;

    private float[] mOrientationVals = new float[3];

    private float[] mRotationMatrixMagnetic = new float[16];
    private float[] mRotationMatrixMagneticToTrue = new float[16];
    private float[] mRotationMatrix = new float[16];

    private AccelerometerListener maccelerometerListener;

    public PDR(SensorManager sensorManager) {


        mCurrentLocation = new float[2];
        mCurrentLocation[0]=(float) 45.1927; //latitude
        mCurrentLocation[1]=(float) 5.7737; //longitude

        mSensorManager = sensorManager;
        flash = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        seuil = 2.56F;
        nbPas = 0;
        passageSeuil = false;
        delay = new Date();

        boubou = mSensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);

        /*TextView lat = (TextView) findViewById(R.id.lat);
        lat.setText(("Latitude : " + mCurrentLocation[0]));

        TextView longi = (TextView) findViewById(R.id.longi);
        longi.setText(("Longitude : " + mCurrentLocation[1]));*/

    }

    public float[] computeNextStep(float stepSize, float bearing) {

        //bearing = (float) (180/Math.PI * bearing);
        bearing = (float) Math.toRadians(bearing);

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
                nbPas++;
                // On appelle la fonction pour le calcul des pas
                float sizepas = (float) 0.7;
                float bearing = (float) mOrientationVals[0];
                mCurrentLocation = computeNextStep(sizepas,bearing);

                //afficher nouvelles coordonnées
                /*TextView lat = (TextView) findViewById(R.id.lat);
                lat.setText(("Latitude : " + mCurrentLocation[0]));

                TextView longi = (TextView) findViewById(R.id.longi);
                longi.setText(("Longitude : " + mCurrentLocation[1]));*/
                passageSeuil = false;

                // On reinit le delay de prise
                delay = new Date();

                maccelerometerListener.mvtChangedDetected(mCurrentLocation);
            }
        }
        else if (event.sensor.getType() == Sensor.TYPE_ROTATION_VECTOR) {
            // Transforme la rotation vector en matrice de rotation
            SensorManager.getRotationMatrixFromVector(mRotationMatrixMagnetic, event.values );

            //Création de la matrice de passage de repère magnétique au repère classique
            Matrix.setRotateM(mRotationMatrixMagneticToTrue, 0, -1.83f, 0, 0, 1);

            //Change la matrice d'orientation du repère magnétique au repère classique
            Matrix.multiplyMM(mRotationMatrix, 0, mRotationMatrixMagnetic, 0, mRotationMatrixMagneticToTrue, 0);

            // Transforme la matrice de rotation en une succession de rotations autour de z , y e t x
            SensorManager.getOrientation(mRotationMatrix , mOrientationVals);


            // On actualise la vue
            float Yawd = (float) (180/Math.PI * mOrientationVals[0]);
            float Pitchd = (float) (180/Math.PI * mOrientationVals[1]);
            float Rolld = (float) (180/Math.PI * mOrientationVals[2]);

            /*TextView Yaw = (TextView) findViewById(R.id.textYaw);
            Yaw.setText(("Yaw : " + Yawd));

            TextView Pitch = (TextView) findViewById(R.id.textPitch);
            Pitch.setText(("Pitch : " + Pitchd));

            TextView Roll = (TextView) findViewById(R.id.textRoll);
            Roll.setText(("Roll : " + Rolld));*/
        }
        else
            return;
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
        delay = new Date();
    }

    public void setAccListener(AccelerometerListener listener) {
        maccelerometerListener = listener;
    }

    public interface AccelerometerListener {
        public void mvtChangedDetected(float[] lattLng);
    }


    public void start() {
        mSensorManager.registerListener(this, flash, SensorManager.SENSOR_DELAY_GAME);
        mSensorManager.registerListener(this, boubou, SensorManager.SENSOR_DELAY_GAME);
    }

    public void stop() {
        mSensorManager.unregisterListener(this);
    }
}
