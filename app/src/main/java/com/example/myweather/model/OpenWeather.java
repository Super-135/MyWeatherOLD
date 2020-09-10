package com.example.myweather.model;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class OpenWeather {
    private static OpenWeather instance = null;
    private IOpenWeather API;

    private OpenWeather() {
        API = createAdapter();
    }

    public static OpenWeather getInstance() {
        if(instance == null) {
            instance = new OpenWeather();
        }

        return instance;
    }

    public IOpenWeather getAPI() {
        return API;
    }

    private IOpenWeather createAdapter() {
        Retrofit adapter = new Retrofit.Builder()
                .baseUrl("https://api.openweathermap.org/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return adapter.create(IOpenWeather.class);
    }
}
