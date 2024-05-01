package org.me.gcu.francis_kikulwe_s2110844;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import org.me.gcu.francis_kikulwe_s2110844.constants.Constants;
import org.me.gcu.francis_kikulwe_s2110844.helper.SharedPreferenceManager;
import org.me.gcu.francis_kikulwe_s2110844.model.ForecastItem;
import org.me.gcu.francis_kikulwe_s2110844.model.WeatherItem;
import org.me.gcu.francis_kikulwe_s2110844.parser.RssFeedParser;
import org.me.gcu.francis_kikulwe_s2110844.service.WeatherFetchingService;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class SplashScreenActivity extends AppCompatActivity {
    private static final String SHARED_PREF_NAME = "weather_items";

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

        checkAndUpdateData();

        // Schedule data updates
        scheduleDataUpdates(this);

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
                Intent startActivity = new Intent(SplashScreenActivity.this, MainActivity.class);
                startActivity(startActivity);
            }
        });
    }

    public static void scheduleDataUpdates(Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(context, MyReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_IMMUTABLE);

        // Set the time when the alarm will first go off
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 8); // 8 AM
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        // Set the interval for data updates (e.g., every 12 hours)
        long intervalMillis = AlarmManager.INTERVAL_HALF_DAY;

        // Schedule the alarm
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), intervalMillis, pendingIntent);
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

    private void checkAndUpdateData() {
        // Check for internet connectivity
        if (isConnectedToInternet()) {
                // Check if SharedPreferences contains items
                SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
                if (!sharedPreferences.contains(SHARED_PREF_NAME)) {
                    // Start the weather fetching service
                    startWeatherFetchingService();
                } else {
                    // delay for 2 seconds before starting the main activity
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            goToMainActivity();
                            finish();
                        }
                    }, 2000);
                }
        } else {
            // Show a toast message indicating no internet connection
            Toast.makeText(this, "No internet connection", Toast.LENGTH_SHORT).show();

            new Thread(() -> {
                try {
                    Thread.sleep(2000);
                    finish();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
        }
    }

    private boolean isConnectedToInternet() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

}