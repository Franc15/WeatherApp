package org.me.gcu.francis_kikulwe_s2110844.helper;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.me.gcu.francis_kikulwe_s2110844.model.WeatherItem;

import java.lang.reflect.Type;
import java.util.List;

public class SharedPreferenceManager {
    private static final String SHARED_PREF_NAME = "weather_items";

    public static void storeWeatherItems(Context context, List<WeatherItem> weatherItems) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(weatherItems);
        editor.putString("weather_items", json);
        editor.apply();
    }

    public static List<WeatherItem> retrieveWeatherItems(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("weather_items", null);
        Type type = new TypeToken<List<WeatherItem>>() {}.getType();
        return gson.fromJson(json, type);
    }
}
