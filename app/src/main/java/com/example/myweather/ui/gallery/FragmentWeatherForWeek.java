package com.example.myweather.ui.gallery;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.example.myweather.IRVOnItemClick;
import com.example.myweather.R;
import com.example.myweather.RecyclerDateAdapter;
import com.example.myweather.model.Daily;
import com.example.myweather.model.ForecastWeek;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

import javax.net.ssl.HttpsURLConnection;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class FragmentWeatherForWeek extends Fragment implements IRVOnItemClick {

    private RecyclerView recyclerView;
    private ArrayList<Daily> listDataWeek;
    private static final String TAG = "WEATHER";
    private static final String WEATHER_URL_WEEK = "https://api.openweathermap.org/data/2.5/onecall?lat=55.7522&lon=37.6156&%20exclude=daily&units=metric&appid=";

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_weather_for_week, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setRetainInstance(true);
        getWeatherWeek();
        findView(view);
        setupRecyclerView();
    }

    private void getWeatherWeek() {
        try {
            final URL uri = new URL(WEATHER_URL_WEEK + "c7f79a8f3dde991ba5771bba492c90d7");
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
                        final ForecastWeek forecastWeek = gson.fromJson(result, ForecastWeek.class);
                        listDataWeek = new ArrayList<>(Arrays.asList(forecastWeek.getDaily()));
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                setupRecyclerView();
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

    @Override
    public void onItemClicked(Daily itemText) {
        // Пока заглушка. Потом открываем активити которое частично перекрывает предыдущее активити
        // и показывает подробную информацию по этому дню.
    }

    private void findView(View view) {
        recyclerView = view.findViewById(R.id.recyclerView);
    }

    private void setupRecyclerView(){
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        RecyclerDateAdapter adapter = new RecyclerDateAdapter(listDataWeek, this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }


}
