package edu.uncc.wins.gestureslive;

import java.util.ArrayList;

/**
 * The highest link in the segment handler chain of responsibility:
 * given a segment (which now includes a feature vector), classify it as a gesture
 * Created by jbandy3 on 6/16/2015.
 */
public class GestureClassifier extends SegmentHandler {
    public GestureClassifier(SegmentHandler nextHandler) {
        super(nextHandler);
    }

    void handleNewSegment(ArrayList<Double>[] segmentPoints, Double[] segmentFeatures) {
        assert segmentFeatures != null;
        //classify the segment and let the world know about it
    }
}
