package com.example.weather_app_drawer_second_java.ui.gallery;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weather_app_drawer_second_java.R;
import com.example.weather_app_drawer_second_java.weatherApp.TestRecycleViewAdapter;

import com.example.weather_app_drawer_second_java.weatherApp.WeatherHistory;


public class GalleryFragment extends Fragment {

    private GalleryViewModel galleryViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        final String test = "";
        if (!WeatherHistory.weatherHistories.isEmpty()) {
            for (int i = 0; i < WeatherHistory.weatherHistories.size(); i++) {
                System.out.println(WeatherHistory.weatherHistories.get(i).getCityName());

            }
            WeatherHistory.weatherHistories.get(0).getCityName();
        }
        galleryViewModel =
                ViewModelProviders.of(this).get(GalleryViewModel.class);
        View root = inflater.inflate(R.layout.fragment_gallery, container, false);
        final TextView textView = root.findViewById(R.id.text_gallery);
        galleryViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(test);
            }
        });

        initRecycleView(root);
        return root;
    }

    private FragmentActivity myContext;

    private void initRecycleView(View view) {
        RecyclerView recyclerView = view.findViewById(R.id.cityWeatherRecycleView);
        recyclerView.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(myContext);
        recyclerView.setLayoutManager(layoutManager);

        TestRecycleViewAdapter adapter = new TestRecycleViewAdapter(WeatherHistory.weatherHistories, getContext());
        recyclerView.setAdapter(adapter);
    }
}