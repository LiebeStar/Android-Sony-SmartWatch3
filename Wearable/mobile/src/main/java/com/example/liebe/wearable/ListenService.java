package com.example.liebe.wearable;

import android.content.Intent;
import android.util.Log;

import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;

/**
 * Created by liebe on 2018/4/19.
 */

public class ListenService extends WearableListenerService {

    private final String LOG_TAG = ListenService.class.getSimpleName();
    final static String MY_ACTION = "MY_ACTION";

    private String nodeId;
    private String receivedMessage;

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        nodeId = messageEvent.getSourceNodeId();
        receivedMessage = messageEvent.getPath();
        broadcast2Activity(receivedMessage);
    }

    private void broadcast2Activity(String message) {
        Intent intent = new Intent();
        intent.setAction(MY_ACTION);

        Log.v(LOG_TAG, "DATA_PASSED <" + message + "> Send by Broadcast from " + nodeId);

        //Right hand
        if(nodeId.equals("5ed169ac")) {
            prepareRightHandIntent(intent, message);
            sendBroadcast(intent);
        }

        //Left hand
        if(nodeId.equals("413805ce")){
            prepareLeftHandIntent(intent, message);
            sendBroadcast(intent);
        }
    }

    private void prepareLeftHandIntent(Intent intent, String message) {
        String[] tokens = message.split(",");
        if (tokens.length == 9) {
            MainActivity.mWatchImuData.yawL = Float.parseFloat(tokens[0]);
            MainActivity.mWatchImuData.pitchL = Float.parseFloat(tokens[1]);
            MainActivity.mWatchImuData.rollL = Float.parseFloat(tokens[2]);

            MainActivity.mWatchImuData.mAcceL[0] = Float.parseFloat(tokens[3]);
            MainActivity.mWatchImuData.mAcceL[1] = Float.parseFloat(tokens[4]);
            MainActivity.mWatchImuData.mAcceL[2] = Float.parseFloat(tokens[5]);

            MainActivity.mWatchImuData.mGyroL[0] = Float.parseFloat(tokens[6]);
            MainActivity.mWatchImuData.mGyroL[1] = Float.parseFloat(tokens[7]);
            MainActivity.mWatchImuData.mGyroL[2] = Float.parseFloat(tokens[8]);
        }
    }

    private void prepareRightHandIntent(Intent intent, String message) {
        String[] tokens = message.split(",");
        if (tokens.length == 9) {
            MainActivity.mWatchImuData.yawR = Float.parseFloat(tokens[0]);
            MainActivity.mWatchImuData.pitchR = Float.parseFloat(tokens[1]);
            MainActivity.mWatchImuData.rollR = Float.parseFloat(tokens[2]);

            MainActivity.mWatchImuData.mAcceR[0] = Float.parseFloat(tokens[3]);
            MainActivity.mWatchImuData.mAcceR[1] = Float.parseFloat(tokens[4]);
            MainActivity.mWatchImuData.mAcceR[2] = Float.parseFloat(tokens[5]);

            MainActivity.mWatchImuData.mGyroR[0] = Float.parseFloat(tokens[6]);
            MainActivity.mWatchImuData.mGyroR[1] = Float.parseFloat(tokens[7]);
            MainActivity.mWatchImuData.mGyroR[2] = Float.parseFloat(tokens[8]);
        }
    }
    
}
