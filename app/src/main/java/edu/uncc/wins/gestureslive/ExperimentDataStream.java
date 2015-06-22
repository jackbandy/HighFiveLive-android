package edu.uncc.wins.gestureslive;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
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


    public ExperimentDataStream(String aFileName){
        super();
        this.myFileName = aFileName;
    }


    public void startupStream() {
        isStreaming = true;

        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(myFileName)));
            String line;
            while (isStreaming && (line = reader.readLine()) != null) {
                String[] RowData = line.split(",");
                double accX = Double.parseDouble(RowData[0]);
                double accY = Double.parseDouble(RowData[1]);
                double accZ = Double.parseDouble(RowData[2]);

                Coordinate toPass = new Coordinate(accX,accY,accZ);
                for(StreamListener myListener: getMyListeners()){
                    myListener.newSensorData(toPass);
                }


                try {
                    //Delay the next point by 20ms and 0ns to match the 20Hz used in experiment
                    Thread.sleep(20,0);
                } catch (InterruptedException e) {
                    System.out.println("Boohoo");
                }
            }
        }
        catch (IOException ex) {
            System.out.println("Boohoo");
        }


    }

    public void terminateStream() {
        isStreaming = false;
    }

    public ArrayList<Coordinate> getCoordinateCache() {
        return new ArrayList<Coordinate>();
    }
}
