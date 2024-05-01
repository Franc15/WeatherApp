// author: francis_kikulwe_s2110844

package org.me.gcu.francis_kikulwe_s2110844.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.me.gcu.francis_kikulwe_s2110844.R;
import org.me.gcu.francis_kikulwe_s2110844.helper.Util;
import org.me.gcu.francis_kikulwe_s2110844.model.ForecastItem;

import java.util.List;

public class ForecastAdapter extends RecyclerView.Adapter<ForecastAdapter.ForecastViewHolder> {

    private final Context context;
    private final List<ForecastItem> forecastItems;

    public ForecastAdapter(Context context, List<ForecastItem> forecastItems) {
        this.context = context;
        this.forecastItems = forecastItems;
    }

    @NonNull
    @Override
    public ForecastViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_forecast, parent, false);
        return new ForecastViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ForecastViewHolder holder, int position) {
        ForecastItem forecastItem = forecastItems.get(position);
        holder.bind(forecastItem);
    }

    @Override
    public int getItemCount() {
        return forecastItems.size();
    }

    static class ForecastViewHolder extends RecyclerView.ViewHolder {
        TextView txtForecastDay;
        TextView txtForecastCondition;
        TextView txtForecastTemperature;
        ImageView imgForecastCondition;

        public ForecastViewHolder(@NonNull View itemView) {
            super(itemView);
            txtForecastDay = itemView.findViewById(R.id.text_day);
            txtForecastCondition = itemView.findViewById(R.id.text_weather_condition);
            imgForecastCondition = itemView.findViewById(R.id.image_weather_icon);
            txtForecastTemperature = itemView.findViewById(R.id.text_temperature);
        }

        public void bind(ForecastItem forecastItem) {
            txtForecastDay.setText(forecastItem.getDay());
            txtForecastCondition.setText(forecastItem.getWeatherDescription());
            imgForecastCondition.setImageResource(Util.getWeatherIcon(forecastItem.getWeatherDescription()));
            String maxTemperature = forecastItem.getMaxTemperature();
            String minTemperature = forecastItem.getMinTemperature();

            String maxTempText = (maxTemperature != null) ? maxTemperature.split(" ")[0] : "";
            String minTempText = (minTemperature != null) ? minTemperature.split(" ")[0] : "";

            txtForecastTemperature.setText("H: " + maxTempText + ", L: " + minTempText);

        }
    }
}
