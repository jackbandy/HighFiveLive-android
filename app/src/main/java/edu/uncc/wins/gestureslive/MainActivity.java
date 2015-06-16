package edu.uncc.wins.gestureslive;

import android.gesture.Gesture;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;


public class MainActivity extends ActionBarActivity {

    private Button goBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        goBtn = (Button) findViewById(R.id.goBtn);
        goBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("GO!");


                /*
                SensorDataStream (who reports to...)
                Segmentor (who listens to ^, reports to...)
                SegmentProcessor (who reports to...)
                FeatureExtractor (who reports to...)
                GestureClassifier (who reports to nobody)
                 */

                //Create the data stream
                SensorDataStream MSStream = new MSBandDataStream();
                //Build the rest of the chain-of-responsibility starting with the top link
                GestureClassifier myClassifier = new GestureClassifier();
                FeatureExtractor myExtractor = new FeatureExtractor(myClassifier);
                SegmentProcessor myProcessor = new SegmentProcessor(myExtractor);
                //Create a segmentor that listens to the stream and reports to the processor
                Segmentor mySegmentor = new Segmentor(MSStream, myProcessor);

                //Start tracking the data
                MSStream.startupStream();
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
}
