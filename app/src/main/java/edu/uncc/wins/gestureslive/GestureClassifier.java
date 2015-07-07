package edu.uncc.wins.gestureslive;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.annotation.MainThread;
import android.util.Log;

import com.microsoft.band.notifications.VibrationType;

import java.util.ArrayList;
import java.util.Arrays;

import de.bwaldvogel.liblinear.Feature;
import de.bwaldvogel.liblinear.Problem;

/**
 * The highest link in the segment handler chain of responsibility:
 * given a segment (which now includes a feature vector), classify it as a gesture
 *
 * Created by jbandy3 on 6/16/2015.
 */
public class GestureClassifier extends SegmentHandler {

    private ArrayList<ClassificationListener> myListeners;
    private int totalGestures;

    /**
     * Constructor. Since this is the highest level in the chain, no handler is needed
     */
    public GestureClassifier() {
        super();
        totalGestures = 0;
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
        //assert featureVector != null;
        Log.v("TAG", "Features: " + Arrays.toString(featureVector));
        //classify the segment and let the world know about it


        //----------------LOGISTIC REGRESSION CLASSIFICATION ------------------
        double[] confidence = new double[12];
        double logit;
        //for each potential gesture
        for(int j = 0; j < 12; j++){
            logit = .0;
            for (int i=0; i<featureVector.length;i++)  {
                logit += sigmoid(Constants.MODEL[j][i] * featureVector[i]);
            }
            confidence[j] = (logit);
        }



        int maxInd = 0;
        for(int i = 0; i < confidence.length; i++)
            if(confidence[i] > confidence[maxInd]) maxInd = i;


        Log.v("TAG", "looped through confidence");

        for(ClassificationListener aListener : myListeners){
            aListener.newClassification(featureVector,"Detected a " + indToGesture(maxInd) + " as gesture number " + totalGestures++ + " sigmoid: " + confidence[maxInd] + "\n\n Other sigmoids: " + Arrays.toString(confidence));
        }
    }



    private double sigmoid(double z) {
        return 1 / (1 + Math.exp(-z));
    }


    private String indToGesture(int index){
        if(index == 0)
            return "0";
            //return "Fist pump";

        else if(index == 1)
            return "1";
            //return "High wave";

        else if(index == 2)
            return "2";
            //return "Hand shake";

        else if(index == 3)
            return "3";
            //return "Fist bump";

        else if(index == 4)
            return "4";
            //return "Low wave";

        else if(index == 5)
            return "5";
            //return "Point";

        else if(index == 6)
            return "6";
            //return "Point";

        else if(index == 7)
            return "7";
            //return "Point";

        else if(index == 8)
            return "8";
            //return "Point";

        else if(index == 9)
            return "9";
            //return "Motion over";

        else if(index == 10)
            return "10";
            //return "High five";

        else if(index == 11)
            return "11";
            //return "Applause";

        else
            return "Unknown";

    }


    public void addListener(ClassificationListener aListener){
        myListeners.add(aListener);
    }


}
