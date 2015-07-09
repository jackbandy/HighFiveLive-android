package edu.uncc.wins.gestureslive;

import java.util.ArrayList;

/**
 * Abstract class for handling segments,
 * a "link" in the chain-of-responsibility design pattern
 * Created by jbandy3 on 6/15/2015.
 */
public abstract class SegmentHandler {
    protected SegmentHandler myNextHandler;

    /**
     * Top-link constructor, the end of the chain
     */
    public SegmentHandler(){
        myNextHandler = null;
    }


    /**
     * Standard constructor
     * @param nextHandler the next link in the chain-of-responsibility
     */
    public SegmentHandler(SegmentHandler nextHandler){
        myNextHandler = nextHandler;
    }


    /**
     * Accept the segment from a Segmentor which was subscribed to
     * @param segmentPoints an ArrayList of coordinates spanning the gesture
     *
     * @param featureVector an array of the extracted features of the segment, if they exist
     */
    abstract void handleNewSegment(ArrayList<Coordinate> segmentPoints, double[] featureVector);
}
