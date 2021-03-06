package edu.uncc.wins.gestureslive;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.AssetManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class MainActivity extends ActionBarActivity implements StreamListener, ClassificationListener {

    private Button dataBtn;
    private Button bandBtn;
    private TextView txtView;
    private TextView featureLabel;
    private Boolean isStreaming;
    private SensorDataStream myStream;

    public static AssetManager appAssets;

    private boolean hasDialogue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        appAssets = getAssets();
        hasDialogue = false;
        isStreaming = false;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dataBtn = (Button) findViewById(R.id.dataBtn);
        bandBtn = (Button) findViewById(R.id.bandBtn);
        txtView = (TextView) findViewById(R.id.txtView);

        if(Constants.DEMO_MODE) {
            featureLabel = (TextView) findViewById(R.id.demoView);
        }
        else{
            featureLabel = (TextView) findViewById(R.id.featView);
        }


        /**
         * button logic. Nothing fancy, just boolean toggles.
         * A state pattern would be ideal if the app grows
         */
        bandBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isStreaming) {
                    isStreaming = false;
                    myStream.terminateStream();
                    bandBtn.setText("START MS Band stream");
                    dataBtn.setEnabled(true);
                } else {
                    featureLabel.setText("No gestures detected yet ");
                    isStreaming = true;
                    //Start tracking the data
                    buildSystemForBand();
                    myStream.startupStream();
                    bandBtn.setText("STOP MS Band stream");
                    dataBtn.setEnabled(false);
                }
            }
        });


        dataBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isStreaming) {
                    isStreaming = false;
                    myStream.terminateStream();
                    dataBtn.setText("START csv file stream");
                    bandBtn.setEnabled(true);
                } else {
                    featureLabel.setText("No gestures detected yet ");
                    isStreaming = true;
                    //Start tracking the data
                    buildSystemForCSV();
                    myStream.startupStream();
                    dataBtn.setText("STOP csv file stream");
                    bandBtn.setEnabled(false);
                }
            }
        });

    }


    /**
     * Sets up a sensor stream from a Microsoft Band
     */
    public void buildSystemForBand(){
        /*
                SensorDataStream from MSBand (reports to...)
                StdDevSegmentor (who listens to ^, and reports to...)
                SegmentProcessor (who reports to...)
                FeatureExtractor (who reports to...)
                LogRegClassifier (who notifies the world)
        */

        //Create the data stream
        myStream = new MSBandDataStream(getApplicationContext());

        //Build the rest of the chain-of-responsibility starting with the top link
        //SlidingWindowGestureClassifier myClassifier = new SlidingWindowGestureClassifier();
        KMeansClassifier myClassifier = new KMeansClassifier();
        FeatureExtractor myExtractor = new FeatureExtractor(myClassifier);
        StretchSegmentToPointLength myProcessor = new StretchSegmentToPointLength(myExtractor,128);

        //Custom constructor to pass MSBand for haptic feedback
        PersistentSegmentor mySegmentor = new PersistentSegmentor(myStream,myProcessor);
        //AccMagSegmentor mySegmentor = new AccMagSegmentor((MSBandDataStream) myStream, myStream, myProcessor);
        //StdDevSegmentor mySegmentor = new StdDevSegmentor((MSBandDataStream) myStream, myStream, myProcessor);

        myStream.addListener(mySegmentor);
        myStream.addListener(this);
        myClassifier.addListener(this);

    }


    /**
     * Sets up a virtual sensor stream from a csv file in the assets folder
     */
    public void buildSystemForCSV(){
        /*
                SensorDataStream from csv file (reports to...)
                Segmentor from annotation (who listens to ^, and reports to...)
                SegmentProcessor (who reports to...)
                FeatureExtractor (who reports to...)
                LogRegClassifier (who notifies the world)
        */

        //myStream = new CSVDataStream("trial0.csv", appAssets);
        myStream = new MSBandDataStream();

        //Build the rest of the chain-of-responsibility starting with the top link
        //LogRegClassifier myClassifier = new LogRegClassifier();
        KMeansClassifier myClassifier = new KMeansClassifier();
        //SlidingWindowGestureClassifier myClassifier = new SlidingWindowGestureClassifier();

        FeatureExtractor myExtractor = new FeatureExtractor(myClassifier);
        SegmentProcessor myProcessor = new SegmentProcessor(myExtractor);

        //Create a segmentor that listens to the stream and reports to the processor
        PersistentSegmentor mySegmentor = new PersistentSegmentor(myStream,myProcessor);
        //AccMagSegmentor mySegmentor = new AccMagSegmentor(myStream, myProcessor);
        //SegmentorFromAnnotation mySegmentor = new SegmentorFromAnnotation(myStream, myProcessor);

        myStream.addListener(mySegmentor);
        myStream.addListener(this);
        myClassifier.addListener(this);
    }


    /**
     * Android-provided
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    /**
     * Android-provided.
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void newSensorData(Coordinate newCoordinate) {
        final String toPrint = newCoordinate.toShortString();
        //System.out.print(toPrint);
        this.runOnUiThread(new Runnable() {
            public void run() {
                txtView.setText(toPrint);
            }
        });
    }


    /**
     * As a ClassificationListener, MainActivity.java implements this method
     * and updates the UI with the info
     * @param classification the classification received
     */
    public void didReceiveNewClassification(final String classification) {
        //final variables to be used in inner class
        final String tmpClassification = classification;
        //Log.v("TAG","reached newClassification in mainActivity");
            this.runOnUiThread(new Runnable() {
                public void run() {
                    if(!hasDialogue && Constants.SHOW_DIALOGS)
                        showAlertWithTitleAndMessage(tmpClassification, "");
                    if(Constants.VIBRATE_FOR_GESTURE && bandBtn.isEnabled())
                        ((MSBandDataStream) myStream).vibrateBandOnce();


                    if(Constants.DEMO_MODE) {
                        featureLabel.setText(classification);
                    }
                    else{
                        featureLabel.setText("Previous gesture:\n " + classification);
                    }
                }
            });

    }


    /**
     * convenience method for showing dialogues. handles front-end stuff
     * @param title The top part of the dialogue
     * @param message The inner message of the dialogue
     */
    private void showAlertWithTitleAndMessage(String title, String message){
        Log.v("TAG","reached showAlertWithTitleAndMessage in mainActivity");
        AlertDialog.Builder myBuilder = new AlertDialog.Builder(this);
        AlertDialog alertDialog = myBuilder.create();
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        hasDialogue = false;
                    }
                });
        alertDialog.show();
        hasDialogue = true;
    }
}
