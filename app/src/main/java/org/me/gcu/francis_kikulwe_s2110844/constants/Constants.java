// author: francis_kikulwe_s2110844

package org.me.gcu.francis_kikulwe_s2110844.constants;

import java.util.Map;

public class Constants {
    public static final String BASE_URL_3_DAY = "https://weather-broker-cdn.api.bbci.co.uk/en/forecast/rss/3day/";
    public static final String BASE_URL_OBSERVATION = "https://weather-broker-cdn.api.bbci.co.uk/en/observation/rss/";
    public static final String LOCATION_GLASGOW = "2648579";
    public static final String LOCATION_MAURITIUS = "934154";
    public static final String LOCATION_LONDON = "2643743";
    public static final String LOCATION_NEW_YORK = "5128581";
    public static final String LOCATION_OMAN = "287286";
    public static final String LOCATION_BANGLADESH = "1185241";

    public static final String[] LOCATIONS = {
            LOCATION_MAURITIUS,
            LOCATION_GLASGOW,
            LOCATION_LONDON,
            LOCATION_NEW_YORK,
            LOCATION_OMAN,
            LOCATION_BANGLADESH
    };

    public static Map<String, String> getLocationIdByName() {
        return Map.of(
                "Glasgow", LOCATION_GLASGOW,
                "Mauritius", LOCATION_MAURITIUS,
                "London", LOCATION_LONDON,
                "New York", LOCATION_NEW_YORK,
                "Oman", LOCATION_OMAN,
                "Bangladesh", LOCATION_BANGLADESH
        );
    }

    public static Map<String, String> getLocationById() {
        return Map.of(
                LOCATION_GLASGOW, "Glasgow",
                LOCATION_MAURITIUS, "Mauritius",
                LOCATION_LONDON, "London",
                LOCATION_NEW_YORK, "New York",
                LOCATION_OMAN, "Oman",
                LOCATION_BANGLADESH, "Bangladesh"
        );
    }

    public static Map<String, String> getLocationLatLng() {
        return Map.of(
                "Glasgow", "55.8642, -4.2518",
                "Mauritius", "-20.3484, 57.5522",
                "London", "51.5074, -0.1278",
                "New York", "40.7128, -74.0060",
                "Oman", "21.4735, 55.9754",
                "Bangladesh", "23.6850, 90.3563"
        );
    }
}
