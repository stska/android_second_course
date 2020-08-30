package com.example.weather_app_drawer_second_java.weatherApp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weather_app_drawer_second_java.R;

import java.util.ArrayList;
import java.util.List;

public class FavRecycleViewAdapt extends RecyclerView.Adapter<FavRecycleViewAdapt.ViewHolder> {
    private final String FAV_FLAG = "favBtnState";
    private final String BTN_STATE_CLICKED = "clicked";
    private final String BTN_STATE_CLEAR = "not_clicked";
    private Context mContext;

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    private List<WeatherHistory> favListCities = new ArrayList<>();

    private FavRecycleViewAdapt.OnItemClickListener itemClickListener;

    public void setOnItemListener(FavRecycleViewAdapt.OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public FavRecycleViewAdapt(Context mContext) {
        this.mContext = mContext;
        SharedPreferencesClass.insertData(mContext, FAV_FLAG, BTN_STATE_CLEAR);
        if (!WeatherHistory.weatherHistories.isEmpty()) {
            for (int i = 0; i < WeatherHistory.weatherHistories.size(); i++) {
                if (WeatherHistory.weatherHistories.get(i).getFavFlag()) {
                    favListCities.add(WeatherHistory.weatherHistories.get(i));
                }
            }
        }

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView textView;
        private TextView weatherText;
        private TextView pressureText;
        private TextView tempText;
        private TextView humidityText;
        private ImageButton addToFavBtn;


        public ViewHolder(@NonNull final View itemView) {
            super(itemView);

            weatherText = itemView.findViewById(R.id.weatherInfo);
            pressureText = itemView.findViewById(R.id.pressureText);
            tempText = itemView.findViewById(R.id.temperatureText);
            humidityText = itemView.findViewById(R.id.humidityText);

            addToFavBtn = itemView.findViewById(R.id.addToFavBtn);
            addToFavBtn.setOnClickListener(new ImageButton.OnClickListener() {

                @Override
                public void onClick(View view) {
                    String checkFlag = SharedPreferencesClass.getData(mContext, FAV_FLAG);

                    if (checkFlag.contains(BTN_STATE_CLEAR)) {
                        addToFavBtn.setImageResource(R.drawable.addtofavorites);
                        SharedPreferencesClass.deleteData(mContext, FAV_FLAG);
                        SharedPreferencesClass.insertData(mContext, FAV_FLAG, BTN_STATE_CLICKED);
                        WeatherHistory.weatherHistories.get(getAdapterPosition()).setFavFlag(true);
                    } else {
                        addToFavBtn.setImageResource(R.drawable.removefromfavorites);
                        SharedPreferencesClass.deleteData(mContext, FAV_FLAG);
                        SharedPreferencesClass.insertData(mContext, FAV_FLAG, BTN_STATE_CLEAR);
                        WeatherHistory.weatherHistories.get(getAdapterPosition()).setFavFlag(false);
                        System.out.println(getAdapterPosition());
                        favListCities.remove(this);
                    }

                }

            });
            textView = itemView.findViewById(R.id.nameTextView);

        }

        public TextView getHumidityText() {
            return humidityText;
        }

        public TextView getTextView() {
            return textView;
        }

        public TextView getWeatherText() {
            return weatherText;
        }

        public TextView getPressureText() {
            return pressureText;
        }

        public TextView getTempText() {
            return tempText;
        }
    }

    @NonNull
    @Override
    public FavRecycleViewAdapt.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.test_weather_item, parent, false);

        return new FavRecycleViewAdapt.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull FavRecycleViewAdapt.ViewHolder holder, int position) {

        holder.getTextView().setText(favListCities.get(position).getCityName());
        holder.getPressureText().setText(favListCities.get(position).getCityPressure().concat("hPa"));
        holder.getTempText().setText(favListCities.get(position).getCityTmp());
        holder.getWeatherText().setText(favListCities.get(position).getWeatherText());
        holder.getHumidityText().setText(favListCities.get(position).getHumText().concat("%"));
        holder.addToFavBtn.setImageResource(R.drawable.addtofavorites);
    }

    @Override
    public int getItemCount() {
        return favListCities.size();
    }
}
