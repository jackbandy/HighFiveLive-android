package edu.uncc.wins.gestureslive.Features;

import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.transform.DftNormalization;
import org.apache.commons.math3.transform.FastFourierTransformer;
import org.apache.commons.math3.transform.TransformType;

/**
 * Created by jbandy3 on 7/7/2015.
 */
public class FFTCoefficients {

    public static Double[] coefficientsFromRawData(double[] rawData, int howManyCoeffs){
        FastFourierTransformer myTransformer = new FastFourierTransformer(DftNormalization.STANDARD);
        Complex[] myFFT = myTransformer.transform(rawData, TransformType.FORWARD);

        Double[] toReturn = new Double[howManyCoeffs];

        for(int i = 0; i < howManyCoeffs; i++) {
            toReturn[i] = myFFT[i].abs();
        }

        return toReturn;
    }


    public static Double[] coefficientsFromRawData(double[] rawData){
        return coefficientsFromRawData(rawData,8);
    }
}
