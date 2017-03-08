package com.weather.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.weather.HttpClient;
import com.weather.R;
import com.weather.model.City;
import com.weather.model.Forecast;
import com.weather.model.Weather;
import com.weather.parser.ParseJson;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.TimeZone;

import io.realm.Realm;
import io.realm.RealmResults;

public class WeatherManager implements Contract.Presenter {

    private static final String ACCESS_KEY = "57eac87bbf864b3de29a4c2274497ced";
    private static final String BASE_URL = "http://api.openweathermap.org/data/2.5/forecast/daily?";

    private Forecast forecast;
    private Contract.View mView;
    private Handler mHandler;
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

                ParseJson parseJson = new ParseJson();

                Forecast forecast = parseJson.parseJson(response);

                notifyResponse(forecast);

                setDataDb(forecast);

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
    public void getWeatherFromDb(City city) {

        final ArrayList<Weather> data = getDataDb();
        forecast = new Forecast(city, data);

        new Thread() {

            @Override
            public void run() {
                notifyResponse(forecast);
            }
        }.start();
    }

    private void setDataDb(final Forecast forecast) {

        ArrayList<Weather> list = new ArrayList<>();

        Weather weathers;

        for (final Weather weather : forecast.getList()) {

            weathers = new Weather();

            weathers.setDate(weather.getDate());
            weathers.setTemp(weather.getTemp());
            weathers.setHumidity(weather.getHumidity());
            weathers.setDescription(weather.getDescription());
            weathers.setIcon(weather.getIcon());
            weathers.setSpeed(weather.getSpeed());
            weathers.setClouds(weather.getClouds());

            list.add(weathers);
        }

        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        realm.delete(Weather.class);
        realm.commitTransaction();
        realm.close();

        for (Weather w : list) {
            realm = Realm.getDefaultInstance();
            realm.beginTransaction();
            realm.copyToRealm(w);
            realm.commitTransaction();
            realm.close();
        }
    }

    @Nullable
    private ArrayList<Weather> getDataDb() {

        Realm realm = Realm.getDefaultInstance();
        RealmResults<Weather> weathers = realm.where(Weather.class).findAll();
        ArrayList<Weather> list = new ArrayList<>(weathers);

        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        long date = calendar.getTimeInMillis();
        Integer hour = calendar.get(Calendar.HOUR_OF_DAY);
        Integer minute = calendar.get(Calendar.MINUTE);
        Integer second = calendar.get(Calendar.SECOND);
        Integer time = ((hour * 60 + minute) * 60 + second) * 1000;

        for (int i = 0; i < list.size(); ) {
            long dateDb = list.get(0).getDate();

            if (dateDb < (date - time)) {
                realm = Realm.getDefaultInstance();
                realm.beginTransaction();
                list.remove(0);
                realm.commitTransaction();
                realm.close();
                i = 0;
            } else break;
        }
        return list;

    }

}