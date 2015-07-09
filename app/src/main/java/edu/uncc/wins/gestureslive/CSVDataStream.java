package edu.uncc.wins.gestureslive;

import android.content.res.AssetManager;
import android.os.AsyncTask;
import android.os.Handler;
import android.provider.ContactsContract;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * A data stream to provide accelerometer data from the scripted gestures,
 * as if the data were coming from the
 *
 * Created by jbandy3 on 6/17/2015.
 */
public class CSVDataStream implements SensorDataStream {

    private boolean isStreaming;
    private String myFileName;
    private ScheduledExecutorService service;
    AssetManager myManager;
    ArrayList<String> theDoubles;
    ArrayList<StreamListener> myListeners;
    private ArrayList<Coordinate> myCache;
    BufferedReader reader;
    private int size;

    public CSVDataStream(String aFileName, AssetManager aManager) {
        super();
        this.myFileName = aFileName;
        myManager = aManager;
        myCache = new ArrayList<Coordinate>();
        myListeners = new ArrayList<StreamListener>();
        size = 0;
    }



    @Override
    public void addListener(StreamListener aListener) {
        myListeners.add(aListener);
    }

    public void startupStream() {
        isStreaming = true;

        reader = null;
        theDoubles = new ArrayList<String>();
        String line;
        try {
            reader = new BufferedReader(new InputStreamReader(myManager.open(myFileName)));
            /*while ((line = reader.readLine()) != null) {
                theDoubles.addAll(Arrays.asList(line.split(",")).subList(1,3));
            }*/
            Log.v("ME", "Count: " + theDoubles.size());
        } catch (IOException e) {
            Log.v("EXC", e.toString());
            e.printStackTrace();
        }

        service = Executors.newSingleThreadScheduledExecutor();
        DataStreamTask myTask = new DataStreamTask();
        service.scheduleWithFixedDelay(myTask, 0, 20, TimeUnit.MILLISECONDS);

    }


    private class DataStreamTask implements Runnable {
        private int count;

        public DataStreamTask(){
            count = 0;
        }

        @Override
        public void run() {
            String line = "";
            try {
                line = reader.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }

            List<String> theDoubles = Arrays.asList(line.split(","));
            final double accX = Double.parseDouble(theDoubles.get(1));
            final double accY = Double.parseDouble(theDoubles.get(2));
            final double accZ = Double.parseDouble(theDoubles.get(3));
            /*
            final double accX = Double.parseDouble(theDoubles.get(count++));
            final double accY = Double.parseDouble(theDoubles.get(count++));
            final double accZ = Double.parseDouble(theDoubles.get(count++));

            System.out.println("x: " + accX + " y: " + accY + " z: " + accZ);
            */
            Coordinate toPass = new Coordinate(accX, accY, accZ);
            myCache.add(size++,toPass);
            for (StreamListener myListener : myListeners) {
                myListener.newSensorData(toPass);
            }
        }
    }


    public void terminateStream() {
        service.shutdownNow();
        isStreaming = false;
    }

    public ArrayList<Coordinate> getCoordinateCache() {
        return (ArrayList<Coordinate>) myCache.subList(size-128,size);
    }
}
