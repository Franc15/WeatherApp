package org.me.gcu.francis_kikulwe_s2110844.model;

public class RssItem {
    private String title;
    private String link;
    private String description;
    private WeatherInfo weatherInfo;
    private String pubDate;

    public RssItem(String title, String link, String description, String pubDate, WeatherInfo weatherInfo) {
        this.title = title;
        this.link = link;
        this.description = description;
        this.pubDate = pubDate;
        this.weatherInfo = weatherInfo;
    }

    public RssItem() {

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public WeatherInfo getWeatherInfo() {
        return weatherInfo;
    }

    public void setWeatherInfo(WeatherInfo weatherInfo) {
        this.weatherInfo = weatherInfo;
    }

    public String getPubDate() {
        return pubDate;
    }

    public void setPubDate(String pubDate) {
        this.pubDate = pubDate;
    }

    @Override
    public String toString() {
        return "RssItem{" +
                "title='" + title + '\'' +
                ", link='" + link + '\'' +
                ", description='" + description + '\'' +
                ", weatherInfo=" + weatherInfo +
                ", pubDate='" + pubDate + '\'' +
                '}';
    }
}
