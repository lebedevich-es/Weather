package com.weather.utils;

import com.weather.model.City;
import com.weather.model.Forecast;

public interface Contract {

    interface View {

        void showData(Forecast forecast);

        void showError(String message);

        void showProgress(boolean isInProgress);

        void onRefresh();
    }

    interface Presenter {

        void getWeather(double lat, double lon);

        void getWeatherFromDb(City city);
    }
}
