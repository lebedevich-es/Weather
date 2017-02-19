package com.weather.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;

import com.weather.HttpClient;
import com.weather.R;
import com.weather.model.Forecast;

import java.io.IOException;

public class WeatherManager implements Contract.Presenter {

    private static final String ACCESS_KEY = "57eac87bbf864b3de29a4c2274497ced";
    private static final String BASE_URL = "http://api.openweathermap.org/data/2.5/forecast/daily?";

    private Contract.View mView;
    private Handler mHandler;
//    private Forecast mForecast;
    private Context mContext;

    public WeatherManager(@NonNull final Contract.View view, Context pContext) {
        mView = view;
        mHandler = new Handler(Looper.getMainLooper());
        mContext = pContext;

    }

    @Override
    public void getWeather(double lat, double lon) {

        mView.showProgress(true);

        if (isNetworkAvailable()) {
            loadData(String.valueOf(lat), String.valueOf(lon));
        } else {
            notifyError(mContext.getString(R.string.no_internet_connection));

        }
    }

    private void loadData(final String lat, final String lon) {

        new Thread() {

            @Override
            public void run() {

                String response = (new HttpClient()).get(BASE_URL + "lat=" + lat + "&lon=" + lon + "&cnt=10" + "&APPID=" + ACCESS_KEY);

                System.out.println(response);
//                    ParseJson parseJsonForecast = new ParseJson();

//                    Forecast forecast = parseJsonForecast.parseJson(response);

                Forecast forecast=null;
                notifyResponse(forecast);

            }
        }.start();
    }

    private void notifyResponse(final Forecast forecast) {
        mHandler.post(new Runnable() {

            @Override
            public void run() {
                mView.showProgress(false);
                mView.showData(forecast);
            }
        });
    }

    private void notifyError(final String message) {
        mHandler.post(new Runnable() {

            @Override
            public void run() {
                mView.showProgress(false);
                mView.showError(message);
            }
        });
    }

    private boolean isNetworkAvailable() {
        final ConnectivityManager connectivityManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        final NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }

    @Override
    public void getWeatherFromDb() {

    }
}
