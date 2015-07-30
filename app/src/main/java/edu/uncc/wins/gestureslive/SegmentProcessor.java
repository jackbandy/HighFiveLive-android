package edu.uncc.wins.gestureslive;

import android.util.Log;

import java.util.ArrayList;

/**
 * SegmentProcessor.java
 * Class for a preprocessor to handle a new segment
 * This is a placeholder class and does not actually perform any preprocessing
 *
 * Created by jbandy3 on 6/16/2015.
 */



public class SegmentProcessor extends SegmentHandler {

    public SegmentProcessor(SegmentHandler nextHandler) {
        super(nextHandler);
    }

    @Override
    /**
     * Do something with the raw segment data
     * @param segmentPoints a 3-item array, whose items are all the coordinates of the segment
     */
    void handleNewSegment(ArrayList<Coordinate> segmentPoints, double[] featureVector) {
        //process the data, features not yet extracted at this point
        //Log.v("TAG", "RECEIVED segment from segmentor");
        myNextHandler.handleNewSegment(segmentPoints, null);
        //Log.v("TAG", "PUSHED to handler from preprocessor");
    }

}
