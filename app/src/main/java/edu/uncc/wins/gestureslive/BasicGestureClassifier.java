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
public class BasicGestureClassifier extends SegmentHandler {

    private ArrayList<ClassificationListener> myListeners;
    private int totalGestures;

    /**
     * Constructor. Since this is the highest level in the chain, no handler is needed
     */
    public BasicGestureClassifier() {
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
        double[] costs = new double[9];
        double logit;
        //for each potential gesture
        for(int j = 0; j < 9; j++){
            logit = .0;

            for (int i=0; i<featureVector.length;i++)  {
                //double term1 = (-1 * featureVector[i]) * Math.log(sigmoid(Constants.MODEL[j][i]));
                //double term2 = (1 - featureVector[i]) * Math.log(1 - sigmoid(Constants.MODEL[j][i]));
                logit += featureVector[i] * Constants.MODEL_SINGLE_POINT[j][i];
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


        for(ClassificationListener aListener : myListeners){
            aListener.didReceiveNewClassification("Detected " + indToGesture(maxInd) + " as gesture number " + totalGestures++  + "\n\n Candidates: " + candidates);
        }
    }



    private double sigmoid(double z) {
        return 1 / (1 + Math.exp(-z));
    }


    private String indToGesture(int index){

        if(Constants.SINGLE_POINT_INDECES_MAP.containsKey(index)){
            return Constants.SINGLE_POINT_INDECES_MAP.get(index);
        }
        else return "UNKNOWN";

    }


    public void addListener(ClassificationListener aListener){
        myListeners.add(aListener);
    }


}
