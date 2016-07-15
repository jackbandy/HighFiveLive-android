package edu.uncc.wins.gestureslive;

import java.util.ArrayList;

/**
 * The highest link in the segment handler chain of responsibility:
 * given a segment (which now includes a feature vector), classify it as a gesture
 *
 *
 *
 * Created by jbandy3 on 7/15/2016.
 */
public class KMeansClassifier extends SegmentHandler {

    private ArrayList<ClassificationListener> myListeners;
    private int totalGestures;
    boolean didAssert;

    /**
     * Constructor. Since this is the highest level in the chain, no handler is needed
     */
    public KMeansClassifier() {
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



        //----------------K MEANS  CLASSIFICATION ------------------
        int clusters = Constants.MODEL_K_MEANS.length;
        double[] dists = new double[clusters];
        double dist;
        for(int j = 0; j < clusters; j++){
            dist = 0.0;
            for (int i=0; i<featureVector.length;i++)  {
                dist += Math.pow((featureVector[i] - Constants.MODEL_K_MEANS[j][i]),2.0);
            }
            dists[j] = Math.sqrt(dist);
        }


        int minInd = -1;
        double min = 0;
        String candidates = "";
        for(int i = 0; i < clusters; i++) {
                if (dists[i] < min){
                    minInd = i;
                    min = dists[i];
                }
                candidates += "\n" + indToGesture(i) + " (" + dists[i] + "),";

        }


        for(ClassificationListener aListener : myListeners) {
            if (Constants.DEMO_MODE) {
                aListener.didReceiveNewClassification(indToGesture(minInd));
            } else {
                aListener.didReceiveNewClassification("Detected " + indToGesture(minInd) + " as gesture number " + totalGestures++  + "\n\n Candidates: " + candidates);
            }
        }
    }



    private String indToGesture(int index){

        if(Constants.K_MEANS_INDECES_MAP.containsKey(index)){
            return Constants.K_MEANS_INDECES_MAP.get(index);
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
