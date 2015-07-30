package edu.uncc.wins.gestureslive;

import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * The highest link in the segment handler chain of responsibility:
 * given a segment (which now includes a feature vector), classify it as a gesture
 *
 * Created by jbandy3 on 6/16/2015.
 */
public class LogRegClassifier extends SegmentHandler {

    private ArrayList<ClassificationListener> myListeners;
    private int totalGestures;
    boolean didAssert;

    /**
     * Constructor. Since this is the highest level in the chain, no handler is needed
     */
    public LogRegClassifier() {
        super();
        didAssert = false;
        totalGestures = 0;
        myListeners = new ArrayList<ClassificationListener>();
    }

    private static boolean almostEquals(double val1, double val2){
        double epsilon = 0.05;
        if ((val1 <= ( val2 - epsilon )) ||  ( val1 >= ( val2 + epsilon ) )) {
            return false;
        }
        return true;
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
        //Log.v("TAG", "Features: " + Arrays.toString(featureVector));
        //classify the segment and let the world know about it

        /*
        //USED FOR VERIFYING FEATURE EXTRACTION

        if(!didAssert){
            double[] toCompare = Constants.txtTo1DArray("Gesture0Features.txt");
            for(int i = 0; i < featureVector.length; i++){
                if (!almostEquals(featureVector[i],toCompare[i])){
                    Log.v("TAG","Feature " + i + ": " + featureVector[i] + " != " + toCompare[i]);
                }
            }

            didAssert = true;
        }
*/


        //----------------LOGISTIC REGRESSION CLASSIFICATION ------------------
        double[] costs = new double[9];
        double logit;
        for(int j = 0; j < 9; j++){
            logit = 0.0;

            for (int i=0; i<featureVector.length;i++)  {
                logit += (featureVector[i] * Constants.MODEL_SINGLE_POINT[j][i]);
            }
            costs[j] = sigmoid(logit);
        }


        int maxInd = -1;
        double max = 0;
        String candidates = "";
        for(int i = 0; i < costs.length; i++) {
            if(costs[i] > .5){
                if (costs[i] > max){
                    maxInd = i;
                    max = costs[i];
                }
                candidates += "\n" + indToGesture(i) + " (" + costs[i] + "),";
            }
        }


        for(ClassificationListener aListener : myListeners) {
            if (Constants.DEMO_MODE) {
                aListener.didReceiveNewClassification(indToGesture(maxInd));
            } else {
                aListener.didReceiveNewClassification("Detected " + indToGesture(maxInd) + " as gesture number " + totalGestures++  + "\n\n Candidates: " + candidates);
            }
        }
    }



    private double sigmoid(double z) {
        return 1 / (1 + Math.exp(-z));
    }


    private String indToGesture(int index){

        if(Constants.SINGLE_POINT_INDECES_MAP.containsKey(index)){
            return Constants.SINGLE_POINT_INDECES_MAP.get(index);
        }
        else if (!Constants.DEMO_MODE) {
            return "UNKNOWN";
        }
        else return "";

    }


    public void addListener(ClassificationListener aListener){
        myListeners.add(aListener);
    }


}
