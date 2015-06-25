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
public class ExperimentDataStream extends SensorDataStream {

    private boolean isStreaming;
    private String myFileName;
    private ScheduledExecutorService service;
    AssetManager myManager;
    ArrayList<String> theDoubles;
    BufferedReader reader;

    public ExperimentDataStream(String aFileName, AssetManager aManager) {
        super();
        this.myFileName = aFileName;
        myManager = aManager;
    }

    private class readTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            System.out.println("doing background");

            BufferedReader reader = null;
            try {
                System.out.println("doing reader");

                reader = new BufferedReader(new InputStreamReader(myManager.open(myFileName)));
                String line;

                System.out.println("doing while");

                while (isStreaming && (line = reader.readLine()) != null) {
                    //System.out.println("parsing");

                    StringTokenizer myTknizer = new StringTokenizer(line, ",");
                    //String[] RowData = line.split(",");

                    final double accX = Double.parseDouble(myTknizer.nextElement().toString());
                    final double accY = Double.parseDouble(myTknizer.nextElement().toString());
                    final double accZ = Double.parseDouble(myTknizer.nextElement().toString());
                    System.out.println("x: " + accX + " y: " + accY + " z: " + accZ);

                    Coordinate toPass = new Coordinate(accX, accY, accZ);
                    for (StreamListener myListener : getMyListeners()) {
                        myListener.newSensorData(toPass);
                    }

                    System.out.println("sleeping");
                    //Delay the next point by 20ms and 0ns to match the 20Hz used in experiment

                    Thread.sleep(100);

                }

            } catch (IOException ex) {
                System.out.println("2Boohoo\n" + ex.toString());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }
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
            for (StreamListener myListener : getMyListeners()) {
                myListener.newSensorData(toPass);
            }
        }
    }


    public void terminateStream() {
        service.shutdownNow();
        isStreaming = false;
    }

    public ArrayList<Coordinate> getCoordinateCache() {
        return new ArrayList<Coordinate>();
    }
}
