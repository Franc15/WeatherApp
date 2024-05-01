package org.me.gcu.francis_kikulwe_s2110844.model;

public class WeatherInfo {
    private String day;
    private String condition;
    private int minTemperatureCelsius;
    private int maxTemperatureCelsius;
    private int minTemperatureFahrenheit;
    private int maxTemperatureFahrenheit;

    // Constructors, getters, and setters

    // Constructor
    public WeatherInfo(String day, String condition, int minTemperatureCelsius, int maxTemperatureCelsius,
                       int minTemperatureFahrenheit, int maxTemperatureFahrenheit) {
        this.day = day;
        this.condition = condition;
        this.minTemperatureCelsius = minTemperatureCelsius;
        this.maxTemperatureCelsius = maxTemperatureCelsius;
        this.minTemperatureFahrenheit = minTemperatureFahrenheit;
        this.maxTemperatureFahrenheit = maxTemperatureFahrenheit;
    }

    public static WeatherInfo createWeatherInfoFromString(String weatherString) {
        String[] parts = weatherString.split(", Minimum Temperature: | Maximum Temperature: ");

        String dayAndCondition = parts[0];
        String temperatureInfo = parts[1];

        String[] dayAndConditionParts = dayAndCondition.split(": ");
        String day = dayAndConditionParts[0];
        String condition = dayAndConditionParts[1];

        // Extract temperatures
        String[] temperatureParts = temperatureInfo.split("\\(");
        String minTemp = "", maxTemp = "";
        if (temperatureParts.length > 1) {
            minTemp = temperatureParts[1].replaceAll("[^0-9]", ""); // Extract digits only
            if (temperatureParts.length > 2) {
                maxTemp = temperatureParts[2].replaceAll("[^0-9]", ""); // Extract digits only
            }
        }

        // Extract Celsius and Fahrenheit temperatures
        int minTemperatureCelsius = 0, minTemperatureFahrenheit = 0, maxTemperatureCelsius = 0, maxTemperatureFahrenheit = 0;
        if (!minTemp.isEmpty()) {
            minTemperatureCelsius = Integer.parseInt(minTemp.substring(0, minTemp.indexOf('°')));
            minTemperatureFahrenheit = Integer.parseInt(minTemp.substring(minTemp.indexOf('(') + 1, minTemp.indexOf('°', minTemp.indexOf('°') + 1)));
        }
        if (!maxTemp.isEmpty()) {
            maxTemperatureCelsius = Integer.parseInt(maxTemp.substring(0, maxTemp.indexOf('°')));
            maxTemperatureFahrenheit = Integer.parseInt(maxTemp.substring(maxTemp.indexOf('(') + 1, maxTemp.indexOf('°', maxTemp.indexOf('°') + 1)));
        }

        // Create WeatherInfo object
        WeatherInfo weatherInfo = new WeatherInfo(day, condition, minTemperatureCelsius, maxTemperatureCelsius,
                minTemperatureFahrenheit, maxTemperatureFahrenheit);

        return weatherInfo;
    }

    // Getters and setters
    // Day
    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    // Weather condition
    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    // Minimum temperature in Celsius
    public int getMinTemperatureCelsius() {
        return minTemperatureCelsius;
    }

    public void setMinTemperatureCelsius(int minTemperatureCelsius) {
        this.minTemperatureCelsius = minTemperatureCelsius;
    }

    // Maximum temperature in Celsius
    public int getMaxTemperatureCelsius() {
        return maxTemperatureCelsius;
    }

    public void setMaxTemperatureCelsius(int maxTemperatureCelsius) {
        this.maxTemperatureCelsius = maxTemperatureCelsius;
    }

    // Minimum temperature in Fahrenheit
    public int getMinTemperatureFahrenheit() {
        return minTemperatureFahrenheit;
    }

    public void setMinTemperatureFahrenheit(int minTemperatureFahrenheit) {
        this.minTemperatureFahrenheit = minTemperatureFahrenheit;
    }

    // Maximum temperature in Fahrenheit
    public int getMaxTemperatureFahrenheit() {
        return maxTemperatureFahrenheit;
    }

    public void setMaxTemperatureFahrenheit(int maxTemperatureFahrenheit) {
        this.maxTemperatureFahrenheit = maxTemperatureFahrenheit;
    }

    @Override
    public String toString() {
        return "WeatherInfo{" +
                "day='" + day + '\'' +
                ", condition='" + condition + '\'' +
                ", minTemperatureCelsius=" + minTemperatureCelsius +
                ", maxTemperatureCelsius=" + maxTemperatureCelsius +
                ", minTemperatureFahrenheit=" + minTemperatureFahrenheit +
                ", maxTemperatureFahrenheit=" + maxTemperatureFahrenheit +
                '}';
    }
}
