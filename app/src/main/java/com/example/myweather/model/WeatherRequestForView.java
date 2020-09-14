package com.example.myweather.model;

import android.content.Context;

import com.example.myweather.R;

import java.util.Locale;

public class WeatherRequestForView {

    private String temperatureStr;
    private String city;
    private String weather;
    private String pressure;
    private float temperatureF;

    public String getTemperatureStr() {
        return temperatureStr;
    }

    public float getTemperatureF() {
        return temperatureF;
    }

    public void setTemperatureF(float temperatureF) {
        this.temperatureF = temperatureF;
    }

    public void setTemperatureStr(String temperatureStr) {
        this.temperatureStr = temperatureStr;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getWeather() {
        return weather;
    }

    public void setWeather(String weather) {
        this.weather = weather;
    }

    public String getPressure() {
        return pressure;
    }

    public void setPressure(String pressure) {
        this.pressure = pressure;
    }

    public String getHumidity() {
        return humidity;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }

    public String getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(String windSpeed) {
        this.windSpeed = windSpeed;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    private String humidity;
    private String windSpeed;
    private String icon;

    public WeatherRequestForView(WeatherRequest weatherRequest, Context context) {
        this.temperatureStr  = String.format(Locale.getDefault(), "+%.0f", weatherRequest.getMain().getTemp());
        this.temperatureF  = weatherRequest.getMain().getTemp();
        this.city = weatherRequest.getName();
        this.weather = weatherRequest.getWeather()[0].getDescription();
        this.pressure = context.getString(R.string.pressure)+" "
                + String.format(Locale.getDefault(),"%.0f", (weatherRequest.getMain().getPressure()*0.750064))
                +" "+context.getString(R.string.pressure1);
        this.humidity = context.getString(R.string.humidity)+" "
                + String.format(Locale.getDefault(), "%d", weatherRequest.getMain().getHumidity())
                +" "+context.getString(R.string.humidity1);
        this.windSpeed = context.getString(R.string.wind)+" "
                + String.format(Locale.getDefault(), "%.3f", weatherRequest.getWind().getSpeed())
                +" "+context.getString(R.string.wind1);
        this.icon = weatherRequest.getWeather()[0].getIcon();
    }
}
