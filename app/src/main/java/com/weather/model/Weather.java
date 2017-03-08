package com.weather.model;

import io.realm.RealmObject;

public class Weather extends RealmObject {

    private long mDate;

    private double mTemp;

    private int mHumidity;

    private String mDescription;

    private String mIcon;

    private int mSpeed;

    private int mClouds;

    public Weather(long pDate, double pTemp, int pHumidity, String pDescription, String pIcon, int pSpeed, int pClouds) {

        mDate = pDate;
        mTemp = pTemp;
        mHumidity = pHumidity;
        mDescription = pDescription;
        mIcon = pIcon;
        mSpeed = pSpeed;
        mClouds = pClouds;
    }

    public Weather() {
        super();
    }

    public long getDate() {
        return mDate;
    }

    public double getTemp() {
        return mTemp;
    }

    public int getHumidity() {
        return mHumidity;
    }

    public String getDescription() {
        return mDescription;
    }

    public String getIcon() {
        return mIcon;
    }

    public int getSpeed() {
        return mSpeed;
    }

    public int getClouds() {
        return mClouds;
    }


    public void setDate(long date) {
        mDate = date;
    }

    public void setTemp(double temp) {
        mTemp = temp;
    }

    public void setHumidity(int humidity) {
        mHumidity = humidity;
    }

    public void setDescription(String description) {
        mDescription = description;
    }

    public void setIcon(String icon) {
        mIcon = icon;
    }

    public void setSpeed(int speed) {
        mSpeed = speed;
    }

    public void setClouds(int clouds) {
        mClouds = clouds;
    }

}
