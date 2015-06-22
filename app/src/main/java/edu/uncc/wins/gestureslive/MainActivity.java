package edu.uncc.wins.gestureslive;

import android.content.res.AssetManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class MainActivity extends ActionBarActivity implements StreamListener {

    private Button goBtn;
    private TextView txtView;
    private Boolean isStreaming;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        isStreaming = false;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        goBtn = (Button) findViewById(R.id.goBtn);
        txtView = (TextView) findViewById(R.id.txtView);
        /*
                SensorDataStream (reports to...)
                Segmentor (who listens to ^, and reports to...)
                SegmentProcessor (who reports to...)
                FeatureExtractor (who reports to...)
                GestureClassifier (who notifies the world)
                 */

        //Create the data stream
        //final SensorDataStream MSStream = new MSBandDataStream(getApplicationContext());

        //Uncomment to use data from the csv File
        AssetManager ast = getAssets();
        final SensorDataStream MSStream = new ExperimentDataStream("raw128length", ast);


        //Build the rest of the chain-of-responsibility starting with the top link
        GestureClassifier myClassifier = new GestureClassifier();
        FeatureExtractor myExtractor = new FeatureExtractor(myClassifier);
        SegmentProcessor myProcessor = new SegmentProcessor(myExtractor);
        //Create a segmentor that listens to the stream and reports to the processor
        Segmentor mySegmentor = new Segmentor(MSStream, myProcessor);

        MSStream.addListener(mySegmentor);
        MSStream.addListener(this);

        goBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isStreaming) {
                    isStreaming = false;
                    MSStream.terminateStream();
                    goBtn.setText("GO");
                }
                else {
                    isStreaming = true;
                    System.out.println("GO!");
                    //Start tracking the data
                    MSStream.startupStream();
                    goBtn.setText("STOP");
                }
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


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
        final String toPrint = newCoordinate.toString();
        //System.out.print(toPrint);
        this.runOnUiThread(new Runnable() {
            public void run() {
                txtView.setText(toPrint);
            }
        });
    }
}
