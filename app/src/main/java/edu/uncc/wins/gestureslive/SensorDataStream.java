package edu.uncc.wins.gestureslive;

import java.util.ArrayList;

/**
 * Abstract class for providing a stream of accelerometer data,
 * uses the observer design pattern
 *
 * Created by jbandy3 on 6/15/2015.
 */

public abstract class SensorDataStream {
    private ArrayList<StreamListener> myListeners = new ArrayList<StreamListener>();

    /**
     * Constructor
     */
    public SensorDataStream(){
        myListeners = new ArrayList<StreamListener>();
    }


    /**
     * @param listener the new listener
     */
    public void addListener(StreamListener listener){
        myListeners.add(listener);
    }


    /**
     * Getter for listeners
     * @return the arraylist of this class' listeners
     */
    public ArrayList<StreamListener> getMyListeners(){
        return myListeners;
    }


    /**
     * Begins the data stream's processes
     */
    abstract public void startupStream();

    /**
     * Ends the data stream's processes
     */
    abstract public void terminateStream();


    /**
     * Provides cache of recent data points for further analysis
     * @return a double array with the 64 most recent data points
     */
    abstract public double[][] getCoordinateCache();

}
