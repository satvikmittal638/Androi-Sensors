package com.example.sensors1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.widget.TextView;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity implements SensorEventListener {
private TextView attributes,value;

private boolean tempSensorIsAvailable;
private SensorManager sensorManager;
private Sensor sensor;
private Vibrator vibrator;
private float val;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        attributes=findViewById(R.id.attributes);
        value=findViewById(R.id.changedValue);
        sensorManager=(SensorManager) getSystemService(Context.SENSOR_SERVICE);
        vibrator=(Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
//        sensor=sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
//        sensor=sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);
        sensor=sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);

        tempSensorIsAvailable=sensor!=null;

        if(tempSensorIsAvailable)
        attributes.setText("Power consumption: "+ sensor.getPower() +" mA"+"\n"+
          "Vendor: "+sensor.getVendor()+"\n"+
          "Version: "+sensor.getVersion()
        );
        else
            attributes.setText("No sensor available");
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
//some like light,temp sensor returns only a single value unlike other sensors which give 3 values(for X,Y and Z axis)
        val=event.values[0];
        value.setText(String.valueOf(val));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createOneShot(500,VibrationEffect.DEFAULT_AMPLITUDE));
        }else{
            //for below 26 API
            //this is deprecated
            vibrator.vibrate(500);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }


    @Override
    //when the app comes back again from sleep
    protected void onResume() {
        super.onResume();
        if(tempSensorIsAvailable) {
            sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);//delay means the time after which the sensor will give the value
            Log.d("sat", "Sensor registered");
        }
    }

    @Override
    //when the app goes to sleep
    protected void onPause() {
        super.onPause();
        if(tempSensorIsAvailable) {
            sensorManager.unregisterListener(this);
            Log.d("sat", "Sensor unregistered");
        }


    }
}