// author: francis_kikulwe_s2110844
package org.me.gcu.francis_kikulwe_s2110844.ui.forecast;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import org.me.gcu.francis_kikulwe_s2110844.R;
import org.me.gcu.francis_kikulwe_s2110844.adapter.ForecastAdapter;
import org.me.gcu.francis_kikulwe_s2110844.constants.Constants;
import org.me.gcu.francis_kikulwe_s2110844.databinding.FragmentForecastBinding;
import org.me.gcu.francis_kikulwe_s2110844.helper.SharedPreferenceManager;
import org.me.gcu.francis_kikulwe_s2110844.helper.Util;
import org.me.gcu.francis_kikulwe_s2110844.model.Data;
import org.me.gcu.francis_kikulwe_s2110844.model.ForecastItem;
import org.me.gcu.francis_kikulwe_s2110844.model.WeatherItem;
import org.me.gcu.francis_kikulwe_s2110844.parser.RssFeedParser;
import org.me.gcu.francis_kikulwe_s2110844.service.WeatherFetchingService;

import java.util.ArrayList;
import java.util.List;

public class ForecastFragment extends Fragment {

    private FragmentForecastBinding binding;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ForecastViewModel forecastViewModel =
                new ViewModelProvider(this).get(ForecastViewModel.class);

        binding = FragmentForecastBinding.inflate(inflater, container, false);

        View root = binding.getRoot();

        final ViewPager2 viewPager = binding.viewPager;



        List<Data> dataList = new ArrayList<>();
        for (int i =0; i < Constants.LOCATIONS.length; i++) {
            dataList.add(new Data(Constants.LOCATIONS[i]));
        }

        MyFragmentAdapter adapter = new MyFragmentAdapter(this, dataList);
        viewPager.setAdapter(adapter);

        Bundle args = getArguments();
        if (args != null) {
            String locationId = args.getString("location");
            String[] arrayLocations =  Constants.LOCATIONS;
            int position = 0;
            for (int i = 0; i < arrayLocations.length; i++) {
                if (arrayLocations[i].equals(locationId)) {
                    position = i;
                    break;
                }
            }
            viewPager.setCurrentItem(position, false);
        }

        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
            }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private class MyFragmentAdapter extends FragmentStateAdapter {
        private List<Data> data;
        public MyFragmentAdapter(@NonNull Fragment fragment, List<Data> data) {
            super(fragment);
            this.data = data;
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            return MyDataFragment.newInstance(data.get(position));
        }

        @Override
        public int getItemCount() {
            return data.size();
        }
    }

    public static class MyDataFragment extends Fragment {
        private static final String ARG_DATA = "data";

        public static MyDataFragment newInstance(Data data) {
            MyDataFragment fragment = new MyDataFragment();
            Bundle args = new Bundle();
            args.putParcelable(ARG_DATA, data);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_weather_main, container, false);
            // Bind the data to your layout components
            final TextView txtWeatherCondition = view.findViewById(R.id.txtWeatherCondition);
            final TextView txtPubDate = view.findViewById(R.id.txtPubDate);
            final TextView txtLocation = view.findViewById(R.id.txtLocationName);
            final TextView txtTemperature = view.findViewById(R.id.txtTemperature);
            final TextView txtHighLowTemp = view.findViewById(R.id.txtHighLowTemp);
            final TextView txtHumidity = view.findViewById(R.id.txtHumidity);
            final TextView txtWindSpeed = view.findViewById(R.id.txtWind);
            final TextView txtPressure = view.findViewById(R.id.txtPressure);
            final ImageView imgWeatherIcon = view.findViewById(R.id.weatherIcon);
            final RecyclerView recyclerView = view.findViewById(R.id.recyclerViewForecast);

            Data data = getArguments().getParcelable(ARG_DATA);

            // get location name from map using the id as value
            String locationName = Constants.getLocationById().get(data.getId());

            List<WeatherItem> weatherItemList = SharedPreferenceManager.retrieveWeatherItems(requireContext());
            WeatherItem weatherItem = weatherItemList.stream().filter(item -> item.getLocationName().equals(locationName)).findFirst().orElse(null);


            txtWeatherCondition.setText(weatherItem.getWeatherDescription());
            txtPubDate.setText(weatherItem.getPubDate());
            txtLocation.setText(locationName);
            txtTemperature.setText(weatherItem.getTemperature().split(" ")[0]);
            txtPressure.setText(weatherItem.getPressure() + " hPa");
            txtHumidity.setText(weatherItem.getHumidity() + "%");
            txtWindSpeed.setText(weatherItem.getWindSpeed() + " km/h");
            imgWeatherIcon.setImageResource(Util.getWeatherIcon(weatherItem.getWeatherDescription()));
//            txtHighLowTemp.setText("H: " + weatherItem.get() + "°C" + " L: " + weatherItem.getLowTemp() + "°C");
//            List<ForecastItem> forecastItems = new ArrayList<>();
            new Thread(() -> {
                List<ForecastItem> fetchedItems = fetchWeatherData(data.getId());
                requireActivity().runOnUiThread(() -> {
                    ForecastAdapter adapter = new ForecastAdapter(requireContext(), fetchedItems);
                    recyclerView.setAdapter(adapter);
                    recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
                    txtHighLowTemp.setText("H: " + fetchedItems.get(0).getMaxTemperature().split(" ")[0] + " L: " + fetchedItems.get(0).getMinTemperature().split(" ")[0]);
                });
            }).start();
            return view;
        }
    }

    private static List<ForecastItem> fetchWeatherData(String locationId) {
        List<ForecastItem> forecastItems = new ArrayList<>();
        String url = "https://weather-broker-cdn.api.bbci.co.uk/en/forecast/rss/3day/" + locationId;
        String weatherForecastData = WeatherFetchingService.fetchWeatherDataFromAPI(url);
        try {
            forecastItems = RssFeedParser.parseThreeDayForecast(weatherForecastData);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return forecastItems;
    }


    private static List<ForecastItem> onWeatherForecastDataFetched(String weatherForecastData) {
        List<ForecastItem> forecastItems = new ArrayList<>();
        try {
            forecastItems = RssFeedParser.parseThreeDayForecast(weatherForecastData);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return forecastItems;
    }
}

