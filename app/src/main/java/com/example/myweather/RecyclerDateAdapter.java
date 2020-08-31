package com.example.myweather;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myweather.model.Daily;

import java.util.ArrayList;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;



public class RecyclerDateAdapter extends RecyclerView.Adapter<RecyclerDateAdapter.ViewHolder> {

    private ArrayList<Daily> data;
    private IRVOnItemClick onItemClick;

    public RecyclerDateAdapter(ArrayList<Daily> data, IRVOnItemClick onItemClick){
        this.data = data;
        this.onItemClick = onItemClick;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rv_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Daily str = data.get(position);
        holder.setTextToTextView(str);
        holder.setOnClickForItem(str);
    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        private TextView tvDay;
        private TextView tvTemperature;
        private ImageView ivDay;

        public ViewHolder(@NonNull View itemView){
            super(itemView);
            tvDay = itemView.findViewById(R.id.tvDay);
            tvTemperature = itemView.findViewById(R.id.tvTemperature);
            ivDay = itemView.findViewById(R.id.ivDay);
        }

        public void setTextToTextView(Daily daily){
            @SuppressLint("SimpleDateFormat")
            String date = new java.text.SimpleDateFormat("dd MMMM").format(new java.util.Date (daily.getDt()*1000));
            tvDay.setText(date);
            String temp = String.format(Locale.getDefault(), "+%.0f", daily.getTemp().getDay());
            tvTemperature.setText(temp);
            String icon = daily.getWeather()[0].getIcon();
            if (icon.equals("01d") || icon.equals("01n")) {
                ivDay.setImageResource(R.drawable.sunny_sunshine_weather_64);
            } else if (icon.equals("02d") || icon.equals("02n")) {
                ivDay.setImageResource(R.drawable.sunny_sun_cloud_time_time_64);
            } else if (icon.equals("03d") || icon.equals("03n")) {
                ivDay.setImageResource(R.drawable.cloudy_cloud_weather_64);
            } else if (icon.equals("04d") || icon.equals("04n")) {
                ivDay.setImageResource(R.drawable.overcast_cloudy_cloud_weather_64);
            } else if (icon.equals("09d") || icon.equals("09n")) {
                ivDay.setImageResource(R.drawable.rain_wather_cloud_cloudyweather_lluvi_64);
            } else if (icon.equals("10d") || icon.equals("10n")) {
                ivDay.setImageResource(R.drawable.rain_clouds_rain_cloudyweather_64);
            } else if (icon.equals("11d") || icon.equals("11n")) {
                ivDay.setImageResource(R.drawable.weather_storms_storm_rain_thunder_64);
            } else if (icon.equals("13d") || icon.equals("13n")) {
                ivDay.setImageResource(R.drawable.littlesnow_cloud_weather_64);
            } else if (icon.equals("50d") || icon.equals("50n")) {
                ivDay.setImageResource(R.drawable.fog_weather_64);
            }
        }

        void setOnClickForItem(final Daily daily) {
            tvDay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(onItemClick != null) {
                        onItemClick.onItemClicked(daily);
                    }
                }
            });
            tvTemperature.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(onItemClick != null) {
                        onItemClick.onItemClicked(daily);
                    }
                }
            });
        }

    }
}
