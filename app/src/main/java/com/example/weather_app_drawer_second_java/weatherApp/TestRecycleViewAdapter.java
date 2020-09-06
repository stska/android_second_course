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
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;

public class TestRecycleViewAdapter extends RecyclerView.Adapter<TestRecycleViewAdapter.ViewHolder> {
    private final String FAV_FLAG = "favBtnState";
    private final String BTN_STATE_CLICKED = "clicked";
    private final String BTN_STATE_CLEAR = "not_clicked";
    private Context mContext;
    private SimpleDraweeView drawImage;
    private Uri uri;
    private String site = "http://openweathermap.org/img/wn/";
    private String type = "@2x.png";

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    private ArrayList<WeatherHistory> dataSource;


    private OnItemClickListener itemClickListener;

    public void setOnItemListener(OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }


    public TestRecycleViewAdapter(ArrayList<WeatherHistory> dataSource, Context mContext) {
        this.dataSource = dataSource;
        this.mContext = mContext;
        Fresco.initialize(mContext);
        SharedPreferencesClass.insertData(mContext, FAV_FLAG, BTN_STATE_CLEAR);

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
    public TestRecycleViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, final int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.test_weather_item, parent, false);
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final int position = (int) view.getTag();
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setTitle(R.string.exclamation)
                        .setMessage("Вы точно хотите удалить эту запись?")
                        .setCancelable(false)
                        .setNegativeButton(R.string.no,

                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        Toast.makeText(mContext, "Нет!", Toast.LENGTH_SHORT).show();
                                    }
                                })
                        .setPositiveButton(R.string.yes,

                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        Toast.makeText(mContext, "Да!", Toast.LENGTH_SHORT).show();
                                        WeatherHistory.weatherHistories.remove(position);
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
    public void onBindViewHolder(@NonNull TestRecycleViewAdapter.ViewHolder holder, int position) {
        holder.getTextView().setText(dataSource.get(position).getCityName());
        holder.getPressureText().setText(dataSource.get(position).getCityPressure());
        holder.getTempText().setText(dataSource.get(position).getCityTmp());
        holder.getWeatherText().setText(dataSource.get(position).getWeatherText());
        holder.getHumidityText().setText(dataSource.get(position).getHumText());
        holder.itemView.setTag(position);
        uri = Uri.parse(site.concat(dataSource.get(position).getIcon()).concat(type));
        drawImage.setImageURI(uri);

        if (WeatherHistory.weatherHistories.get(position).getFavFlag()) {
            holder.addToFavBtn.setImageResource(R.drawable.addtofavorites);
        } else holder.addToFavBtn.setImageResource(R.drawable.removefromfavorites);

    }




    @Override
    public int getItemCount() {
        return WeatherHistory.weatherHistories.size();
    }
}
