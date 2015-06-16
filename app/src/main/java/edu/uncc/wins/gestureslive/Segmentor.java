package edu.uncc.wins.gestureslive;

/**
 * Concrete class for listening to a SensorDataStream and outputting segments
 * Employs the observer design pattern as a listener,
 * also begins the chain-of-responsibility
 *
 * Created by jbandy3 on 6/16/2015.
 */

public class Segmentor implements StreamListener {
    private SegmentHandler nextHandler;
    private SensorDataStream myStream;

    /**
     * Perfunctory constructor.
     * @param aStream the stream to listen to
     * @param aHandler the first handler in the Segment chain
     */
    public Segmentor(SensorDataStream aStream, SegmentHandler aHandler){
        myStream = aStream;
        nextHandler = aHandler;
    }


    /**
     * Listen to the stream
     * @param coordinate the most recent coordinate collected from the sensor
     */
    public void newSensorData(double[] coordinate){
        //to acquire the cache of points, simply call:
        myStream.getCoordinateCache();

        /*
         * Skeleton for chain-of-responsibility design pattern
        if(produces new segment){
            nextHandler.handleNewSegment(ArrayList<Double>[], Double[] featureVector);
        }
        */

    }


}
