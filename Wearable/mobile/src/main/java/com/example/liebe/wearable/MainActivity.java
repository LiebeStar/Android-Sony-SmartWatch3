package com.example.liebe.wearable;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView textVw_yaw, textVw_pitch, textVw_roll;
    private TextView textVw_Accel_X, textVw_Accel_Y, textVw_Accel_Z;
    private TextView textVw_Gyro_X, textVw_Gyro_Y, textVw_Gyro_Z;
    private MyBroadcastReceiver myReceiver;

    public static WatchImuData mWatchImuData = new WatchImuData();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initial();
        startListenerService();
        registerBroadcastReceiverFromListenerService();
    }

    @Override
    protected void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
    }


    @Override
    protected void onDestroy() {
        unregisterReceiver(myReceiver);
        super.onDestroy();
        this.finish(); // finish Activity
    }

    private void initial() {
        textVw_yaw     = (TextView)findViewById(R.id.textView_yaw);
        textVw_pitch   = (TextView)findViewById(R.id.textView_pitch);
        textVw_roll    = (TextView)findViewById(R.id.textView_roll);

        textVw_Accel_X = (TextView)findViewById(R.id.textView_Accel_X);
        textVw_Accel_Y = (TextView)findViewById(R.id.textView_Accel_Y);
        textVw_Accel_Z = (TextView)findViewById(R.id.textView_Accel_Z);

        textVw_Gyro_X  = (TextView)findViewById(R.id.textView_Gyro_X);
        textVw_Gyro_Y  = (TextView)findViewById(R.id.textView_Gyro_Y);
        textVw_Gyro_Z  = (TextView)findViewById(R.id.textView_Gyro_Z);
    }


    private void startListenerService() {
        Intent intent = new Intent(MainActivity.this, com.example.liebe.wearable.ListenService.class);
        startService(intent);
    }


    private void registerBroadcastReceiverFromListenerService() {
        myReceiver = new MyBroadcastReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ListenService.MY_ACTION);
        registerReceiver(myReceiver, intentFilter);
    }


    public class MyBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intend) {
            // TODO Auto-generated method stub
            setImuData2View(intend);
        }

        public void setImuData2View(Intent intent) {
            // Right Hand Data
            textVw_yaw.setText("Yaw_R = " + String.format("%.2f", Math.toDegrees(mWatchImuData.yawR)));
            textVw_pitch.setText("Pitch_R = " + String.format("%.2f", Math.toDegrees(mWatchImuData.pitchR)));
            textVw_roll.setText("Roll_R  = " + String.format("%.2f", Math.toDegrees(mWatchImuData.rollR)));

            // get data from broadcasting
            textVw_Accel_X.setText("ACCE_X = " + String.format("%.2f", Math.toDegrees(mWatchImuData.mAcceR[0])));
            textVw_Accel_Y.setText("ACCE_Y = " + String.format("%.2f", Math.toDegrees(mWatchImuData.mAcceR[1])));
            textVw_Accel_Z.setText("ACCE_Z = " + String.format("%.2f", Math.toDegrees(mWatchImuData.mAcceR[2])));

            textVw_Gyro_X.setText("GYRO_X = " + String.format("%.2f", Math.toDegrees(mWatchImuData.mGyroR[0])));
            textVw_Gyro_Y.setText("GYRO_Y = " + String.format("%.2f", Math.toDegrees(mWatchImuData.mGyroR[1])));
            textVw_Gyro_Z.setText("GYRO_Z = " + String.format("%.2f", Math.toDegrees(mWatchImuData.mGyroR[2])));
        }

    }
}
