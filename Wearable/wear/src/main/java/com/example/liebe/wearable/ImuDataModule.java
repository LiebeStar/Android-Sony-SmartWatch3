package com.example.liebe.wearable;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import java.util.ArrayList;

import static android.hardware.Sensor.TYPE_ACCELEROMETER;
import static android.hardware.Sensor.TYPE_GYROSCOPE;
import static android.hardware.Sensor.TYPE_MAGNETIC_FIELD;

/**
 * Created by liebe on 2018/4/20.
 */

public class ImuDataModule implements SensorEventListener {
    private SensorManager sensorManager;
    private ArrayList<Sensor> senALL;
    private SendMessageModule sendMessageModule;

    private float[] mAcceleration;

    // Constructor
    public ImuDataModule(Context context) {
        senALL = new ArrayList<>();
        initSensorManager(context);
        initSendMessage2MobileModule(context);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        Sensor mySensor = event.sensor;

        if(mySensor.getType() == Sensor.TYPE_ACCELEROMETER)
            mAcceleration = event.values;

        if(mAcceleration != null) {
            sendIMUdata();
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    private void initSendMessage2MobileModule(Context context) {
        sendMessageModule = new SendMessageModule(context);
    }

    private void initSensorManager(Context context) {
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        addNeedSensor2SensorManager();
        registerAllSensorListener();
    }

    private void addNeedSensor2SensorManager() {
        senALL.add(sensorManager.getDefaultSensor(TYPE_ACCELEROMETER));
    }

    public void registerAllSensorListener() {
        for(int i = 0; i < senALL.size(); i++) {
            sensorManager.registerListener(this, senALL.get(i), SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    /*
     * call this method per [updateTime_Orientation] ms
     */
    private long lastUpdateTime_Orientation;
    private final int updateTime_Orientation = 10;
    private void sendIMUdata() {
        long curTime = System.currentTimeMillis();

        if((curTime - lastUpdateTime_Orientation) > updateTime_Orientation) {
            lastUpdateTime_Orientation = curTime;

            String data = String.format("%.4f,%.4f,%.4f", mAcceleration[0], mAcceleration[1], mAcceleration[2]);
            sendMessageModule.sendMessages(data);
        }
    }
}
