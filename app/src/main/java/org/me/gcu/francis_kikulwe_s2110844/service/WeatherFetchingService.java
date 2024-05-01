package org.me.gcu.francis_kikulwe_s2110844.service;

import android.util.Log;


import org.me.gcu.francis_kikulwe_s2110844.helper.Util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class WeatherFetchingService {

    public static String fetchWeatherDataFromAPI(String locationUrl) {
        String result = "";
        String url = locationUrl;
        URL aurl;
        URLConnection yc;
        BufferedReader in = null;
        String inputLine = "";

        try {
            aurl = new URL(url);
            yc = aurl.openConnection();
            in = new BufferedReader(new InputStreamReader(yc.getInputStream()));
            while ((inputLine = in.readLine()) != null) {
                result = result + inputLine;
                Log.e("MyTag", inputLine);
            }
            in.close();
        } catch (IOException ae) {
            Log.e("MyTag", "ioexception");
        }

        result = Util.cleanXmlString(result);

        return result;
    }

}
