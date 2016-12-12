package com.cours.alexandre.podometre;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.opengl.Matrix;

public class Boussole implements SensorEventListener {

    private SensorManager mSensorManager;
    private Sensor boubou ;

    private float[] mOrientationVals = new float[3];

    private float[] mRotationMatrixMagnetic = new float[16];
    private float[] mRotationMatrixMagneticToTrue = new float[16];
    private float[] mRotationMatrix = new float[16];

    private BoussoleListener mBoussoleListener;

    public Boussole (SensorManager sensorManager) {

        mSensorManager = sensorManager;
        boubou = mSensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() != Sensor.TYPE_ROTATION_VECTOR) {
            return;
        }

        // Transforme la rotation vector en matrice de rotation
        SensorManager.getRotationMatrixFromVector(mRotationMatrixMagnetic, event.values );

        //Création de la matrice de passage de repère magnétique au repère classique
        Matrix.setRotateM(mRotationMatrixMagneticToTrue, 0, -1.83f, 0, 0, 1);

        //Change la matrice d'orientation du repère magnétique au repère classique
        Matrix.multiplyMM(mRotationMatrix, 0, mRotationMatrixMagnetic, 0, mRotationMatrixMagneticToTrue, 0);

        // Transforme la matrice de rotation en une succession de rotations autour de z , y e t x
        SensorManager.getOrientation(mRotationMatrix , mOrientationVals);

        mBoussoleListener.onBoussoleChanged(mOrientationVals[0]);

    }

    public void start () {
        mSensorManager.registerListener(this, boubou, SensorManager.SENSOR_DELAY_GAME);
    }

    public void stop () {
        mSensorManager.unregisterListener(this);
    }

    public void setBoussoleListener (BoussoleListener listener) {
        mBoussoleListener = listener;
    }

    public interface BoussoleListener {
        public void onBoussoleChanged(float yaw);
    }

}
