package org.me.gcu.francis_kikulwe_s2110844;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import org.me.gcu.francis_kikulwe_s2110844.constants.Constants;
import org.me.gcu.francis_kikulwe_s2110844.helper.SharedPreferenceManager;
import org.me.gcu.francis_kikulwe_s2110844.model.ForecastItem;
import org.me.gcu.francis_kikulwe_s2110844.model.WeatherItem;
import org.me.gcu.francis_kikulwe_s2110844.parser.RssFeedParser;
import org.me.gcu.francis_kikulwe_s2110844.service.WeatherFetchingService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class SplashScreenActivity extends AppCompatActivity {

    private ImageView imageView;
    private TextView textView;
    private List<WeatherItem> weatherItems = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_splash_screen);

        imageView = findViewById(R.id.app_logo);
        textView = findViewById(R.id.app_name_text);

        Animation fadeInAnimation = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        imageView.startAnimation(fadeInAnimation);

        Animation bounceFromTopAnimation = AnimationUtils.loadAnimation(this, R.anim.bounce_from_top);
        textView.startAnimation(bounceFromTopAnimation);

        // Start the weather fetching service
        startWeatherFetchingService();

//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                Intent startActivity = new Intent(SplashScreenActivity.this, BottomNavActivity.class);
//                startActivity(startActivity);
//
//                finish();
//            }
//        }, 2000);
    }

    private void startWeatherFetchingService() {
        Map<String, String> locationMap = Constants.getLocationIdByName();

        AtomicInteger completedThreads = new AtomicInteger(0);

        for (String location : locationMap.keySet()) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    String url = "https://weather-broker-cdn.api.bbci.co.uk/en/forecast/rss/3day/" + locationMap.get(location);
                    String weatherForecastData = WeatherFetchingService.fetchWeatherDataFromAPI(url);
                    List<ForecastItem> threeDayForecastItems= onWeatherForecastDataFetched(weatherForecastData);
                    url = "https://weather-broker-cdn.api.bbci.co.uk/en/observation/rss/" + locationMap.get(location);
                    String weatherItemData = WeatherFetchingService.fetchWeatherDataFromAPI(url);
                    WeatherItem weatherItem = onWeatherItemDataFetched(weatherItemData);
                    weatherItem.setWeatherDescription(threeDayForecastItems.get(0).getWeatherDescription());
                    weatherItem.setLocationName(location);
                    if (completedThreads.incrementAndGet() == locationMap.size()) {
                        SharedPreferenceManager.storeWeatherItems(SplashScreenActivity.this, weatherItems);
                        goToMainActivity();

                        finish();
                    }
                }
            }).start();
        }

    }

    public void goToMainActivity() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Intent startActivity = new Intent(SplashScreenActivity.this, BottomNavActivity.class);
                startActivity(startActivity);
            }
        });
    }


    public List<ForecastItem> onWeatherForecastDataFetched(String weatherData) {
        String weatherDataString = weatherData;

        // Parse the XML data
        try {
            List<ForecastItem> threeDayForecastList = RssFeedParser.parseThreeDayForecast(weatherDataString);
            return threeDayForecastList;
        } catch (Exception e) {
            Log.e("MyTag", e.getMessage());
            return null;
        }
    }

    public WeatherItem onWeatherItemDataFetched(String weatherData) {
        String weatherDataString = weatherData;

        // Parse the XML data
        try {
            WeatherItem weatherItem = RssFeedParser.parseWeatherItem(weatherDataString);
            weatherItems.add(weatherItem);
            return weatherItem;
        } catch (Exception e) {
            Log.e("MyTag", e.getMessage());
            return null;
        }
    }


}