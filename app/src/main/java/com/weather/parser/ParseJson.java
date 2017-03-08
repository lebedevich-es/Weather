package com.weather.parser;

import com.weather.model.City;
import com.weather.model.Forecast;
import com.weather.model.Weather;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ParseJson {

    public Forecast parseJson(final String responce) {

        City mCity;
        Weather weather;
        Forecast forecast = null;
        JSONObject dataJsonObject;

        try {
            dataJsonObject = new JSONObject(responce);

            JSONObject city = dataJsonObject.getJSONObject("city");

            mCity = new City(city.getString("name"), city.getString("country"));

            long mDate;
            double mTemp;
            int mHumidity;
            String mDescription;
            String mIcon;
            int mSpeed;
            int mClouds;

            ArrayList<Weather> mList = new ArrayList<>();

            JSONArray list = dataJsonObject.getJSONArray("list");

            for (int i = 0; i < list.length(); i++) {

                JSONObject day = list.getJSONObject(i);

                mDate = day.getLong("dt") * 1000;

                mTemp = (day.getJSONObject("temp").getDouble("min") + day.getJSONObject("temp").getDouble("max")) / 2.0;

                mHumidity = day.getInt("humidity");
                mSpeed = day.getInt("speed");
                mClouds = day.getInt("clouds");

                mDescription = (day.getJSONArray("weather")).getJSONObject(0).getString("description");
                mIcon = (day.getJSONArray("weather")).getJSONObject(0).getString("icon");

                weather = new Weather(mDate, mTemp, mHumidity, mDescription, mIcon, mSpeed, mClouds);
                mList.add(weather);

            }
            forecast = new Forecast(mCity, mList);

        } catch (final JSONException e) {
            e.printStackTrace();
        }
        return forecast;
    }

}
