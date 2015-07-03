package edu.uncc.wins.gestureslive;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.annotation.MainThread;
import android.util.Log;

import com.microsoft.band.notifications.VibrationType;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * The highest link in the segment handler chain of responsibility:
 * given a segment (which now includes a feature vector), classify it as a gesture
 *
 * Created by jbandy3 on 6/16/2015.
 */
public class GestureClassifier extends SegmentHandler {

    private ArrayList<ClassificationListener> myListeners;

    /**
     * Constructor. Since this is the highest level in the chain, no handler is needed
     */
    public GestureClassifier() {
        super();
        myListeners = new ArrayList<ClassificationListener>();
    }

    /**
     * Handle the segment, which now includes a feature vector,
     * classify it as a gesture
     * @param segmentPoints a 3-item array, whose items are coordinate ArrayLists of the segment
     *                      e.g. [ArrayList X Acc, ArrayList Y Acc, ArrayList Z Acc]
     *
     * @param featureVector an array of the extracted features of the segment, if they exist
     */
    void handleNewSegment(ArrayList<Coordinate> segmentPoints, double[] featureVector) {
        assert featureVector != null;
        Log.v("TAG", "Features: " + Arrays.toString(featureVector));
        //classify the segment and let the world know about it


        for(ClassificationListener aListener : myListeners){
            Log.v("TAG", "added listener!");
            aListener.newClassification(featureVector,"We made it!");
        }
    }


    public void addListener(ClassificationListener aListener){
        myListeners.add(aListener);
    }


}
