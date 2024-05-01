package org.me.gcu.francis_kikulwe_s2110844.ui.maps;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.me.gcu.francis_kikulwe_s2110844.R;
import org.me.gcu.francis_kikulwe_s2110844.constants.Constants;
import org.me.gcu.francis_kikulwe_s2110844.ui.forecast.ForecastFragment;

import java.util.Map;

public class MapsFragment extends Fragment {

    private GoogleMap googleMap;
    private InfoWindowAdapter infoWindowAdapter;

    private OnMapReadyCallback callback = new OnMapReadyCallback() {
        @Override
        public void onMapReady(GoogleMap googleMap) {
            MapsFragment.this.googleMap = googleMap;
            infoWindowAdapter = new InfoWindowAdapter(getLayoutInflater());
            googleMap.setInfoWindowAdapter(infoWindowAdapter);

            Map<String, String> locationLatLng = Constants.getLocationLatLng();

            locationLatLng.forEach((location, latLng) -> {
                String[] latLngArray = latLng.split(", ");
                LatLng latLngObj = new LatLng(Double.parseDouble(latLngArray[0]), Double.parseDouble(latLngArray[1]));
                Marker marker = googleMap.addMarker(new MarkerOptions().position(latLngObj).title(location));
                marker.setTag(location); // Set the location as a tag for later use
            });

            LatLng glasgow = new LatLng(55.8642, -4.2518);
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(glasgow));

            googleMap.setOnMarkerClickListener(marker -> {
                infoWindowAdapter.setLocation(marker.getTag().toString());
                return false;
            });
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_maps, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }
    }

    private class InfoWindowAdapter implements GoogleMap.InfoWindowAdapter {
        private final View view;
        private final TextView locationName;
        private final Button viewWeatherButton;

        InfoWindowAdapter(LayoutInflater inflater) {
            view = inflater.inflate(R.layout.info_window, null);
            locationName = view.findViewById(R.id.location_name);
            viewWeatherButton = view.findViewById(R.id.view_weather_button);
        }

        void setLocation(String location) {
            locationName.setText("GCU " + location + " Campus");
            viewWeatherButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getContext(), ForecastFragment.class);
                    String id = Constants.getLocationIdByName().get(location);
                    intent.putExtra("location", id);
                    startActivity(intent);
                }
            });
        }

        @Override
        public View getInfoWindow(Marker marker) {
            return null;
        }

        @Override
        public View getInfoContents(Marker marker) {
            return view;
        }
    }
}