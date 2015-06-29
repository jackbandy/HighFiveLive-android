package edu.uncc.wins.gestureslive;

import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Concrete class for listening to a SensorDataStream and outputting segments
 * Employs the observer design pattern as a listener,
 * also begins the chain-of-responsibility
 *
 * Created by jbandy3 on 6/16/2015.
 */

public class SegmentorFromAnnotation implements StreamListener {
    private SegmentHandler nextHandler;
    private SensorDataStream myStream;
    private ArrayList<Coordinate> segmentCoordinates;
    private int windowCount;
    private Integer totalCount;
    private boolean isSegmenting;

    private ArrayList<Integer> annotatedStarts;


    /**
     * Perfunctory constructor.
     * @param aStream the stream to listen to
     * @param aHandler the first handler in the Segment chain
     */
    public SegmentorFromAnnotation(SensorDataStream aStream, SegmentHandler aHandler){
        myStream = aStream;
        nextHandler = aHandler;
        windowCount = 0;
        totalCount = 0;
        isSegmenting = false;

        annotatedStarts = new ArrayList<Integer>();
        for(int i = 0; i < Constants.trial0StartPoints.length; i++){
            annotatedStarts.add(Constants.trial0StartPoints[i]);
        }
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
        totalCount++;

        if(!isSegmenting){
            //Not currently tracking a gesture, start tracking if threshold is crossed
            if(annotatedStarts.contains(totalCount)){
                isSegmenting = true;
                //System.out.println("STARTED segmenting");
                Log.v("TAG", "STARTED segmenting");

            }
        }


        else {
            windowCount++;
            //Currently tracking a gesture,
            if(windowCount == 128) {
                //end it
                Log.v("TAG", "STOPPED segmenting");
                windowCount = 0;
                segmentCoordinates.clear();
                isSegmenting = false;
                nextHandler.handleNewSegment(myStream.getCoordinateCache(),null);
                Log.v("TAG", "STOPPED segmenting");
            }
        }

    }


}
