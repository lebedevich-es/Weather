package com.weather.utils;

import com.weather.model.Forecast;

public interface Contract {

    interface View {

        void showData(Forecast forecast);

        void showError(String message);

        void showProgress(boolean isInProgress);
    }

    interface Presenter {

        void getWeather(double lat, double lon);

        void getWeatherFromDb();
    }
}
