package com.example.liebe.wearable;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    private TextView textVw_Accel_XL, textVw_Accel_YL, textVw_Accel_ZL;
    private TextView textVw_Accel_XR, textVw_Accel_YR, textVw_Accel_ZR;

    private MyBroadcastReceiver myReceiver;
    private FileManager fileL, fileR;
    private boolean isRecording = false;

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
        textVw_Accel_XL = (TextView)findViewById(R.id.textView_Accel_XL);
        textVw_Accel_YL = (TextView)findViewById(R.id.textView_Accel_YL);
        textVw_Accel_ZL = (TextView)findViewById(R.id.textView_Accel_ZL);

        textVw_Accel_XR = (TextView)findViewById(R.id.textView_Accel_XR);
        textVw_Accel_YR = (TextView)findViewById(R.id.textView_Accel_YR);
        textVw_Accel_ZR = (TextView)findViewById(R.id.textView_Accel_ZR);
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

    public void createFile(View view)
    {
        fileL = new FileManager("L", "All");
        fileR = new FileManager("R", "All");
        isRecording = true;
    }


    public class MyBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intend) {
            // TODO Auto-generated method stub
            setImuData2View(intend);

            if(isRecording==true){
                writeRightImuData2File();
                writeLeftImuData2File();
            }
        }

        public void setImuData2View(Intent intent) {
            // Left Hand Data
            textVw_Accel_XL.setText("ACCE_XL = " + String.format("%.2f", Math.toDegrees(mWatchImuData.mAcceL[0])));
            textVw_Accel_YL.setText("ACCE_YL = " + String.format("%.2f", Math.toDegrees(mWatchImuData.mAcceL[1])));
            textVw_Accel_ZL.setText("ACCE_ZL = " + String.format("%.2f", Math.toDegrees(mWatchImuData.mAcceL[2])));

            // Right Hand Data
            textVw_Accel_XR.setText("ACCE_XR = " + String.format("%.2f", Math.toDegrees(mWatchImuData.mAcceR[0])));
            textVw_Accel_YR.setText("ACCE_YR = " + String.format("%.2f", Math.toDegrees(mWatchImuData.mAcceR[1])));
            textVw_Accel_ZR.setText("ACCE_ZR = " + String.format("%.2f", Math.toDegrees(mWatchImuData.mAcceR[2])));
        }

        public void writeRightImuData2File()
        {
            String data = Double.toString( mWatchImuData.mAcceR[0] ) + " " +
                    Double.toString( mWatchImuData.mAcceR[1]) + " " +
                    Double.toString( mWatchImuData.mAcceR[2]) + "\r\n";

            fileR.writeDataToFile(data);
        }

        public void writeLeftImuData2File()
        {
            String data = Double.toString( mWatchImuData.mAcceL[0] ) + " " +
                    Double.toString( mWatchImuData.mAcceL[1] ) + " " +
                    Double.toString( mWatchImuData.mAcceL[2] ) + "\r\n";

            fileL.writeDataToFile(data);
        }

    }
}
