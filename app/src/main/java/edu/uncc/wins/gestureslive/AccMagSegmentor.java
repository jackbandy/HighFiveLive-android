package edu.uncc.wins.gestureslive;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Concrete class for listening to a SensorDataStream and outputting segments
 * Employs the observer design pattern as a listener,
 * also begins the chain-of-responsibility
 *
 * AccMagSegmentor will segment gestures based on sliding-window magnitude thresholds
 *
 * Created by jbandy3 on 6/16/2015.
 */

public class AccMagSegmentor implements StreamListener {
    private final double ACC_ONSET_THRESHOLD = 1.1;
    private final double ACC_OFFSET_THRESHOLD = 1.5;
    private final double MIN_GESTURE_DURATION = 48;
    private final int SAMPLE_WINDOW_SIZE = 20;

    private SegmentHandler nextHandler;
    private SensorDataStream myStream;
    private ArrayList<Coordinate> segmentCoordinates;

    private int windowCount;
    private long totalCount;
    private boolean isSegmenting;

    private MSBandDataStream myBand;

    /**
     * Perfunctory constructor.
     * @param aStream the stream to listen to
     * @param aHandler the first handler in the Segment chain
     */
    public AccMagSegmentor(SensorDataStream aStream, SegmentHandler aHandler){
        myStream = aStream;
        nextHandler = aHandler;
        windowCount = 0;
        totalCount = 0;
        isSegmenting = false;
        segmentCoordinates = new ArrayList<Coordinate>(SAMPLE_WINDOW_SIZE);
        myBand = null;
    }


    public AccMagSegmentor(MSBandDataStream aBand, SensorDataStream aStream, SegmentHandler aHandler){
        this(aStream,aHandler);
        myBand = aBand;
    }


    public double avgMag(ArrayList<Coordinate> aWindow){
        double sum = 0;

        for (Coordinate c: aWindow) {
            sum += c.getMagnitude();
        }

        return sum/aWindow.size();
    }


    /**
     * Listen to the stream
     * @param newCoordinate the most recent coordinate collected from the sensor
     */
    public void newSensorData(Coordinate newCoordinate){
        totalCount++;
        if(!isSegmenting){
            //Not currently tracking a gesture, start tracking if threshold is crossed
            if (windowCount == SAMPLE_WINDOW_SIZE){
                //make sure we've collected enough points
                segmentCoordinates.remove(0);
                segmentCoordinates.trimToSize();
                segmentCoordinates.add(SAMPLE_WINDOW_SIZE - 1,newCoordinate);
                if (avgMag(segmentCoordinates) > ACC_ONSET_THRESHOLD){
                    this.onsetDidOccur();
                }
            }
            else {
                segmentCoordinates.add(newCoordinate);
                windowCount++;
            }
        }

        else {
            //Currently tracking a gesture
            windowCount++;
            segmentCoordinates.remove(0);
            segmentCoordinates.trimToSize();
            segmentCoordinates.add(SAMPLE_WINDOW_SIZE - 1,newCoordinate);

            if(windowCount % (128 - SAMPLE_WINDOW_SIZE) == 0){
                this.offsetDidOccur();
            }
        }
    }


    private void onsetDidOccur(){
        isSegmenting = true;
        //Log.v("TAG", "STARTED segmenting");

        if(myBand != null && Constants.VIBRATE_FOR_SEGMENT) myBand.vibrateBandTwice();
    }


    private void offsetDidOccur(){
        //Log.v("TAG", "STOPPED segmenting");
        if(myBand != null && Constants.VIBRATE_FOR_SEGMENT) myBand.vibrateBandOnce();

        int tmpSize = myStream.getCoordinateCache().size();
        List<Coordinate> theList = myStream.getCoordinateCache().subList((tmpSize - (128)), tmpSize);
        ArrayList<Coordinate> toPass = new ArrayList<>(windowCount);
        toPass.addAll(theList);
        nextHandler.handleNewSegment(toPass, null);

        windowCount = 0;
        segmentCoordinates.clear();
        isSegmenting = false;
    }


}
