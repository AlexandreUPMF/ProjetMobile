package com.cours.alexandre.podometre;

import android.app.Activity;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.opengl.Matrix;
import android.os.Bundle;
import android.widget.TextView;

public class Boussole extends Activity implements SensorEventListener {

    private float[] mOrientationVals = new float[3];

    private float[] mRotationMatrixMagnetic = new float[16];
    private float[] mRotationMatrixMagneticToTrue = new float[16];
    private float[] mRotationMatrix = new float[16];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_boussole);

        Intent intent = getIntent();

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


        // On actualise la vue
        float Yawd = (float) (180/Math.PI * mOrientationVals[0]);
        float Pitchd = (float) (180/Math.PI * mOrientationVals[1]);
        float Rolld = (float) (180/Math.PI * mOrientationVals[2]);

        TextView Yaw = (TextView) findViewById(R.id.textYaw);
        Yaw.setText(("Yaw : " + Yawd));

        TextView Pitch = (TextView) findViewById(R.id.textPitch);
        Pitch.setText(("Pitch : " + Pitchd));

        TextView Roll = (TextView) findViewById(R.id.textRoll);
        Roll.setText(("Roll : " + Rolld));

    }
}
