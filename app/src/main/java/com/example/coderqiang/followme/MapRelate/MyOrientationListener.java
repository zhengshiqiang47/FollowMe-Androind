package com.example.coderqiang.followme.MapRelate;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

/**
 * Created by CoderQiang on 2017/2/18.
 */

public class MyOrientationListener implements SensorEventListener {

    private SensorManager sensorManager;
    private Context context;
    private Sensor sensor;

    public OnOrientationListener getListener() {
        return listener;
    }

    public void setListener(OnOrientationListener listener) {
        this.listener = listener;
    }

    private OnOrientationListener listener;

    private float lastX;
    private float lastY;

    public MyOrientationListener(Context context) {
        this.context=context;
    }

    public void start(){
        sensorManager=(SensorManager)context.getSystemService(Context.SENSOR_SERVICE);
        if(sensorManager!=null){
            sensor=sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
        }
        if(sensor!=null){
            sensorManager.registerListener(this,sensor,SensorManager.SENSOR_DELAY_UI);
        }
    }

    public void stop(){
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if(event.sensor.getType()==Sensor.TYPE_ORIENTATION){
            float x=event.values[SensorManager.DATA_X];
            if(Math.abs(x-lastX)>0.5){
                if(listener!=null){
                    listener.onOrientationChanged(x);
                }
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public interface OnOrientationListener{
        void onOrientationChanged(float x);
    }

}
