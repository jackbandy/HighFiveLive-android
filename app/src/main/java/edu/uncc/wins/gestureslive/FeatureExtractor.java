package edu.uncc.wins.gestureslive;

import java.util.ArrayList;

/**
 * The second-highest class in the segment handler chain of responsibility:
 * A wrapper class for using feature extraction libraries
 * before this link, segmentFeatures will be null,
 * this link extracts features for use with handlers up the chain
 * Created by jbandy3 on 6/16/2015.
 */
public class FeatureExtractor extends SegmentHandler {
    public FeatureExtractor(SegmentHandler nextHandler) {
        super(nextHandler);
    }


    void handleNewSegment(ArrayList<Double>[] segmentPoints, Double[] segmentFeatures) {
        //extract the features!
        //when handling from this link up, a feature vector will be included
        Double[] tmp = new Double[2];
        myNextHandler.handleNewSegment(segmentPoints, tmp);
    }
}
