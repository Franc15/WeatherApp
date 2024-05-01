// author: francis_kikulwe_s2110844
package org.me.gcu.francis_kikulwe_s2110844;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import org.me.gcu.francis_kikulwe_s2110844.constants.Constants;
import org.me.gcu.francis_kikulwe_s2110844.helper.SharedPreferenceManager;
import org.me.gcu.francis_kikulwe_s2110844.model.ForecastItem;
import org.me.gcu.francis_kikulwe_s2110844.model.WeatherItem;
import org.me.gcu.francis_kikulwe_s2110844.parser.RssFeedParser;
import org.me.gcu.francis_kikulwe_s2110844.service.WeatherFetchingService;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class MyReceiver extends BroadcastReceiver {
    List<WeatherItem> weatherItems = null;

    @Override
    public void onReceive(Context context, Intent intent) {
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
                        SharedPreferenceManager.storeWeatherItems(context, weatherItems);
                    }
                }
            }).start();
        }
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