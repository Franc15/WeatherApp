// author: francis_kikulwe_s2110844

package org.me.gcu.francis_kikulwe_s2110844.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.me.gcu.francis_kikulwe_s2110844.R;
import org.me.gcu.francis_kikulwe_s2110844.helper.Util;
import org.me.gcu.francis_kikulwe_s2110844.model.WeatherItem;

import java.util.List;

public class LocationWeatherAdapter extends RecyclerView.Adapter<LocationWeatherAdapter.ViewHolder> {
    private final List<WeatherItem> weatherItemList;
    private final OnItemClickListener listener;

    public LocationWeatherAdapter(List<WeatherItem> weatherItemList, OnItemClickListener listener) {
        this.weatherItemList = weatherItemList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_location_weather, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        WeatherItem weatherItem = weatherItemList.get(position);
        holder.bind(weatherItem);
    }

    @Override
    public int getItemCount() {
        return weatherItemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        public void bind(final WeatherItem locationWeather) {
            TextView locationNameTextView = itemView.findViewById(R.id.location_name);
            TextView locationTemperatureTextView = itemView.findViewById(R.id.location_temperature);
            TextView locationWeatherDescriptionTextView = itemView.findViewById(R.id.location_weather_description);
            ImageView locationWeatherIconImageView = itemView.findViewById(R.id.weather_icon);

            locationNameTextView.setText(locationWeather.getLocationName());
            locationTemperatureTextView.setText(locationWeather.getTemperature().split(" ")[0].trim());
            locationWeatherDescriptionTextView.setText(locationWeather.getWeatherDescription());
            locationWeatherIconImageView.setImageResource(Util.getWeatherIcon(locationWeather.getWeatherDescription()));

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onItemClick(locationWeather);
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(WeatherItem locationWeather);
    }
}
