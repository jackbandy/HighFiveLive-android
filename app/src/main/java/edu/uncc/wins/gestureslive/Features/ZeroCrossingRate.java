package edu.uncc.wins.gestureslive.Features;

/**
 * Copyright (C) 2011 Jacquet Wong
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Modified by jbandy3 on 6/30/2015.
 */

public class ZeroCrossingRate {
    private double[] signals;
    private double lengthInPoints;

    /**
     * Constructor
     *
     * @param signals       input signal array
     * @param lengthInPoints        length of the signal (in points)
     */
    public ZeroCrossingRate(double[] signals, double lengthInPoints){
        setSignals(signals,1);
    }

    /**
     * set the signals
     *
     * @param signals       input signal array
     * @param lengthInPoints        length of the signal (in points)
     */
    public void setSignals(double[] signals, double lengthInPoints){
        this.signals=signals;
        this.lengthInPoints=lengthInPoints;
    }

    public double evaluate(){
        int numZC=0;
        int size=signals.length;

        for (int i=0; i<size-1; i++){
            if((signals[i]>=0 && signals[i+1]<0) || (signals[i]<0 && signals[i+1]>=0)){
                numZC++;
            }
        }

        return numZC/lengthInPoints;
    }
}
