package edu.uncc.wins.gestureslive;

import android.content.Context;
import android.os.AsyncTask;

import com.microsoft.band.BandClient;
import com.microsoft.band.BandClientManager;
import com.microsoft.band.BandException;
import com.microsoft.band.BandIOException;
import com.microsoft.band.BandInfo;
import com.microsoft.band.ConnectionState;
import com.microsoft.band.notifications.VibrationType;
import com.microsoft.band.sensors.BandAccelerometerEvent;
import com.microsoft.band.sensors.BandAccelerometerEventListener;
import com.microsoft.band.sensors.SampleRate;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.app.Activity;
import android.os.AsyncTask;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import edu.uncc.wins.gestureslive.StreamListener;
import edu.uncc.wins.gestureslive.SensorDataStream;

/**
 * Concrete implementation of the SensorDataStream abstract class,
 * which allows for multiple "observers" to listen
 * Most of the snippets were pasted from Microsoft's BandStreamingSample app
 *
 * Created by jbandy3 on 6/15/2015.
 */
public class MSBandDataStream extends SensorDataStream {
    private BandClient client;
    private Context context;
    private ArrayList<Coordinate> myCache;

    public MSBandDataStream(Context context){
        super();
        client = null;
        this.context = context.getApplicationContext();
    }

    public MSBandDataStream(){
        super();
        client = null;
    }


    public void startupStream() {
        System.out.println("Reached startup stream");
        myCache = new ArrayList<Coordinate>(128);
        new myTask().execute();
    }


    public void terminateStream() {
        if (client != null) {
            try {
                client.getSensorManager().unregisterAccelerometerEventListeners();
            } catch (BandIOException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    /**
     *
     * @return a 3-item array containing the 128 (might change) most recent points,
     * collected in the stream from the x, y, and z axes
     */
    public ArrayList<Coordinate> getCoordinateCache() {
        return new ArrayList<Coordinate>(0);
    }


    /**
     * Microsoft-provided
     * EventListener method for each time the accelerometer reports new data
     */
    private BandAccelerometerEventListener mAccelerometerEventListener = new BandAccelerometerEventListener() {
        @Override
        public void onBandAccelerometerChanged(final BandAccelerometerEvent event) {
            //Log.v("Acc", "NEW DATA");
            //System.out.println("NEW DATA");
            double accX, accY, accZ;
            accX = accY = accZ = 0;

            if (event != null) {
                accX = event.getAccelerationX();
                accY = event.getAccelerationY();
                accZ = event.getAccelerationZ();
            }

            Coordinate toPass = new Coordinate(accX,accY,accZ);
            for(StreamListener myListener: getMyListeners()){
                myListener.newSensorData(toPass);
            }
            myCache.add(toPass);
        }
    };


    /**
     * Microsoft-provided
     * method to detect connection to the Band
     */
    private boolean getConnectedBandClient() throws InterruptedException, BandException {
        if (client == null) {
            BandInfo[] devices = BandClientManager.getInstance().getPairedBands();
            if (devices.length == 0) {
                System.out.println("Band isn't paired with your phone.\n");
                return false;
            }
            client = BandClientManager.getInstance().create(context, devices[0]);
        } else if (ConnectionState.CONNECTED == client.getConnectionState()) {
            return true;
        }

        System.out.println("Band is connecting...\n");
        return ConnectionState.CONNECTED == client.connect().await();
    }



    /**
     * Microsoft-provided
     * task to connect to the Band
     */
    private class myTask extends AsyncTask<Void, Void, Void> {
        protected Void doInBackground(Void... params) {
            System.out.println("Reached do in bg");
            try {
                if (getConnectedBandClient()) {
                    System.out.println("Band is connected.\n");
                    client.getSensorManager().registerAccelerometerEventListener(mAccelerometerEventListener, SampleRate.MS16);
                } else {
                    System.out.println("Band isn't connected. Please make sure bluetooth is on and the band is in range.\n");
                }
            } catch (BandException e) {
                String exceptionMessage="";
                switch (e.getErrorType()) {
                    case UNSUPPORTED_SDK_VERSION_ERROR:
                        exceptionMessage = "Microsoft Health BandService doesn't support your SDK Version. Please update to latest SDK.";
                        break;
                    case SERVICE_ERROR:
                        exceptionMessage = "Microsoft Health BandService is not available. Please make sure Microsoft Health is installed and that you have the correct permissions.";
                        break;
                    default:
                        exceptionMessage = "Unknown error occured: " + e.getMessage();
                        break;
                }
                System.out.println(exceptionMessage);

            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
            return null;
        }
    }


    public void vibrateBandOnce(){
        try {
            client.getNotificationManager().vibrate(VibrationType.NOTIFICATION_ONE_TONE);
        } catch (BandIOException e) {
            e.printStackTrace();
        }
    }

    public void vibrateBandTwice(){
        try {
            client.getNotificationManager().vibrate(VibrationType.NOTIFICATION_TWO_TONE);
        } catch (BandIOException e) {
            e.printStackTrace();
        }
    }

}
