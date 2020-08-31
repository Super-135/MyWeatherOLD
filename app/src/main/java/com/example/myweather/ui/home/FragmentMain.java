package com.example.myweather.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.example.myweather.R;
import com.example.myweather.model.WeatherRequest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Locale;

import javax.net.ssl.HttpsURLConnection;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class FragmentMain extends Fragment {

    private TextView textViewWind;
    private TextView tViewHumidity;
    private TextView tViewPressure;
    private TextView tViewWeather;
    private TextView tViewTemperature;
    private MaterialButton btnSendWeather;
    private ImageView iViewIcons;
    private ImageView iViewCity;
    private TextView tViewCiy;
    private String lang;
    private static final String TAG = "WEATHER";
    private static final String WEATHER_URL_PREFIX = "https://api.openweathermap.org/data/2.5/weather?q=Москва&";
    private static final String WEATHER_URL_POSTFIX = "&units=metric&appid=";

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setRetainInstance(true);
        lang = Locale.getDefault().getISO3Language().substring(0,2);
        findView(view);
        setOniViewCurrent();
        SetOnSelectCity();
        getWeather();
    }

    private void getWeather() {
        try {
            final URL uri = new URL(WEATHER_URL_PREFIX + "lang="+ lang + WEATHER_URL_POSTFIX + "c7f79a8f3dde991ba5771bba492c90d7");
            final Handler handler = new Handler(); // Запоминаем основной поток
            new Thread(new Runnable() {
                public void run() {
                    HttpsURLConnection urlConnection = null;
                    try {
                        urlConnection = (HttpsURLConnection) uri.openConnection();
                        urlConnection.setRequestMethod("GET"); // установка метода получения данных -GET
                        urlConnection.setReadTimeout(10000); // установка таймаута - 10 000 миллисекунд
                        BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream())); // читаем  данные в поток
                        String result = getLines(in);
                        // преобразование данных запроса в модель
                        Gson gson = new Gson();
                        final WeatherRequest weatherRequest = gson.fromJson(result, WeatherRequest.class);
                        // Возвращаемся к основному потоку
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                displayWeather(weatherRequest);
                            }
                        });
                    } catch (Exception e) {
                        Log.e(TAG, "Fail connection", e);
                        e.printStackTrace();
                    } finally {
                        if (null != urlConnection) {
                            urlConnection.disconnect();
                        }
                    }
                }
            }).start();
        } catch (MalformedURLException e) {
            Log.e(TAG, "Fail URI", e);
            e.printStackTrace();
        }
    }

    private String getLines(BufferedReader in) {
        StringBuilder rawData = new StringBuilder(1024);
        String tempVariable;
        while (true) {
            try {
                tempVariable = in.readLine();
                if (tempVariable == null) break;
                rawData.append(tempVariable).append("\n");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return rawData.toString();
    }

    private void displayWeather(WeatherRequest weatherRequest){
        String temperatureValue = String.format(Locale.getDefault(), "+%.0f", weatherRequest.getMain().getTemp());
        tViewTemperature.setText(temperatureValue);
        tViewCiy.setText(weatherRequest.getName());
        tViewWeather.setText(weatherRequest.getWeather()[0].getDescription());
        String pressureText = getString(R.string.pressure)+" "
                + String.format(Locale.getDefault(),"%.0f", (weatherRequest.getMain().getPressure()*0.750064))
                +" "+getString(R.string.pressure1);
        tViewPressure.setText(pressureText);
        String humidityStr = getString(R.string.humidity)+" "
                + String.format(Locale.getDefault(), "%d", weatherRequest.getMain().getHumidity())
                +" "+getString(R.string.humidity1);
        tViewHumidity.setText(humidityStr);
        String windSpeedStr = getString(R.string.wind)+" "
                + String.format(Locale.getDefault(), "%.3f", weatherRequest.getWind().getSpeed())
                +" "+getString(R.string.wind1);
        textViewWind.setText(windSpeedStr);
        String icon = weatherRequest.getWeather()[0].getIcon();
        if (icon.equals("01d") || icon.equals("01n")) {
            iViewIcons.setImageResource(R.drawable.sunny_sunshine_weather_64);
        } else if (icon.equals("02d") || icon.equals("02n")) {
            iViewIcons.setImageResource(R.drawable.sunny_sun_cloud_time_time_64);
        } else if (icon.equals("03d") || icon.equals("03n")) {
            iViewIcons.setImageResource(R.drawable.cloudy_cloud_weather_64);
        } else if (icon.equals("04d") || icon.equals("04n")) {
            iViewIcons.setImageResource(R.drawable.overcast_cloudy_cloud_weather_64);
        } else if (icon.equals("09d") || icon.equals("09n")) {
            iViewIcons.setImageResource(R.drawable.rain_wather_cloud_cloudyweather_lluvi_64);
        } else if (icon.equals("10d") || icon.equals("10n")) {
            iViewIcons.setImageResource(R.drawable.rain_clouds_rain_cloudyweather_64);
        } else if (icon.equals("11d") || icon.equals("11n")) {
            iViewIcons.setImageResource(R.drawable.weather_storms_storm_rain_thunder_64);
        } else if (icon.equals("13d") || icon.equals("13n")) {
            iViewIcons.setImageResource(R.drawable.littlesnow_cloud_weather_64);
        } else if (icon.equals("50d") || icon.equals("50n")) {
            iViewIcons.setImageResource(R.drawable.fog_weather_64);
        }
    }

    private void SetOnSelectCity() {
        iViewCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, R.string.snackbar_text, Snackbar.LENGTH_LONG).show();
            }
        });
    }

    private void setOniViewCurrent() {
        btnSendWeather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_TEXT, tViewCiy.getText().toString()+ "  "+ tViewTemperature.getText().toString());
                startActivity(Intent.createChooser(shareIntent, "Share text"));
            }
        });
    }

    private void findView(View view) {
        tViewTemperature = view.findViewById(R.id.tViewTemperature);
        btnSendWeather = view.findViewById(R.id.btnSendWeather);
        iViewCity = view.findViewById(R.id.iViewCity);
        tViewCiy = view.findViewById(R.id.tViewCiy);
        tViewWeather = view.findViewById(R.id.tViewWeather);
        tViewPressure = view.findViewById(R.id.tViewPressure);
        tViewHumidity = view.findViewById(R.id.tViewHumidity);
        textViewWind = view.findViewById(R.id.textViewWind);
        iViewIcons = view.findViewById(R.id.iViewIcons);
    }
}
