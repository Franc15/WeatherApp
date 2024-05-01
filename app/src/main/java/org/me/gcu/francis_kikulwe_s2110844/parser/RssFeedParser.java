package org.me.gcu.francis_kikulwe_s2110844.parser;

import android.util.Log;

import org.me.gcu.francis_kikulwe_s2110844.helper.Util;
import org.me.gcu.francis_kikulwe_s2110844.model.ForecastItem;
import org.me.gcu.francis_kikulwe_s2110844.model.WeatherItem;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

public class RssFeedParser {

    static final String PUB_DATE = "pubDate";
    static final String DESCRIPTION = "description";
    static final String CHANNEL = "channel";
    static final String LINK = "link";
    static final String TITLE = "title";
    static final String ITEM = "item";



    public static List<ForecastItem> parseThreeDayForecast(String rssFeed) throws XmlPullParserException {
        List<ForecastItem> list = new ArrayList<ForecastItem>();
        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
        factory.setNamespaceAware(false); // Disable namespace awareness
        XmlPullParser parser = factory.newPullParser();
        InputStream stream = null;
        try {
            // auto-detect the encoding from the stream
            parser.setInput(new StringReader(rssFeed));
            int eventType = parser.getEventType();
            boolean done = false;
            ForecastItem item = null;
            while (eventType != XmlPullParser.END_DOCUMENT && !done) {
                String name = null;
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        break;
                    case XmlPullParser.START_TAG:
                        name = parser.getName();
                        if (name.equalsIgnoreCase(ITEM)) {
                            Log.i("new item", "Create new item");
                            item = new ForecastItem();
                        } else if (item != null) {
                            if (name.equalsIgnoreCase(LINK)) {
                                Log.i("Attribute", "setLink");
                                String link = parser.nextText().trim();
//                                item.setLink(parser.nextText());
                            } else if (name.equalsIgnoreCase(DESCRIPTION)) {
                                Log.i("Attribute", "description");
                                String description = parser.nextText().trim();
                                Util.parseWeatherForecastData(description, item);
//                                item.setDescription(description);
                            } else if (name.equalsIgnoreCase(PUB_DATE)) {
                                Log.i("Attribute", "date");
                                String date = parser.nextText().trim();
//                                item.setPubDate(parser.nextText());
                            } else if (name.equalsIgnoreCase(TITLE)) {
                                Log.i("Attribute", "title");
                                String title = parser.nextText().trim();
                                String[] titleParts = title.split(":");
                                item.setDay(titleParts[0].trim());
                                item.setWeatherDescription(titleParts[1].split(",")[0].trim());
                            }
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        name = parser.getName();
                        Log.i("End tag", name);
                        if (name.equalsIgnoreCase(ITEM) && item != null) {
                            Log.i("Added", item.toString());
                            list.add(item);
                        } else if (name.equalsIgnoreCase(CHANNEL)) {
                            done = true;
                        }
                        break;
                }
                eventType = parser.next();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return list;
    }

    public static WeatherItem parseWeatherItem(String rssFeed) throws XmlPullParserException {
        WeatherItem item = null;
        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
        factory.setNamespaceAware(false); // Disable namespace awareness
        XmlPullParser parser = factory.newPullParser();
        InputStream stream = null;
        try {
            // auto-detect the encoding from the stream
            parser.setInput(new StringReader(rssFeed));
            int eventType = parser.getEventType();
            boolean done = false;
            while (eventType != XmlPullParser.END_DOCUMENT && !done) {
                String name = null;
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        break;
                    case XmlPullParser.START_TAG:
                        name = parser.getName();
                        if (name.equalsIgnoreCase(ITEM)) {
                            Log.i("new item", "Create new item");
                            item = new WeatherItem();
                        } else if (item != null) {
                            if (name.equalsIgnoreCase(LINK)) {
                                Log.i("Attribute", "setLink");
                                String link = parser.nextText().trim();
//                                item.setLink(parser.nextText());
                            } else if (name.equalsIgnoreCase(DESCRIPTION)) {
                                Log.i("Attribute", "description");
                                String description = parser.nextText().trim();
                                Util.parseWeatherItemData(description, item);
//                                item.setDescription(description);
                            } else if (name.equalsIgnoreCase(PUB_DATE)) {
                                Log.i("Attribute", "date");
                                String date = parser.nextText().trim();
                                item.setPubDate(date);
//                                item.setPubDate(parser.nextText());
                            } else if (name.equalsIgnoreCase(TITLE)) {
                                Log.i("Attribute", "title");
                                String title = parser.nextText().trim();
                            }
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        name = parser.getName();
                        Log.i("End tag", name);
                        if (name.equalsIgnoreCase(ITEM) && item != null) {
                            Log.i("Added", item.toString());
                        } else if (name.equalsIgnoreCase(CHANNEL)) {
                            done = true;
                        }
                        break;
                }
                eventType = parser.next();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return item;
    }

}
