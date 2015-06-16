package edu.uncc.wins.gestureslive;

import java.util.ArrayList;

/**
 * Abstract class for handling segments as part of chain-of-responsibility design pattern
 * Created by jbandy3 on 6/15/2015.
 */
public abstract class SegmentHandler {
    protected SegmentHandler myNextHandler;

    public SegmentHandler(SegmentHandler nextHandler){
        myNextHandler = nextHandler;
    }

    /**
     * Accept the segment from a Segmentor which was subscribed to
     * @param segmentPoints a 3-item array, whose items are coordinate ArrayLists of the segment
     *                      e.g. [ArrayList X Acc, ArrayList Y Acc, ArrayList Z Acc]
     *
     * @param segmentFeatures an array of the extracted features of the segment, if they exist
     */
    abstract void handleNewSegment(ArrayList<Double>[] segmentPoints, Double[] segmentFeatures);
}
