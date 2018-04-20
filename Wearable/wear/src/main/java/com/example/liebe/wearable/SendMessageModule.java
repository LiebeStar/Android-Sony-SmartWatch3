package com.example.liebe.wearable;

import android.content.Context;
import android.util.Log;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.Wearable;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.jar.Pack200;

/**
 * Created by liebe on 2018/4/19.
 */

public class SendMessageModule {
    private final String LOG_TAG = SendMessageModule.class.getSimpleName();

    private String mobileNodesId;
    private String sendingMessage;

    private GoogleApiClient googleApiClient;
    private static final long CONNECTION_TIME_OUT_MS = 100;


    public SendMessageModule(Context context){
        googleApiClient = getGoogleApiClient(context);
        retrieveDeviceNode();
    }

    // get a reference to the Wear API
    private GoogleApiClient getGoogleApiClient(Context context) {
        return new GoogleApiClient.Builder(context)
                .addApi(Wearable.API)
                .build();
    }

    /*
     * Devices that are connected over Bluetooth are identified in the Wear API as “nodes”.
     * Since our Wear device is already connected to our phone,
     * we need to get a list of the nodes that are connected to it.
     */
    private void retrieveDeviceNode() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                googleApiClient.blockingConnect(CONNECTION_TIME_OUT_MS, TimeUnit.MILLISECONDS);
                NodeApi.GetConnectedNodesResult result = Wearable.NodeApi.getConnectedNodes(googleApiClient).await();

                List<Node> nodes = result.getNodes();
                if (nodes.size() > 0) {
                    mobileNodesId = nodes.get(0).getId();
                    Log.v(LOG_TAG, "mobile:" + mobileNodesId);
                }
                googleApiClient.disconnect();
            }
        }).start();
    }


    public void sendMessages(String sendMessage) {
        sendingMessage = sendMessage;
        if (mobileNodesId != null) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    if (googleApiClient != null && !(googleApiClient.isConnected() || googleApiClient.isConnecting()))
                        googleApiClient.blockingConnect(CONNECTION_TIME_OUT_MS, TimeUnit.MILLISECONDS);

                    Log.v(LOG_TAG, "sent message is <"+ sendingMessage+">");
                    Wearable.MessageApi.sendMessage(googleApiClient, mobileNodesId, sendingMessage, null).await();
                    googleApiClient.disconnect();
                }
            }).start();
        }
    }
}
