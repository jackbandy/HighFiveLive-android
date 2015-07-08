package edu.uncc.wins.gestureslive;

import android.util.Log;

import java.util.ArrayList;

/**
 * SegmentProcessor.java
 * Class for a preprocessor to handle a new segment
 *
 * Created by jbandy3 on 6/16/2015.
 */



public class StretchTo128SegmentProcessor extends SegmentHandler {

    public StretchTo128SegmentProcessor(SegmentHandler nextHandler) {
        super(nextHandler);
    }

    @Override
    /**
     * Do something with the raw segment data
     * @param segmentPoints a 3-item array, whose items are all the coordinates of the segment
     */
    void handleNewSegment(ArrayList<Coordinate> segmentPoints, double[] featureVector) {
        //process the data, features not yet extracted at this point
        Log.v("TAG", "RECEIVED segment from segmentor");

        //myNextHandler.handleNewSegment(keepAtCenter(segmentPoints), null);
        myNextHandler.handleNewSegment(pushToLeft(segmentPoints), null);

        Log.v("TAG", "PUSHED to handler from preprocessor");
    }



    private static ArrayList<Coordinate> pushToLeft(ArrayList<Coordinate> segmentPoints){
        ArrayList<Coordinate> toPass = new ArrayList<Coordinate>(128);
        Coordinate fluffCoordinate = new Coordinate(0.,0.,0.);
        int initialSize = segmentPoints.size();
        int fluffPointsNeeded = 128 - initialSize;
        int currentSize = 0;

        //Add the actual segment points
        //Log.v("TAG", "Old Points: " + segmentPoints.toString());
        toPass.addAll(currentSize, segmentPoints);
        //Log.v("TAG", "New Points: " + toPass.toString());

        currentSize += initialSize;

        //Fill in the last part of the array
        while(currentSize<128)
            toPass.add(currentSize++,fluffCoordinate);

        return toPass;
    }



    private static ArrayList<Coordinate> keepAtCenter(ArrayList<Coordinate> segmentPoints){
        ArrayList<Coordinate> toPass = new ArrayList<Coordinate>(128);
        Coordinate fluffCoordinate = new Coordinate(0.,0.,0.);
        int initialSize = segmentPoints.size();
        int fluffPointsNeeded = 128 - initialSize;
        int currentSize = 0;

        //Fill in the first part of the array
        while(currentSize < fluffPointsNeeded / 2)
            toPass.add(currentSize++,fluffCoordinate);

        //Add the actual segment points
        //Log.v("TAG", "Old Points: " + segmentPoints.toString());
        toPass.addAll(currentSize, segmentPoints);
        //Log.v("TAG", "New Points: " + toPass.toString());

        currentSize += initialSize;

        //Fill in the last part of the array
        while(currentSize<128)
            toPass.add(currentSize++,fluffCoordinate);

        return toPass;
    }

}
