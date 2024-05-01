/*  Starter project for Mobile Platform Development in main diet 2023/2024
    You should use this project as the starting point for your assignment.
    This project simply reads the data from the required URL and displays the
    raw data in a TextField
*/

//
// Name                 Francis Steven Kikulwe
// Student ID           s2110844
// Programme of Study   Computing
//

// UPDATE THE PACKAGE NAME to include your Student Identifier
package org.me.gcu.francis_kikulwe_s2110844;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import org.me.gcu.francis_kikulwe_s2110844.adapter.DailyAdapter;
import org.me.gcu.francis_kikulwe_s2110844.model.Daily;
import org.me.gcu.francis_kikulwe_s2110844.parser.RssFeedParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements OnClickListener
{
    private TextView rawDataDisplay;
    private Button startButton;
    private String result;
    private String url1="";
    private String urlSource="https://weather-broker-cdn.api.bbci.co.uk/en/forecast/rss/3day/2643123";

    private RecyclerView.Adapter adapterDaily;
    private RecyclerView recyclerViewDaily;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_main);
        // Set up the raw links to the graphical components
//        rawDataDisplay = (TextView)findViewById(R.id.rawDataDisplay);
//        startButton = (Button)findViewById(R.id.startButton);
//        startButton.setOnClickListener(this);

        // More Code goes here
        initRecyclerView();
    }

    private void initRecyclerView() {
        recyclerViewDaily = findViewById(R.id.view1);
        //recyclerViewDaily.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        int numberOfColumns = 3; // Change this to the desired number of columns
        GridLayoutManager layoutManager = new GridLayoutManager(this, numberOfColumns);
        recyclerViewDaily.setLayoutManager(layoutManager);
        
        ArrayList<Daily> dailyList = new ArrayList<>();
        dailyList.add(new Daily("Monday", "Rain", "12°C", "15°C"));
        dailyList.add(new Daily("Tuesday", "Sunny", "10°C", "14°C"));
        dailyList.add(new Daily("Wednesday", "Cloudy", "11°C", "16°C"));

        adapterDaily = new DailyAdapter(dailyList);
        recyclerViewDaily.setAdapter(adapterDaily);
    }

    public void onClick(View aview)
    {
        startProgress();
    }

    public void startProgress()
    {
        // Run network access on a separate thread;
        new Thread(new Task(urlSource)).start();
    } //

    // Need separate thread to access the internet resource over network
    // Other neater solutions should be adopted in later iterations.
    private class Task implements Runnable
    {
        private String url;

        public Task(String aurl)
        {
            url = aurl;
        }
        @Override
        public void run()
        {

            URL aurl;
            URLConnection yc;
            BufferedReader in = null;
            String inputLine = "";


            Log.e("MyTag","in run");

            try
            {
                Log.e("MyTag","in try");
                aurl = new URL(url);
                yc = aurl.openConnection();
                in = new BufferedReader(new InputStreamReader(yc.getInputStream()));
                while ((inputLine = in.readLine()) != null)
                {
                    result = result + inputLine;
                    Log.e("MyTag",inputLine);

                }
                in.close();
            }
            catch (IOException ae)
            {
                Log.e("MyTag", "ioexception");
            }

            //Get rid of the first tag <?xml version="1.0" encoding="utf-8"?>
            int i = result.indexOf(">");
            result = result.substring(i+1);
            //Get rid of the 2nd tag <rss version="2.0" xmlns:atom="http://www.w3.org/2005/Atom">
            i = result.indexOf(">");
            result = result.substring(i+1);
            Log.e("MyTag - cleaned",result);

            // remove the </rss> tag at the end
            i = result.lastIndexOf("<");
            result = result.substring(0, i);
            Log.e("MyTag - cleaned",result);

            //
            // Now that you have the xml data you can parse it
            //
            try {
                RssFeedParser.parseThreeDayForecast(result);
            } catch (Exception e) {
//                Log.e("MyTag", "XmlPullParserException");
                Log.e("MyTag", e.getMessage());
//                throw new RuntimeException(e);
            }


            // Now update the TextView to display raw XML data
            // Probably not the best way to update TextView
            // but we are just getting started !

            MainActivity.this.runOnUiThread(new Runnable()
            {
                public void run() {
                    Log.d("UI thread", "I am the UI thread");
                    rawDataDisplay.setText(result);
                }
            });
        }

    }

}
