package org.me.gcu.francis_kikulwe_s2110844.helper;

import android.util.Log;

import org.me.gcu.francis_kikulwe_s2110844.R;
import org.me.gcu.francis_kikulwe_s2110844.model.ForecastItem;
import org.me.gcu.francis_kikulwe_s2110844.model.WeatherItem;

public class Util {

    public static String cleanXmlString(String result) {
        // Get rid of the first tag <?xml version="1.0" encoding="utf-8"?>
        int i = result.indexOf(">");
        result = result.substring(i + 1);
        // Get rid of the 2nd tag <rss version="2.0" xmlns:atom="http://www.w3.org/2005/Atom">
        i = result.indexOf(">");
        result = result.substring(i + 1);
        Log.e("MyTag - cleaned", result);

        // Remove the </rss> tag at the end
        i = result.lastIndexOf("<");
        result = result.substring(0, i).trim();
        Log.e("MyTag - cleaned", result);

        return result;
    }

    public static void parseWeatherForecastData(String weatherString, ForecastItem forecastItem) {
        if (weatherString == null || weatherString.isEmpty()) {
            // Handle empty or null input string
            return;
        }

        String[] parts = weatherString.split(", ");
        for (String part : parts) {
            if (part.contains("Maximum Temperature")) {
                String temp = extractValue(part);
                if (temp != null) {
                    try {
                        forecastItem.setMaxTemperature(temp);
                    } catch (NumberFormatException e) {
                        forecastItem.setMaxTemperature("-1");
                    }
                }
            } else if (part.contains("Minimum Temperature")) {
                String temp = extractValue(part);
                if (temp != null) {
                    try {
                        forecastItem.setMinTemperature(temp);
                    } catch (NumberFormatException e) {
                        forecastItem.setMinTemperature("-1");
                    }
                }
            } else if (part.contains("Wind Direction")) {
                forecastItem.setWindDirection(extractValue(part));
            } else if (part.contains("Wind Speed")) {
                String speed = extractValue(part);
                if (speed != null) {
                    try {
                        forecastItem.setWindSpeed(speed.replaceAll("[^0-9]", ""));
                    } catch (NumberFormatException e) {
                        forecastItem.setWindSpeed("-1");
                    }
                }
            } else if (part.contains("Pressure")) {
                String pressure = extractValue(part);
                if (pressure != null) {
                    try {
                        pressure = pressure.replaceAll("[^0-9]", "");
                        if (!pressure.isEmpty()) {
                            forecastItem.setPressure(pressure);
                        }
                    } catch (NumberFormatException e) {
                        forecastItem.setPressure("-1");
                    }
                }
            } else if (part.contains("Humidity")) {
                String humidityStr = extractValue(part);
                if (humidityStr != null) {
                    try {
                        forecastItem.setHumidity(humidityStr.replaceAll("[^0-9]", ""));
                    } catch (NumberFormatException e) {
                        forecastItem.setHumidity("-1");
                    }
                }
            } else if (part.contains("Sunrise")) {
                forecastItem.setSunrise(extractTime(part));
            } else if (part.contains("Sunset")) {
                forecastItem.setSunset(extractTime(part));
            }
        }
    }

    public static void parseWeatherItemData(String weatherString, WeatherItem weatherItem) {
        if (weatherString == null || weatherString.isEmpty()) {
            // Handle empty or null input string
            return;
        }

        String[] parts = weatherString.split(", ");
        for (String part : parts) {
            if (part.contains("Temperature")) {
                weatherItem.setTemperature(extractValue(part));
            } else if (part.contains("Wind Direction")) {
                weatherItem.setWindDirection(extractValue(part));
            } else if (part.contains("Wind Speed")) {
                String speed = extractValue(part);
                if (speed != null) {
                    try {
                        weatherItem.setWindSpeed(speed.replaceAll("[^0-9]", ""));
                    } catch (NumberFormatException e) {
                        weatherItem.setWindSpeed("-1");
                    }
                }
            } else if (part.contains("Pressure")) {
                String pressure = extractValue(part);
                if (pressure != null) {
                    try {
                        pressure = pressure.replaceAll("[^0-9]", "");
                        if (!pressure.isEmpty()) {
                            weatherItem.setPressure(pressure);
                        }
                    } catch (NumberFormatException e) {
                        weatherItem.setPressure("-1");
                    }
                }
            } else if (part.contains("Humidity")) {
                String humidityStr = extractValue(part);
                if (humidityStr != null) {
                    try {
                        weatherItem.setHumidity(humidityStr.replaceAll("[^0-9]", ""));
                    } catch (NumberFormatException e) {
                        weatherItem.setHumidity("-1");
                    }
                }
            }
        }
    }

    private static String extractValue(String part) {
        int startIndex = part.indexOf(": ");
        if (startIndex != -1) {
            startIndex += 2;
            return part.substring(startIndex);
        }
        return null;
    }

    private static String extractTime(String part) {
        int startIndex = part.indexOf(": ");
        if (startIndex != -1) {
            startIndex += 2;
            return part.substring(startIndex, startIndex + 5);
        }
        return null;
    }

    public static int getWeatherIcon(String weatherDescription) {
        if (weatherDescription == null) {
            return -1;
        }
        String lowerCaseWeatherDescription = weatherDescription.toLowerCase();
        if (lowerCaseWeatherDescription.contains("sunny")) {
            return R.drawable.day_clear;
        } else if (lowerCaseWeatherDescription.contains("clear")) {
            return R.drawable.day_clear;
        } else if (lowerCaseWeatherDescription.contains("partly cloudy") ||
                lowerCaseWeatherDescription.contains("light cloud")) {
            return R.drawable.day_partial_cloud;
        } else if (lowerCaseWeatherDescription.contains("cloudy")) {
            return R.drawable.cloudy;
        } else if (lowerCaseWeatherDescription.contains("overcast")) {
            return R.drawable.overcast;
        } else if (lowerCaseWeatherDescription.contains("mist")) {
            return R.drawable.mist;
        } else if (lowerCaseWeatherDescription.contains("fog")) {
            return R.drawable.fog;
        } else if (lowerCaseWeatherDescription.contains("drizzle")) {
            return R.drawable.day_rain;
        } else if (lowerCaseWeatherDescription.contains("light rain")) {
            return R.drawable.rain;
        } else if (lowerCaseWeatherDescription.contains("heavy rain")) {
            return R.drawable.rain_thunder;
        } else if (lowerCaseWeatherDescription.contains("showers")) {
            return R.drawable.day_rain;
        } else if (lowerCaseWeatherDescription.contains("light snow")) {
            return R.drawable.snow;
        } else if (lowerCaseWeatherDescription.contains("heavy snow")) {
            return R.drawable.snow_thunder;
        } else if (lowerCaseWeatherDescription.contains("snow showers")) {
            return R.drawable.day_snow;
        } else if (lowerCaseWeatherDescription.contains("thunder")) {
            return R.drawable.thunder;
        } else {
            return R.drawable.day_clear;
        }
    }


}
