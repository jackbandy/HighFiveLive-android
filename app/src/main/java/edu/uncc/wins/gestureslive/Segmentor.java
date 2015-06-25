package edu.uncc.wins.gestureslive;

import android.util.Log;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Concrete class for listening to a SensorDataStream and outputting segments
 * Employs the observer design pattern as a listener,
 * also begins the chain-of-responsibility
 *
 * Created by jbandy3 on 6/16/2015.
 */

public class Segmentor implements StreamListener {
    private SegmentHandler nextHandler;
    private SensorDataStream myStream;
    private ArrayList<Coordinate> segmentCoordinates;
    private int windowCount;
    private boolean isSegmenting;

    /**
     * Perfunctory constructor.
     * @param aStream the stream to listen to
     * @param aHandler the first handler in the Segment chain
     */
    public Segmentor(SensorDataStream aStream, SegmentHandler aHandler){
        myStream = aStream;
        nextHandler = aHandler;
        windowCount = 0;
        isSegmenting = false;
        segmentCoordinates = new ArrayList<Coordinate>(16);
    }


    public double stdDev(ArrayList<Coordinate> aWindow){

        //Standard deviation loop from StackOverflow
        double powerSum1 = 0;
        double powerSum2 = 0;

        for (Coordinate c: aWindow) {
            powerSum1 += c.getMagnitude();
            powerSum2 += Math.pow(c.getMagnitude(), 2);
        }
        return Math.sqrt(aWindow.size()*powerSum2 - Math.pow(powerSum1, 2))/aWindow.size();
    }


    /**
     * Listen to the stream
     * @param newCoordinate the most recent coordinate collected from the sensor
     */
    public void newSensorData(Coordinate newCoordinate){

        if(!isSegmenting){
            //Not currently tracking a gesture, start tracking if threshold is crossed
            if(newCoordinate.getMagnitude() > 1.5) {
                isSegmenting = true;
                //System.out.println("STARTED segmenting");
                Log.v("TAG", "STARTED segmenting");
            }
        }


        else {
            windowCount++;
            if (windowCount > 16){
                segmentCoordinates.remove(0);
                segmentCoordinates.trimToSize();
                segmentCoordinates.add(15,newCoordinate);
            }
            else {
                segmentCoordinates.add(newCoordinate);
            }

            if(windowCount % 128 == 0){
                //System.out.println("STOPPED segmenting");
                Log.v("TAG", "STOPPED segmenting");
                windowCount = 0;
                segmentCoordinates.clear();
                isSegmenting = false;
                //if(stdDev(segmentCoordinates) )
            }
            else if(windowCount % 16 == 0){
                //System.out.println("Stdev: " + stdDev(segmentCoordinates));
                Log.v("TAG", "Stdev: " + stdDev(segmentCoordinates));
                //if(stdDev(segmentCoordinates) )
            }
        }

        //to acquire the stream's cache of points, call this getter:
        //myStream.getCoordinateCache();



        /*
         * Skeleton for chain-of-responsibility design pattern
        if(produces new segment){
            nextHandler.handleNewSegment(ArrayList<Double>[], Double[] featureVector);
        }
        */

    }


}
