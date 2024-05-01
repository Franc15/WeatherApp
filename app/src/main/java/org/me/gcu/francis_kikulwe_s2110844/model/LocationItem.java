package org.me.gcu.francis_kikulwe_s2110844.model;

public class LocationItem {
    private String locationName;
    private String temperature;
    private String weatherDescription;

    public LocationItem() {
    }

    public LocationItem(String locationName, String temperature, String weatherDescription) {
        this.locationName = locationName;
        this.temperature = temperature;
        this.weatherDescription = weatherDescription;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getWeatherDescription() {
        return weatherDescription;
    }

    public void setWeatherDescription(String weatherDescription) {
        this.weatherDescription = weatherDescription;
    }
}
