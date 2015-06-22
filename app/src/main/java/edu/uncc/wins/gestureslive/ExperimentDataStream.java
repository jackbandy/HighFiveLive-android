package edu.uncc.wins.gestureslive;

import android.content.res.AssetManager;
import android.os.AsyncTask;
import android.os.Handler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * A data stream to provide accelerometer data from the scripted gestures,
 * as if the data were coming from the
 *
 * Created by jbandy3 on 6/17/2015.
 */
public class ExperimentDataStream extends SensorDataStream {

    private boolean isStreaming;
    private String myFileName;
    AssetManager myManager;


    public ExperimentDataStream(String aFileName, AssetManager aManager){
        super();
        this.myFileName = aFileName;
        myManager = aManager;
    }

    private class readTask extends AsyncTask<Void,Void,Void>{

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
                    System.out.println("parsing");

                    String[] RowData = line.split(",");
                    final double accX = Double.parseDouble(RowData[0]);
                    final double accY = Double.parseDouble(RowData[1]);
                    final double accZ = Double.parseDouble(RowData[2]);

                    Coordinate toPass = new Coordinate(accX, accY, accZ);
                    for (StreamListener myListener : getMyListeners()) {
                        myListener.newSensorData(toPass);
                    }

                    System.out.println("sleeping");
                    //Delay the next point by 20ms and 0ns to match the 20Hz used in experiment

                    final Handler handler = new Handler();
                    handler.post(new Runnable() {
                        @Override
                        public void run() {

                            handler.postDelayed(this, 1000);
                        }
                    });

                }

            }
            catch (IOException ex) {
                System.out.println("2Boohoo\n" + ex.toString());
            }
            return null;
        }
    }


    public void startupStream() {
        isStreaming = true;
        new readTask().doInBackground();
    }



    public void terminateStream() {
        isStreaming = false;
    }

    public ArrayList<Coordinate> getCoordinateCache() {
        return new ArrayList<Coordinate>();
    }
}
