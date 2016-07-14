package edu.uncc.wins.gestureslive;

/**
 * Interface for classes to implement if they wish to subscribe to a GestureClassifier
 * For example, a front-end UI component to notify the user
 *
 * Created by jbandy3 on 6/15/2015.
 */
public interface ClassificationListener {

    /**
     * Called when a Classifier reports a new classification to its listeners
     * @param aClassification the qualitative classification of the gesture
     */
    void didReceiveNewClassification(String aClassification);
}
