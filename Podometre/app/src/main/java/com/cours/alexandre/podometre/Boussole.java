package com.cours.alexandre.podometre;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.opengl.Matrix;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class Boussole extends AppCompatActivity  implements SensorEventListener {

    private float[] mOrientationVals = new float[3];

    private float[] mRotationMatrixMagnetic = new float[16];
    private float[] mRotationMatrixMagneticToTrue = new float[16];
    private float[] mRotationMatrix = new float[16];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_boussole);
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
    }
}
