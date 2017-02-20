package com.weather.model;

public class City {

    private String mName;

    private String mCountry;

    public City(String pName, String pCountry) {
        mName = pName;
        mCountry = pCountry;
    }

    public String getName() {
        return mName;
    }

    public String getCountry() {
        return mCountry;
    }
}
