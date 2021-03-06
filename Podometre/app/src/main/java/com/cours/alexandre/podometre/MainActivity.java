package com.cours.alexandre.podometre;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import java.util.Date;

public class MainActivity extends Activity implements  View.OnClickListener {

    private float longueurPas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        longueurPas = 0.7F;

        TextView champLongPas =(TextView) findViewById(R.id.longPas);
        champLongPas.setText(""+longueurPas);

        Button VueMap = (Button) findViewById(R.id.Map);
        VueMap.setOnClickListener(this);

    }

    protected void onResume () {
        super.onResume();
    }

    protected void onPause () {
        super.onPause();
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();

        if(id == R.id.Map) {
            // Explicit Intent by specifying its class name
            /*TextView champLongPas =(TextView) findViewById(R.id.longPas);
            longueurPas = Float.parseFloat((String) champLongPas.getText());*/


            EditText tonEdit = (EditText)findViewById(R.id.longPas);
            String val = tonEdit.getText().toString();
            longueurPas = Float.parseFloat(val);

            Intent i = new Intent(this, map.class);
            i.putExtra("sizePas", ""+longueurPas);

            // Starts TargetActivity
            startActivity(i);
        }
    }
}
