package edu.uncc.wins.gestureslive;

import java.util.ArrayList;

/**
 * Interface for providing a stream of accelerometer data,
 * uses the observer design pattern
 *
 * Created by jbandy3 on 6/15/2015.
 */

public interface SensorDataStream {

    /**
     * @param aListener the new listener
     */
    void addListener(StreamListener aListener);


    /**
     * Begins the data stream's processes
     */
    void startupStream();


    /**
     * Ends the data stream's processes
     */
    void terminateStream();


    /**
     * Provides cache of recent data points for further analysis
     * @return an ArrayList of the most recent data points
     */
    ArrayList<Coordinate> getCoordinateCache();

}
