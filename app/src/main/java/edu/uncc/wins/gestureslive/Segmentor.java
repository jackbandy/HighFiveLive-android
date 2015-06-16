package edu.uncc.wins.gestureslive;

import java.util.ArrayList;

/**
 * Abstract class for listening to a SensorDataStream and outputting segments
 * Employs the observer design pattern, with SegmentHandlers serving as listeners
 * Created by jbandy3 on 6/16/2015.
 */
public abstract class Segmentor implements StreamListener {
    private ArrayList<SegmentHandler> myListeners;
    private SensorDataStream theStream;

    /**
     * Perfunctory constructor.
     */
    public Segmentor(SensorDataStream myStream){
        theStream = myStream;
    }

    /**
     * Listen to the stream
     * @param coordinate
     */
    public void newSensorData(double[] coordinate){
        //to acquire the cache of points, simply call:
        theStream.getCoordinateCache();

        /*
         * Skeleton for observer design pattern
        if(produces new segment){
            for(SegmentHandler listener : myListeners){
                listener.handleNewSegment()
            }
        }
        */
    }

    public void addListener(SegmentHandler listener){
        myListeners.add(listener);
    }
}
