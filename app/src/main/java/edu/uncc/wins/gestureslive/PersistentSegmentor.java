package edu.uncc.wins.gestureslive;

import java.util.ArrayList;
import java.util.List;

/**
 * Concrete class for listening to a SensorDataStream and outputting segments
 * Employs the observer design pattern as a listener,
 * also begins the chain-of-responsibility
 *
 * PersistentSegmentor will create a segment for every n-point window
 *
 * Created by jbandy3 on 7/14/2016.
 */

public class PersistentSegmentor implements StreamListener {
    private final int WINDOW_LENGTH = 128;
    private final int OVERLAP_AMOUNT = 64;

    private SegmentHandler nextHandler;
    private SensorDataStream myStream;
    private ArrayList<Coordinate> segmentCoordinates;

    private int windowCount;


    /**
     * Perfunctory constructor.
     * @param aStream       the stream to listen to
     * @param aHandler      the first handler in the Segment chain
     */
    public PersistentSegmentor(SensorDataStream aStream, SegmentHandler aHandler){
        myStream = aStream;
        nextHandler = aHandler;
        segmentCoordinates = new ArrayList<Coordinate>();
        for(int i = 0; i < WINDOW_LENGTH; i++){
            segmentCoordinates.add(new Coordinate(0.0,0.0,0.0));
        }
    }


    /**
     * Listen to the stream
     * @param newCoordinate the most recent coordinate collected from the sensor
     */
    public void newSensorData(Coordinate newCoordinate){
        windowCount++;
        segmentCoordinates.remove(0);
        segmentCoordinates.add(newCoordinate);
        if (windowCount % WINDOW_LENGTH == 0){
            windowCount -= OVERLAP_AMOUNT;
            this.offsetDidOccur();
        }
    }



    private void offsetDidOccur(){
        nextHandler.handleNewSegment(segmentCoordinates, null);
    }


}
