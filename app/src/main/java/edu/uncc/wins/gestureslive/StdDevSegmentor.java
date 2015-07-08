package edu.uncc.wins.gestureslive;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Concrete class for listening to a SensorDataStream and outputting segments
 * Employs the observer design pattern as a listener,
 * also begins the chain-of-responsibility
 *
 * StdDevSegmentor will segment gestures based on standard deviation thresholds
 *
 * Created by jbandy3 on 6/16/2015.
 */

public class StdDevSegmentor implements StreamListener {
    private final double STDEV_ONSET_THRESHOLD = .5;
    private final double STDEV_OFFSET_THRESHOLD = 0.1;


    private SegmentHandler nextHandler;
    private SensorDataStream myStream;
    private ArrayList<Coordinate> segmentCoordinates;
    private ArrayList<Double> trapezoidAreas;
    private double trapezoidSum;

    private int windowCount;
    private long totalCount;
    private boolean isSegmenting;

    private MSBandDataStream myBand;

    /**
     * Perfunctory constructor.
     * @param aStream the stream to listen to
     * @param aHandler the first handler in the Segment chain
     */
    public StdDevSegmentor(SensorDataStream aStream, SegmentHandler aHandler){
        myStream = aStream;
        nextHandler = aHandler;
        windowCount = 0;
        totalCount = 0;
        trapezoidSum = 0;
        isSegmenting = false;
        segmentCoordinates = new ArrayList<Coordinate>(16);
        trapezoidAreas = new ArrayList<Double>();
        myBand = null;
    }


    public StdDevSegmentor(MSBandDataStream aBand, SensorDataStream aStream, SegmentHandler aHandler){
        this(aStream,aHandler);
        myBand = aBand;
    }


    public double stdDev(ArrayList<Coordinate> aWindow){

        //Standard deviation loop from StackOverflow
        double powerSum1 = 0;
        double powerSum2 = 0;

        for (Coordinate c: aWindow) {
            powerSum1 += c.getMagnitude();
            powerSum2 += Math.pow(c.getMagnitude(), 2);
        }
        return Math.sqrt(aWindow.size()*powerSum2 - Math.pow(powerSum1, 2))/aWindow.size();
    }


    /**
     * Listen to the stream
     * @param newCoordinate the most recent coordinate collected from the sensor
     */
    public void newSensorData(Coordinate newCoordinate){
        totalCount++;
        if(!isSegmenting){
            //Not currently tracking a gesture, start tracking if threshold is crossed
            if (windowCount == 16){
                //
                segmentCoordinates.remove(0);
                segmentCoordinates.trimToSize();
                segmentCoordinates.add(15,newCoordinate);
                if(stdDev(segmentCoordinates) > STDEV_ONSET_THRESHOLD){
                    this.onsetDidOccur();
                }
            }
            else {
                segmentCoordinates.add(newCoordinate);
                windowCount++;
            }


            /*
            if(newCoordinate.getMagnitude() > MAGNITUDE_ONSET_THRESHOLD) {
                this.onsetDidOccur();
            }
            */
        }


        else {
            //Currently tracking a gesture,
            windowCount++;
            segmentCoordinates.remove(0);
            segmentCoordinates.trimToSize();
            segmentCoordinates.add(15,newCoordinate);


            if(windowCount % 112 == 0){
                this.offsetDidOccur();
            }


            else if(windowCount % 16 == 0){

                Double[] current = newCoordinate.toArray();
                Double[] previous = segmentCoordinates.get(14).toArray();
                double average = ((current[0]+current[1]+current[2]) - (previous[0]+previous[1]+previous[2])) / 2;
                trapezoidSum += average*1.0;
                //Log.v("TAG", "\nIntegral: " + trapezoidSum + "\nStdev: " + stdDev(segmentCoordinates) + "\nPoint: " + totalCount);


                if(stdDev(segmentCoordinates) < STDEV_OFFSET_THRESHOLD && windowCount > 48){
                    //end the segment if the stdev has leveled off
                    this.offsetDidOccur();
                }
            }

        }


    }


    private void onsetDidOccur(){
        isSegmenting = true;
        Log.v("TAG", "STARTED segmenting");

        if(myBand != null && Constants.VIBRATE_FOR_SEGMENT) myBand.vibrateBandTwice();
    }


    private void offsetDidOccur(){
        Log.v("TAG", "STOPPED segmenting");


        if(myBand != null && Constants.VIBRATE_FOR_SEGMENT) myBand.vibrateBandOnce();

        List<Coordinate> theList = myStream.getCoordinateCache().subList(128 - (windowCount+16), 128);
        ArrayList<Coordinate> toPass = new ArrayList<>(windowCount);
        toPass.addAll(theList);
        nextHandler.handleNewSegment(toPass, null);

        windowCount = 0;
        segmentCoordinates.clear();
        trapezoidAreas.clear();
        trapezoidSum = 0;
        isSegmenting = false;
    }


}
