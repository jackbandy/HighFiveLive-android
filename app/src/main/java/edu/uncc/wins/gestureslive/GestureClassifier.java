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
        double[] costs = new double[12];
        double logit;
        //for each potential gesture
        for(int j = 0; j < 12; j++){
            logit = .0;

            for (int i=0; i<featureVector.length;i++)  {
                //double term1 = (-1 * featureVector[i]) * Math.log(sigmoid(Constants.MODEL[j][i]));
                //double term2 = (1 - featureVector[i]) * Math.log(1 - sigmoid(Constants.MODEL[j][i]));
                logit += featureVector[i] * Constants.MODEL[j][i];
            }
            costs[j] = sigmoid(logit);
        }


        int maxInd = 0;
        String candidates = "";
        for(int i = 0; i < costs.length; i++) {
            if (costs[i] > costs[maxInd]) maxInd = i;
            if(costs[i] > .9) candidates += indToGesture(i) + ",\n";
        }
        /*
        int minInd = 0;
        for(int i = 0; i < costs.length; i++)
            if(costs[i] < costs[minInd]) minInd = i;
        */

        Log.v("TAG", "looped through confidence");

        for(ClassificationListener aListener : myListeners){
            aListener.newClassification(featureVector,"Detected a " + indToGesture(maxInd) + " as gesture number " + totalGestures++ + " sigmoid: " + costs[maxInd] + "\n\n Candidates: " + candidates);
        }
    }



    private double sigmoid(double z) {
        return 1 / (1 + Math.exp(-z));
    }


    private String indToGesture(int index){
        if(index == 0)
            //return "0";
            return "FIST PUMP";

        else if(index == 1)
            //return "1";
            return "HIGH WAVE";

        else if(index == 2)
            //return "2";
            return "HAND SHAKE";

        else if(index == 3)
            //return "3";
            return "FIST BUMP";

        else if(index == 4)
            //return "4";
            return "LOW WAVE";

        else if(index == 5)
            //return "5";
            return "POINT";

        else if(index == 6)
            //return "6";
            return "POINT";

        else if(index == 7)
            //return "7";
            return "POINT";

        else if(index == 8)
            //return "8";
            return "POINT";

        else if(index == 9)
            //return "9";
            return "MOTION OVER";

        else if(index == 10)
            //return "10";
            return "HIGH FIVE";

        else if(index == 11)
            //return "11";
            return "APPLAUSE";

        else
            return "UNKNOWN";

    }


    public void addListener(ClassificationListener aListener){
        myListeners.add(aListener);
    }


}
