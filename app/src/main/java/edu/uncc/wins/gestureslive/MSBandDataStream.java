package edu.uncc.wins.gestureslive;

import android.os.AsyncTask;

import com.microsoft.band.BandClient;
import com.microsoft.band.BandClientManager;
import com.microsoft.band.BandException;
import com.microsoft.band.BandIOException;
import com.microsoft.band.BandInfo;
import com.microsoft.band.ConnectionState;
import com.microsoft.band.sensors.BandAccelerometerEvent;
import com.microsoft.band.sensors.BandAccelerometerEventListener;
import com.microsoft.band.sensors.SampleRate;

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


    public MSBandDataStream(){
        super();
        client = null;
    }


    public void startupStream() {
        new appTask().execute();
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
    public double[][] getCoordinateCache() {
        return new double[0][];
    }


    /**
     * Microsoft-provided
     * EventListener method for each time the accelerometer reports new data
     */
    private BandAccelerometerEventListener mAccelerometerEventListener = new BandAccelerometerEventListener() {
        @Override
        public void onBandAccelerometerChanged(final BandAccelerometerEvent event) {
            double accX, accY, accZ;
            accX = accY = accZ = 0;

            if (event != null) {
                accX = event.getAccelerationX();
                accY = event.getAccelerationY();
                accZ = event.getAccelerationZ();
            }

            double[] toPass = new double[]{accX,accY,accZ};
            for(StreamListener myListener: getMyListeners()){
                myListener.newSensorData(toPass);
            }
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
            client = BandClientManager.getInstance().create(null, devices[0]);
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
    private class appTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
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
}
