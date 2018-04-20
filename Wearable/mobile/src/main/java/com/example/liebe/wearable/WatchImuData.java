package com.example.liebe.wearable;

/**
 * Created by liebe on 2018/4/19.
 */

public class WatchImuData {
    public float yawR = 0.0f, pitchR = 0.0f, rollR = 0.0f;
    public float[] mAcceR = new float[3];
    public float[] mGyroR  = new float[3];

    public float yawL = 0.0f, pitchL = 0.0f, rollL = 0.0f;
    public float[] mAcceL = new float[3];
    public float[] mGyroL  = new float[3];
}
