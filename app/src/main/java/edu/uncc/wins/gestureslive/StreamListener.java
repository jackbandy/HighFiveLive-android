package edu.uncc.wins.gestureslive;

/**
 * Interface for classes to implement if they wish to subscribe to a SensorDataStream
 * For example, a Segmentor would implement this class in order to listen to a
 * data stream and produce segments
 * Created by jbandy3 on 6/15/2015.
 */
public interface StreamListener {
    void newSensorData(double[] coordinate);
}
