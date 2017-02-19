package com.weather.model;

import java.util.ArrayList;

public class Forecast {

    private City mCity;

    private ArrayList<Weather> mList;

    public Forecast(City pCity, ArrayList<Weather> pList) {
        mCity = pCity;
        mList = pList;
    }

    public City getCity() {
        return mCity;
    }

    public Weather getList(int i) {
        return mList.get(i);
    }
}
