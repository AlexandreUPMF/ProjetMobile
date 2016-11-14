package com.cours.alexandre.podometre;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.opengl.Matrix;
import android.os.Bundle;
import android.widget.Button;

import java.util.Date;

/**
 * Created by Alexandre on 14/11/2016.
 */

public class PDR extends Activity implements SensorEventListener {

    private float[] mOrientationVals = new float[3];

    private float[] mRotationMatrixMagnetic = new float[16];
    private float[] mRotationMatrixMagneticToTrue = new float[16];
    private float[] mRotationMatrix = new float[16];

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() != Sensor.TYPE_ROTATION_VECTOR) {
            return;
        }

        SensorManager.getRotationMatrixFromVector(mRotationMatrixMagnetic, event.values );

        Matrix.setRotateM(mRotationMatrixMagneticToTrue, 0, -1.83f, 0, 0, 1);

        Matrix.multiplyMM(mRotationMatrix, 0, mRotationMatrixMagnetic, 0, mRotationMatrixMagneticToTrue, 0);

        SensorManager.getOrientation(mRotationMatrix , mOrientationVals);
    }

}
