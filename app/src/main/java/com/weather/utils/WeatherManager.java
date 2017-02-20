package com.weather.utils;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.weather.HttpClient;
import com.weather.R;
import com.weather.costants.Constants;
import com.weather.db.DbHelper;
import com.weather.db.IDbOperations;
import com.weather.db.WeatherTable;
import com.weather.model.Forecast;
import com.weather.model.Weather;
import com.weather.parser.ParseJson;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class WeatherManager implements Contract.Presenter {

    private static final String ACCESS_KEY = "57eac87bbf864b3de29a4c2274497ced";
    private static final String BASE_URL = "http://api.openweathermap.org/data/2.5/forecast/daily?";

    private Contract.View mView;
    private Handler mHandler;
    private Context mContext;
    private IDbOperations operations;

    public WeatherManager(@NonNull final Contract.View view, Context pContext) {
        mView = view;
        mHandler = new Handler(Looper.getMainLooper());
        mContext = pContext;
        operations = new DbHelper(pContext);
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
                ParseJson parseJson = new ParseJson();

                Forecast forecast = parseJson.parseJson(response);

                notifyResponse(forecast);

                operations.delete(WeatherTable.WeatherTable, null, null);

                setDateDb(forecast);

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

        new Thread() {

            @Override
            public void run() {

//                notifyResponse(getDataDb());

            }
        }.start();

    }


    private void setDateDb(final Forecast forecast) {

        final List<ContentValues> listContantValues = new ArrayList<>();

        ContentValues values;

        for (final Weather weather : forecast.getList()) {

                values = new ContentValues();


                values.put(WeatherTable.DATE, weather.getDate());
                values.put(WeatherTable.TEMP, weather.getTemp());
                values.put(WeatherTable.HUMIDITY, weather.getHumidity());
                values.put(WeatherTable.DESCRIPTION, weather.getDescription());
                values.put(WeatherTable.ICON, weather.getIcon());
                values.put(WeatherTable.CLOUDS, weather.getClouds());
                values.put(WeatherTable.SPEED, weather.getSpeed());

                listContantValues.add(values);
            }

        operations.bulkInsert(WeatherTable.WeatherTable, listContantValues);
    }

    @Nullable
    public ArrayList<Weather> getDataDb() {

        String sql = WeatherTable.DATE + "<?";

        operations.delete(WeatherTable.WeatherTable, sql, Long.toString((new Date()).getTime()));

        sql = "SELECT * FROM " + WeatherTable.WeatherTable;

        Cursor cursor = operations.query(sql, null);

        ArrayList<Weather> list=null;

        if (cursor.moveToFirst()) {

            list= new ArrayList<>();
            do {

                Weather weather = new Weather(cursor.getLong(cursor.getColumnIndex(WeatherTable.DATE)),
                        cursor.getDouble(cursor.getColumnIndex(WeatherTable.TEMP)),
                        cursor.getInt(cursor.getColumnIndex(WeatherTable.HUMIDITY)),
                        cursor.getString(cursor.getColumnIndex(WeatherTable.DESCRIPTION)),
                        cursor.getString(cursor.getColumnIndex(WeatherTable.ICON)),
                        cursor.getInt(cursor.getColumnIndex(WeatherTable.SPEED)),
                        cursor.getInt(cursor.getColumnIndex(WeatherTable.CLOUDS)));

                list.add(weather);

            } while (cursor.moveToNext());

            return list;
        }

        cursor.close();
        return null;
    }

}
