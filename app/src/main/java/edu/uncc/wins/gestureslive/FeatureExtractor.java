package edu.uncc.wins.gestureslive;

import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;

import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.stat.*;
import org.apache.commons.math3.stat.correlation.PearsonsCorrelation;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.apache.commons.math3.transform.DftNormalization;
import org.apache.commons.math3.transform.FastFourierTransformer;
import org.apache.commons.math3.transform.TransformType;

import edu.uncc.wins.gestureslive.Features.FFTSignalEnergy;
import edu.uncc.wins.gestureslive.Features.MeanCrossingRate;
import edu.uncc.wins.gestureslive.Features.ZeroCrossingRate;

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


    void handleNewSegment(ArrayList<Coordinate> segmentPoints, double[] featureVector) {
        Log.v("TAG", "reached feature extractor");
        //extract the features!


        ArrayList<Double> allFeatures = new ArrayList<Double>();
        int featCount = 0;

        //individual arrays to hold all the points
        int numberOfPoints = segmentPoints.size();
        double[] xArray = new double[numberOfPoints];
        double[] yArray = new double[numberOfPoints];
        double[] zArray = new double[numberOfPoints];


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
        allFeatures.add(featCount++, xStats.getMin());
        allFeatures.add(featCount++, yStats.getMin());
        allFeatures.add(featCount++, zStats.getMin());
        allFeatures.add(featCount++, xStats.getMax());
        allFeatures.add(featCount++, yStats.getMax());
        allFeatures.add(featCount++, zStats.getMax());
        allFeatures.add(featCount++, xStats.getMean());
        allFeatures.add(featCount++, yStats.getMean());
        allFeatures.add(featCount++, zStats.getMean());

        //stdev
        allFeatures.add(featCount++, xStats.getStandardDeviation());
        allFeatures.add(featCount++, yStats.getStandardDeviation());
        allFeatures.add(featCount++, zStats.getStandardDeviation());

        //pairwise correlation
        /*TODO*/
        PearsonsCorrelation pCorr = new PearsonsCorrelation(new double[][]{xArray, yArray, zArray});
        //PLACEHOLDER FOR TESTING
        allFeatures.add(featCount++, 0.);
        allFeatures.add(featCount++, 0.);
        allFeatures.add(featCount++, 0.);




        //zero crossing rate
        allFeatures.add(featCount++, ZeroCrossingRate.zeroCrossingRate(xArray,128));
        allFeatures.add(featCount++, ZeroCrossingRate.zeroCrossingRate(yArray, 128));
        allFeatures.add(featCount++, ZeroCrossingRate.zeroCrossingRate(zArray,128));

        //skew
        allFeatures.add(featCount++, xStats.getSkewness());
        allFeatures.add(featCount++, yStats.getSkewness());
        allFeatures.add(featCount++, zStats.getSkewness());

        //kurtosis
        allFeatures.add(featCount++, xStats.getKurtosis());
        allFeatures.add(featCount++, yStats.getKurtosis());
        allFeatures.add(featCount++, zStats.getKurtosis());

        //signal-to-noise ratio
        /*TODO*/
        //PLACEHOLDER FOR TESTING
        allFeatures.add(featCount++, 0.);
        allFeatures.add(featCount++, 0.);
        allFeatures.add(featCount++, 0.);

        //mean crossing rate
        allFeatures.add(featCount++, MeanCrossingRate.meanCrossingRate(xArray, 128, xStats.getMean()));
        allFeatures.add(featCount++, MeanCrossingRate.meanCrossingRate(yArray, 128, yStats.getMean()));
        allFeatures.add(featCount++, MeanCrossingRate.meanCrossingRate(zArray, 128, zStats.getMean()));


        //trapezoidal sum
        /*TODO*/
        //what step size to use?
        //PLACEHOLDER FOR TESTING
        allFeatures.add(featCount++, 0.);
        allFeatures.add(featCount++, 0.);
        allFeatures.add(featCount++, 0.);


        //signal energy
        allFeatures.add(featCount++, FFTSignalEnergy.signalEnergyFromRawData(xArray));
        allFeatures.add(featCount++, FFTSignalEnergy.signalEnergyFromRawData(yArray));
        allFeatures.add(featCount++, FFTSignalEnergy.signalEnergyFromRawData(zArray));


        //DFT coefficients
        /*TODO*/


        //when handling a segment from this link up, a feature vector will be included
        double[] toPass = new double[featCount];
        for(int i = 0; i < toPass.length; i++)
            toPass[i] = allFeatures.get(i);

        myNextHandler.handleNewSegment(segmentPoints, toPass);
        Log.v("TAG", "pushing to classifier from extractor");
    }

}
