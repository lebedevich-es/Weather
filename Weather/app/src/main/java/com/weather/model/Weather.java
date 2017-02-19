package com.weather.model;

public class Weather {

    private long mDate;

    private int mTemp;

    private int mHumidity;

    private String mDescription;

    private String mIcon;

    private int mSpeed;

    private int mClouds;

    private double mRain;

    private double mSnow;

    public Weather(long pDate, int pTemp, int pHumidity, String pDescription, String pIcon, int pSpeed, int pClouds, double pRain, double pSnow) {
        mDate = pDate;
        mTemp = pTemp;
        mHumidity = pHumidity;
        mDescription = pDescription;
        mIcon = pIcon;
        mSpeed = pSpeed;
        mClouds = pClouds;
        mRain = pRain;
        mSnow = pSnow;
    }

    public long getDate() {
        return mDate;
    }

    public int getTemp() {
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

    public double getRain() {
        return mRain;
    }

    public double getSnow() {
        return mSnow;
    }
}
