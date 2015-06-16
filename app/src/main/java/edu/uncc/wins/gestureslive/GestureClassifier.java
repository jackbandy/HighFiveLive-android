package edu.uncc.wins.gestureslive;

import java.util.ArrayList;

/**
 * The highest link in the segment handler chain of responsibility:
 * given a segment (which now includes a feature vector), classify it as a gesture
 *
 * Created by jbandy3 on 6/16/2015.
 */
public class GestureClassifier extends SegmentHandler {

    /**
     * Constructor. Since this is the highest level in the chain, no handler is needed
     */
    public GestureClassifier() {
        super();
    }

    /**
     * Handle the segment, which now includes a feature vector,
     * classify it as a gesture
     * @param segmentPoints a 3-item array, whose items are coordinate ArrayLists of the segment
     *                      e.g. [ArrayList X Acc, ArrayList Y Acc, ArrayList Z Acc]
     *
     * @param featureVector an array of the extracted features of the segment, if they exist
     */
    void handleNewSegment(ArrayList<Double>[] segmentPoints, Double[] featureVector) {
        assert featureVector != null;
        //classify the segment and let the world know about it
    }
}
