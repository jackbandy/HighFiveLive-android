package edu.uncc.wins.gestureslive;

import java.util.ArrayList;

/**
 * Interface for classes to implement which are qualitatively classifying gestures
 * and broadcasting them to listeners
 *
 * Created by jbandy3 on 6/15/2015.
 */
public interface GestureClassifier {

    /**
     * Called when a GestureClassifier has confidently detected a new gesture
     * @param classification the qualitative classification of the gesture
     */
    void didClassify(String classification);

    /**
     * Called when a GestureClassifier has confidently detected a new gesture
     * @param aListener the class wanting to listen for classifications
     */
    void addListener(ClassificationListener aListener);
}
