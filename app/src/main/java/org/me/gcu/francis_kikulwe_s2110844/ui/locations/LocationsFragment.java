package org.me.gcu.francis_kikulwe_s2110844.ui.locations;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.me.gcu.francis_kikulwe_s2110844.R;
import org.me.gcu.francis_kikulwe_s2110844.adapter.LocationWeatherAdapter;
import org.me.gcu.francis_kikulwe_s2110844.constants.Constants;
import org.me.gcu.francis_kikulwe_s2110844.databinding.FragmentLocationsBinding;
import org.me.gcu.francis_kikulwe_s2110844.helper.SharedPreferenceManager;
import org.me.gcu.francis_kikulwe_s2110844.model.WeatherItem;
import org.me.gcu.francis_kikulwe_s2110844.ui.forecast.ForecastFragment;

import java.util.ArrayList;
import java.util.List;


public class LocationsFragment extends Fragment implements LocationWeatherAdapter.OnItemClickListener {
    private FragmentLocationsBinding binding;
    private EditText locationEditText;
    List<WeatherItem> weatherItemList = new ArrayList<>();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentLocationsBinding.inflate(inflater, container, false);

        locationEditText = binding.searchLocations;

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        weatherItemList = SharedPreferenceManager.retrieveWeatherItems(requireContext());

        locationEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Handle text change here
                String searchText = charSequence.toString();
                // Perform filtering based on the search text
                List<WeatherItem> filteredWeatherItems = null;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                    filteredWeatherItems = weatherItemList.stream().filter(weatherItem -> weatherItem.getLocationName().toLowerCase().contains(searchText.toLowerCase())).toList();
                } else {
                    filteredWeatherItems = new ArrayList<>();
                    for (WeatherItem weatherItem : weatherItemList) {
                        if (weatherItem.getLocationName().toLowerCase().contains(searchText.toLowerCase())) {
                            filteredWeatherItems.add(weatherItem);
                        }
                    }
                }
                binding.locationsRecyclerView.setAdapter(new LocationWeatherAdapter(filteredWeatherItems, LocationsFragment.this));
                binding.locationsRecyclerView.getAdapter().notifyDataSetChanged();
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }

        });

        binding.locationsRecyclerView.setLayoutManager(new GridLayoutManager(requireContext(), 2));
        binding.locationsRecyclerView.setAdapter(new LocationWeatherAdapter(weatherItemList, this));

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


    @Override
    public void onItemClick(WeatherItem locationWeather) {
        String id = Constants.getLocationIdByName().get(locationWeather.getLocationName());
        NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_activity_bottom_nav);
        Bundle args = new Bundle();
        args.putString("location", id);
        navController.navigate(R.id.action_navigation_locations_to_navigation_forecast, args);

    }
}