package edu.uncc.wins.gestureslive;

import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Concrete class for listening to a SensorDataStream and outputting segments
 * Employs the observer design pattern as a listener,
 * also begins the chain-of-responsibility
 *
 * IntegralSegmentor will segment gestures by integrating the acceleration functions,
 * and chopping them once the value of that integration returns to zero
 *
 * Created by jbandy3 on 6/16/2015.
 */

public class IntegralSegmentor implements StreamListener {
    private SegmentHandler nextHandler;
    private SensorDataStream myStream;
    private ArrayList<Coordinate> segmentCoordinates;
    private ArrayList<Double> trapezoidAreas;
    private double trapezoidSum;

    private int windowCount;
    private long totalCount;
    private boolean isSegmenting;

    /**
     * Perfunctory constructor.
     * @param aStream the stream to listen to
     * @param aHandler the first handler in the Segment chain
     */
    public IntegralSegmentor(SensorDataStream aStream, SegmentHandler aHandler){
        myStream = aStream;
        nextHandler = aHandler;
        windowCount = 0;
        totalCount = 0;
        trapezoidSum = 0;
        isSegmenting = false;
        segmentCoordinates = new ArrayList<Coordinate>(16);
        trapezoidAreas = new ArrayList<Double>();
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
            if(newCoordinate.getMagnitude() > 1.5) {
                isSegmenting = true;
                //System.out.println("STARTED segmenting");
                Log.v("TAG", "STARTED segmenting");
            }
        }


        else {
            //Currently tracking a gesture,
            windowCount++;
            if (windowCount > 16){
                //
                segmentCoordinates.remove(0);
                segmentCoordinates.trimToSize();
                segmentCoordinates.add(15,newCoordinate);
            }
            else {
                segmentCoordinates.add(newCoordinate);
            }

            if(windowCount % 128 == 0){
                Log.v("TAG", "STOPPED segmenting");
                windowCount = 0;
                segmentCoordinates.clear();
                trapezoidAreas.clear();
                trapezoidSum = 0;
                isSegmenting = false;
            }
            else if(windowCount % 16 == 0){

                double xwidth = 16;
                Double[] current = newCoordinate.toArray();
                Double[] previous = segmentCoordinates.get(14).toArray();
                double average = ((current[0]+current[1]+current[2]) - (previous[0]+previous[1]+previous[2])) / 2;
                //Log.v("TAG", "x1: " + newCoordinate.toArray()[0] + "\taverage: " + average);
                Log.v("TAG", "xwidth: " + xwidth + "\taverage: " + average);
                trapezoidSum += average*xwidth;
                Log.v("TAG", "\nIntegral: " + trapezoidSum + "\nStdev: " + stdDev(segmentCoordinates) + "\nPoint: " + totalCount);


                if(stdDev(segmentCoordinates) < .05){
                    //end the segment if the stdev has leveled off
                    Log.v("TAG", "STOPPED segmenting");
                    windowCount = 0;
                    segmentCoordinates.clear();
                    trapezoidAreas.clear();
                    trapezoidSum = 0;
                    isSegmenting = false;
                }
            }
        }

        //to acquire the stream's cache of points, call this getter:
        //myStream.getCoordinateCache();



        /*
         * Skeleton for chain-of-responsibility design pattern
        if(produces new segment){
            nextHandler.handleNewSegment(ArrayList<Double>, Double[] featureVector);
        }
        */

    }


}