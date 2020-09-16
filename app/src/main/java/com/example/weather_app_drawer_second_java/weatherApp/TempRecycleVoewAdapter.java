package com.example.weather_app_drawer_second_java.weatherApp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weather_app_drawer_second_java.R;

public class TempRecycleVoewAdapter extends RecyclerView.Adapter<TempRecycleVoewAdapter.ViewHolder> {
    String[] temp;
    String[] nightTemp;

    public TempRecycleVoewAdapter(){
       //  temp = App.getContext().getResources().getStringArray(R.array.dayTemp);
        // nightTemp = App.getContext().getResources().getStringArray(R.array.nightTemp);
    }
     String[] dayTempTest = {"1","2","3","4","5"};
    String[]  nightTempTest = {"-1","-2","-3","-4","-5"};

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView dayTemp;
        private TextView nightTemp;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            dayTemp = itemView.findViewById(R.id.dayId);
            nightTemp = itemView.findViewById(R.id.nightId);
        }

        public TextView getDayTemp(){
            return dayTemp;
        }
        public TextView getNightTemp(){
            return nightTemp;
        }
    }

    @NonNull
    @Override
    public TempRecycleVoewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       // View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.test_weather_item,parent,false);
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cells_for_five_days,parent,false);

        return new ViewHolder(v);
    }


    @Override
    public void onBindViewHolder(@NonNull TempRecycleVoewAdapter.ViewHolder holder, int position) {
        holder.getDayTemp().setText("Day: " + dayTempTest[position]);
        holder.getNightTemp().setText("Night: " + nightTempTest[position]);
    }

    @Override
    public int getItemCount() {
        return 5;
    }
}
