package edu.uncc.wins.gestureslive.Features;

/**
 * Created by jbandy3 on 6/30/2015.
 */
public class TrapezoidalSum {

    public static double sumFromArrayWithStepSize(double[] aFunction, double stepSize){
        double sum = 0;

        for (int i = 0; i < aFunction.length-1; i++) {
            double x = (aFunction[i] + aFunction[i+1] / 2);
            sum+=(x*stepSize);
        }

        return sum;
    }
}
