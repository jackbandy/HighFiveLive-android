package edu.uncc.wins.gestureslive;

import android.util.Log;

import java.util.ArrayList;
import org.apache.commons.math3.stat.*;
import org.apache.commons.math3.stat.correlation.PearsonsCorrelation;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.apache.commons.math3.transform.DftNormalization;
import org.apache.commons.math3.transform.FastFourierTransformer;
import org.apache.commons.math3.transform.TransformType;

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
        Log.v("TAG", "reached feature extractor");

        //extract the features!
        Coordinate[] tmp = (Coordinate[]) segmentPoints.toArray();
        ArrayList<Double> xFeatures = new ArrayList<Double>();
        double[] xArray = new double[tmp.length];
        int xCount = 0;
        ArrayList<Double> yFeatures = new ArrayList<Double>();
        double[] yArray = new double[tmp.length];
        int yCount = 0;
        ArrayList<Double> zFeatures = new ArrayList<Double>();
        double[] zArray = new double[tmp.length];
        int zCount = 0;

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

        //min,max,mean
        xFeatures.add(xStats.getMin());
        yFeatures.add(yStats.getMin());
        zFeatures.add(zStats.getMin());
        xFeatures.add(xStats.getMax());
        yFeatures.add(yStats.getMax());
        zFeatures.add(zStats.getMax());
        xFeatures.add(xStats.getMean());
        yFeatures.add(yStats.getMean());
        zFeatures.add(zStats.getMean());

        //stdev
        xFeatures.add(xStats.getStandardDeviation());
        yFeatures.add(yStats.getStandardDeviation());
        zFeatures.add(zStats.getStandardDeviation());

        //pairwise correlation
        PearsonsCorrelation pCorr = new PearsonsCorrelation(new double[][]{xArray, yArray, zArray});
        /*TODO*/

        //zero crossing rate
        /*TODO*/

        //skew
        xFeatures.add(xStats.getSkewness());
        yFeatures.add(yStats.getSkewness());
        zFeatures.add(zStats.getSkewness());

        //kurtosis
        xFeatures.add(xStats.getKurtosis());
        yFeatures.add(yStats.getKurtosis());
        zFeatures.add(zStats.getKurtosis());

        //signal-to-noise ratio
        xFeatures.add(xStats.getSkewness());
        yFeatures.add(yStats.getSkewness());
        zFeatures.add(zStats.getSkewness());

        //mean crossing rate
        /*TODO*/

        //trapezoidal sum
        /*TODO*/

        //signal energy
        /*TODO*/

        //DFT coefficients
        /*TODO*/


        //Sample Fourier Transform:
        FastFourierTransformer xFour = new FastFourierTransformer(DftNormalization.STANDARD);
        xFour.transform(xArray, TransformType.FORWARD);


        //when handling from this link up, a feature vector will be included
        ArrayList<Double> allFeatures = new ArrayList<>(xArray.length+yArray.length+zArray.length);
        allFeatures.addAll(xFeatures);
        allFeatures.addAll(yFeatures);
        allFeatures.addAll(zFeatures);

        myNextHandler.handleNewSegment(segmentPoints, (Double[]) allFeatures.toArray());
        Log.v("TAG", "pushing to classifier from extractor");
    }

}
