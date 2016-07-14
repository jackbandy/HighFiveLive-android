package edu.uncc.wins.gestureslive.Features;

import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.transform.DftNormalization;
import org.apache.commons.math3.transform.FastFourierTransformer;
import org.apache.commons.math3.transform.TransformType;

/**
 * Created by jbandy3 on 6/30/2015.
 */
public class FFTSignalEnergy {

    public static double signalEnergyFromFFTCoefficients(double[] fftCoeff){
        double sum = 0;

        for(int i = 0; i < fftCoeff.length; i++) {
            sum += Math.pow(fftCoeff[i], 2);
        }

        return (1.0/128.0)*sum;
    }



    public static double signalEnergyFromRawData(double[] rawData){
        FastFourierTransformer myTransformer = new FastFourierTransformer(DftNormalization.STANDARD);
        Complex[] myFFT = myTransformer.transform(rawData, TransformType.FORWARD);
        double[] myCoeff = coeffsFromFFT(myFFT);

        return signalEnergyFromFFTCoefficients(myCoeff);
    }



    public static double[] coeffsFromFFT(Complex[] aFFT){
        double[] toReturn = new double[aFFT.length];

        for(int i = 0; i < toReturn.length; i++) {
            toReturn[i] = aFFT[i].abs();
        }

        return toReturn;
    }
}
