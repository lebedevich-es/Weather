package com.weather.model;

public class Weather {

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

}
