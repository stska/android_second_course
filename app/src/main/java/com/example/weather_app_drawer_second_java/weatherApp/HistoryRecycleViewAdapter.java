package com.example.weather_app_drawer_second_java.weatherApp;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weather_app_drawer_second_java.R;
import com.example.weather_app_drawer_second_java.weatherApp.database.WeatherDatabaseRoom;
import com.example.weather_app_drawer_second_java.weatherApp.database.WeatherSourceForDB;
import com.example.weather_app_drawer_second_java.weatherApp.database.WeatherDaoInterface;
import com.example.weather_app_drawer_second_java.weatherApp.database.WeatherEntity;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.List;

public class HistoryRecycleViewAdapter extends RecyclerView.Adapter<HistoryRecycleViewAdapter.ViewHolder> {
    private final String FAV_FLAG = "favBtnState";
    private final String BTN_STATE_CLICKED = "clicked";
    private final String BTN_STATE_CLEAR = "not_clicked";
    private Context mContext;
    private SimpleDraweeView drawImage;
    private Uri uri;
    private String site = "http://openweathermap.org/img/wn/";
    private String type = "@2x.png";
    private WeatherDaoInterface weatherDaoInterface;
    private WeatherDatabaseRoom weatherDatabaseRoom;
    private WeatherSourceForDB weatherSourceForDB;
  //  List <WeatherEntity> weatherEntities;

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    private ArrayList<WeatherHistory> dataSource;


    private OnItemClickListener itemClickListener;

    public void setOnItemListener(OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }


    public HistoryRecycleViewAdapter(ArrayList<WeatherHistory> dataSource, Context mContext, WeatherSourceForDB weatherSourceForDB) {
        this.dataSource = dataSource;
        this.mContext = mContext;
        Fresco.initialize(mContext);
        SharedPreferencesClass.insertData(mContext, FAV_FLAG, BTN_STATE_CLEAR);
        this.weatherSourceForDB = weatherSourceForDB;

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView textView;
        private TextView weatherText;
        private TextView pressureText;
        private TextView tempText;
        private TextView humidityText;
        private ImageButton addToFavBtn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            drawImage = (SimpleDraweeView)itemView.findViewById(R.id.my_image_view);
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
    public HistoryRecycleViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, final int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.test_weather_item, parent, false);
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final int position = (int) view.getTag();
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setTitle("Important notion!")
                        .setMessage("Do you really want to delete this?")
                        .setCancelable(false)
                        .setNegativeButton("No",

                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        Toast.makeText(mContext, "Canceled", Toast.LENGTH_SHORT).show();
                                    }
                                })
                        .setPositiveButton("Yes",

                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        Toast.makeText(mContext, "Deleted", Toast.LENGTH_SHORT).show();
                                        List <WeatherEntity> weatherEntities = weatherSourceForDB.getWeatherEntityList();
                                        WeatherEntity weatherEntity = weatherEntities.get(position);
                                        weatherSourceForDB.deleteWeatherLikeObject(weatherEntity);
                                        notifyDataSetChanged();
                                    }
                                });
                AlertDialog alert = builder.create();
                alert.show();

            }
        });
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryRecycleViewAdapter.ViewHolder holder, int position) {
         List <WeatherEntity> weatherEntities = weatherSourceForDB.getWeatherEntityList();
        WeatherEntity weatherEntity = weatherEntities.get(position);

        holder.getTextView().setText(weatherEntity.cityName);
        holder.getPressureText().setText(weatherEntity.pressureName);
        holder.getTempText().setText(weatherEntity.temperatureName.concat("\u00B0"));
        holder.getWeatherText().setText(weatherEntity.weatherDescriptionName);
        holder.getHumidityText().setText(weatherEntity.humidityName);
        holder.itemView.setTag(position);
        uri = Uri.parse(site.concat(weatherEntity.icon).concat(type));
        drawImage.setImageURI(uri);
        holder.addToFavBtn.setImageResource(R.drawable.removefromfavorites);
         if(weatherEntity.favourite){
             holder.addToFavBtn.setImageResource(R.drawable.addtofavorites);
         }else holder.addToFavBtn.setImageResource(R.drawable.removefromfavorites);

    }




    @Override
    public int getItemCount() {
        return (int) weatherSourceForDB.getCountWeather();
    }
}
