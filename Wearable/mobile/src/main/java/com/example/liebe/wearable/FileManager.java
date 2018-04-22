package com.example.liebe.wearable;

import android.os.Environment;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.text.SimpleDateFormat;

/**
 * Created by liebe on 2018/4/22.
 */

public class FileManager {
    SimpleDateFormat sDateFormat;
    String nowTime;
    String path;
    String fileName;

    private File ofile, ifile;

    public FileManager( String directLR, String coordinate )
    {
        sDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        nowTime = sDateFormat.format(new java.util.Date());

        path = Environment.getExternalStoragePublicDirectory( Environment.DIRECTORY_DCIM ).getAbsolutePath().toString();
        fileName = nowTime + "_" + directLR + ".txt";
        ofile = new File( path + "/TaiChi/" + fileName );
    }

    public void writeDataToFile( String data ) {
        File parentPath = ofile.getParentFile();

        if(!isSdcardWritable()){
            Log.v("writeDataToFile", "SD卡不存在！");
            return;
        }

        try{
            // 如果資料夾不存在，就產生一個資料夾
            if(!parentPath.exists()) parentPath.mkdir();

            OutputStream outSt = new FileOutputStream(ofile, true);
            outSt.write(data.getBytes());
            outSt.close();
        } catch (IOException e) {
            Log.v("writeDataToFile", e.toString());
        }
    }

    public String readDataFromFile(){
        ifile = new File(path + "/Right/1 太極起式X.txt");
        String ret = "";

        try {
            InputStream inputStream = new FileInputStream(ifile);

            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    stringBuilder.append(receiveString);
                    stringBuilder.append("\r\n");
                }

                inputStream.close();
                ret = stringBuilder.toString();
            }
        }
        catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }

        return ret;
    }

    // Check whether the SD card is exist
    private boolean isSdcardWritable(){
        String state = Environment.getExternalStorageState();
        if(state.equals(Environment.MEDIA_MOUNTED))
            return true;
        else return false;
    }
}
