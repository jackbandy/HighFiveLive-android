package edu.uncc.wins.gestureslive;

import java.util.ArrayList;

/**
 * SegmentProcessor.java
 * Abstract class for preprocessors to handle a new segment
 * Created by jbandy3 on 6/16/2015.
 */



public class SegmentProcessor extends SegmentHandler {

    public SegmentProcessor(SegmentHandler nextHandler) {
        super(nextHandler);
    }

    @Override
    /**
     * Potentially do something with the raw segment data
     * @param segmentPoints a 3-item array, whose items are all the coordinates of the segment
     */
    void handleNewSegment(ArrayList<Double>[] segmentPoints, Double[] segmentFeatures) {
        //process the data, features are not yet extracted
        myNextHandler.handleNewSegment(segmentPoints, null);
    }

}
