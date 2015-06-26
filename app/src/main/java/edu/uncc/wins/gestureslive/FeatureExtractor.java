package edu.uncc.wins.gestureslive;

import java.util.ArrayList;
import org.apache.commons.math3.stat.*;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

/**
 * The second-highest class in the segment handler chain of responsibility:
 * A wrapper class for u sing feature extraction libraries
 * before this link, segmentFeatures will be null,
 * this link extracts features for use with handlers up the chain
 *
 * Created by jbandy3 on 6/16/2015.
 */
public class FeatureExtractor extends SegmentHandler {
    public FeatureExtractor(SegmentHandler nextHandler) {
        super(nextHandler);
    }

    void handleNewSegment(ArrayList<Coordinate> segmentPoints, Double[] featureVector) {
        //extract the features!
        Coordinate[] tmp = (Coordinate[]) segmentPoints.toArray();
        double[] xArray = new double[tmp.length];
        double[] yArray = new double[tmp.length];
        double[] zArray = new double[tmp.length];

        //extract raw doubles from the Coordinates
        for(int index = 0; index < tmp.length; index++) {
            Double[] retrieved = tmp[index].toArray();
            xArray[index] = retrieved[0];
            yArray[index] = retrieved[1];
            zArray[index] = retrieved[2];
        }

        //Initialize a stats library for each axis
        DescriptiveStatistics xStats = new DescriptiveStatistics(xArray);
        DescriptiveStatistics yStats = new DescriptiveStatistics(yArray);
        DescriptiveStatistics zStats = new DescriptiveStatistics(zArray);

        //from here, it's as easy as
        double xSkew = xStats.getSkewness();

        //when handling from this link up, a feature vector will be included
        Double[] features = new Double[2];
        myNextHandler.handleNewSegment(segmentPoints, features);
    }

}
