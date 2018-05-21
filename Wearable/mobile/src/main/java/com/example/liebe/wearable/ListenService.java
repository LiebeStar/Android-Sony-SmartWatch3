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
        if(nodeId.equals("6e4f1f4e")) {
            prepareRightHandIntent(intent, message);
            sendBroadcast(intent);
        }

        //Left hand
        if(nodeId.equals("6ddcf37f")){
            prepareLeftHandIntent(intent, message);
            sendBroadcast(intent);
        }
    }

    private void prepareLeftHandIntent(Intent intent, String message) {
        String[] tokens = message.split(",");
        if (tokens.length == 3) {
            MainActivity.mWatchImuData.mAcceL[0] = Float.parseFloat(tokens[0]);
            MainActivity.mWatchImuData.mAcceL[1] = Float.parseFloat(tokens[1]);
            MainActivity.mWatchImuData.mAcceL[2] = Float.parseFloat(tokens[2]);
        }
    }

    private void prepareRightHandIntent(Intent intent, String message) {
        String[] tokens = message.split(",");
        if (tokens.length == 3) {
            MainActivity.mWatchImuData.mAcceR[0] = Float.parseFloat(tokens[0]);
            MainActivity.mWatchImuData.mAcceR[1] = Float.parseFloat(tokens[1]);
            MainActivity.mWatchImuData.mAcceR[2] = Float.parseFloat(tokens[2]);
        }
    }
    
}
