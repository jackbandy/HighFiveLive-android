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
        int numberOfPoints = segmentPoints.size();

        ArrayList<Double> xFeatures = new ArrayList<Double>();
        double[] xArray = new double[numberOfPoints];
        int xCount = 0;
        ArrayList<Double> yFeatures = new ArrayList<Double>();
        double[] yArray = new double[numberOfPoints];
        int yCount = 0;
        ArrayList<Double> zFeatures = new ArrayList<Double>();
        double[] zArray = new double[numberOfPoints];
        int zCount = 0;

        Log.v("TAG", "reached feature extractor 1");

        //extract raw doubles from the Coordinates
        for(int index = 0; index < numberOfPoints; index++) {
            Double[] retrieved = segmentPoints.get(index).toArray();
            xArray[index] = retrieved[0];
            yArray[index] = retrieved[1];
            zArray[index] = retrieved[2];
        }

        Log.v("TAG", "reached feature extractor 2");
        //Initialize a stats library for each axis
        DescriptiveStatistics xStats = new DescriptiveStatistics(xArray);
        DescriptiveStatistics yStats = new DescriptiveStatistics(yArray);
        DescriptiveStatistics zStats = new DescriptiveStatistics(zArray);

        //min,max,mean
        xFeatures.add(xCount++,xStats.getMin());
        yFeatures.add(yCount++,yStats.getMin());
        zFeatures.add(zCount++,zStats.getMin());
        xFeatures.add(xCount++,xStats.getMax());
        yFeatures.add(yCount++,yStats.getMax());
        zFeatures.add(zCount++,zStats.getMax());
        xFeatures.add(xCount++,xStats.getMean());
        yFeatures.add(yCount++,yStats.getMean());
        zFeatures.add(zCount++,zStats.getMean());

        //stdev
        xFeatures.add(xCount++,xStats.getStandardDeviation());
        yFeatures.add(yCount++,yStats.getStandardDeviation());
        zFeatures.add(zCount++,zStats.getStandardDeviation());

        //pairwise correlation
        PearsonsCorrelation pCorr = new PearsonsCorrelation(new double[][]{xArray, yArray, zArray});
        /*TODO*/

        //zero crossing rate
        /*TODO*/

        //skew
        xFeatures.add(xCount++,xStats.getSkewness());
        yFeatures.add(yCount++,yStats.getSkewness());
        zFeatures.add(zCount++,zStats.getSkewness());

        //kurtosis
        xFeatures.add(xCount++,xStats.getKurtosis());
        yFeatures.add(yCount++,yStats.getKurtosis());
        zFeatures.add(zCount++,zStats.getKurtosis());

        //signal-to-noise ratio
        xFeatures.add(xCount++,xStats.getSkewness());
        yFeatures.add(yCount++,yStats.getSkewness());
        zFeatures.add(zCount++,zStats.getSkewness());

        //mean crossing rate
        /*TODO*/

        //trapezoidal sum
        /*TODO*/

        //signal energy
        /*TODO*/

        //DFT coefficients
        /*TODO*/

        Log.v("TAG", "reached feature extractor 3");
        //Sample Fourier Transform:
        FastFourierTransformer xFour = new FastFourierTransformer(DftNormalization.STANDARD);
        xFour.transform(xArray, TransformType.FORWARD);

        Log.v("TAG", "reached feature extractor 4");

        //when handling from this link up, a feature vector will be included
        ArrayList<Double> allFeatures = new ArrayList<>(xCount+yCount+zCount);
        allFeatures.addAll(xFeatures);
        allFeatures.addAll(yFeatures);
        allFeatures.addAll(zFeatures);

        Log.v("TAG", "reached feature extractor 5");

        Double[] toPass = new Double[xCount+yCount+zCount];
        for(int i = 0; i < toPass.length; i++)
            toPass[i] = allFeatures.get(i);



        myNextHandler.handleNewSegment(segmentPoints, toPass);
        Log.v("TAG", "pushing to classifier from extractor");
    }

}
