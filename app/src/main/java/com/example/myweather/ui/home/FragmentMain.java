package com.example.myweather.ui.home;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myweather.R;
import com.example.myweather.Thermometer;
import com.example.myweather.model.OpenWeather;
import com.example.myweather.model.WeatherRequest;
import com.example.myweather.model.WeatherRequestForView;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentMain extends Fragment {

    private Thermometer thermometerView;
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
    private String city = "Москва";



    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Вынести в отдельный класс
        final SharedPreferences defaultPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
        setRetainInstance(true);
        lang = Locale.getDefault().getISO3Language().substring(0,2);
        readFromPreference(defaultPrefs);
        findView(view);
        setOniViewCurrent();
        SetOnSelectCity();
        getWeatherTread();
    }

    @Override
    public void onStart() {
        super.onStart();
        // Вынести в отдельный класс
        final SharedPreferences defaultPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
        lang = Locale.getDefault().getISO3Language().substring(0,2);
        readFromPreference(defaultPrefs);
        setOniViewCurrent();
        SetOnSelectCity();
        getWeatherTread();


    }

    private void getWeatherTread() {
        OpenWeather.getInstance().getAPI().loadWeather(city + ",ru",
                "metric", lang, "c7f79a8f3dde991ba5771bba492c90d7")
                .enqueue(new Callback<WeatherRequest>() {
                    @Override
                    public void onResponse(@NonNull Call<WeatherRequest> call,
                                           @NonNull Response<WeatherRequest> response) {
                        if (response.body() != null && response.isSuccessful()) {
                            final WeatherRequestForView weatherRequestForView = new WeatherRequestForView(response.body(), getActivity().getApplicationContext());
                            displayWeather(weatherRequestForView);
                        } else {
                            //Похоже, код у нас не в диапазоне [200..300) и случилась ошибка
                            //обрабатываем ее
                            if(response.code() == 500) {
                                //ой, случился Internal Server Error. Решаем проблему
                            } else if(response.code() == 401) {
                                //не авторизованы, что-то с этим делаем.
                                //например, открываем страницу с логинкой
                            }// и так далее
                        }
                    }

                    //сбой при интернет подключении
                    @Override
                    public void onFailure(Call<WeatherRequest> call, Throwable t) {
                        clickAlertDialogNoInternetAccess();
                    }
                });
    }

    private void clickAlertDialogNoInternetAccess() {
        AlertDialog.Builder builder = new AlertDialog.Builder(FragmentMain.this.getContext());
        builder.setTitle(R.string.title_alert_dialog)
                .setMessage(R.string.message_allert_dialog)
                .setIcon(R.drawable.wifioff)
                .setPositiveButton(R.string.button_ok,
                        new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }


    private void saveToPreference(SharedPreferences preferences) {
        SharedPreferences.Editor editor = preferences.edit();

//        String text = inputEditText.getText().toString();
 //       editor.putString(textKey, text);
        editor.apply();
    }

    private void readFromPreference(SharedPreferences preferences) {
        String textKeyCurrentPlace = "current_place";
        String textKeyDefaultCity = "edit_text_preference_1";
        boolean currentPlace = preferences.getBoolean(textKeyCurrentPlace, false);
        String textCity = preferences.getString(textKeyDefaultCity, "");
        if (currentPlace) {
            city = textCity;
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

    private void displayWeather(WeatherRequestForView weatherRequestForView){
        tViewTemperature.setText(weatherRequestForView.getTemperatureStr());
        thermometerView.setLevel((int) (50+weatherRequestForView.getTemperatureF()));
        tViewCiy.setText(weatherRequestForView.getCity());
        tViewWeather.setText(weatherRequestForView.getWeather());
        tViewPressure.setText(weatherRequestForView.getPressure());
        tViewHumidity.setText(weatherRequestForView.getHumidity());
        textViewWind.setText(weatherRequestForView.getWindSpeed());
        String icon = weatherRequestForView.getIcon();
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
        thermometerView = view.findViewById(R.id.thermometerView);

        Uri uri = Uri.parse("https://images.unsplash.com/photo-1553984840-ec965a23cddd?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=2089&q=80");
        SimpleDraweeView tViewLoad = (SimpleDraweeView) view.findViewById(R.id.tViewLoad);
        tViewLoad.setImageURI(uri);
    }
}
