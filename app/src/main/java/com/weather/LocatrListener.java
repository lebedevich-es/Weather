package com.weather;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;

public class LocatrListener implements LocationListener {

    private static final int MIN_TIME = 5000;
    public static Location imHere;

    public static void SetUpLocationListener(final Context context) {

        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {

            LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
            LocationListener locationListener = new LocatrListener();

            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME, 10, locationListener);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME, 10, locationListener);

            if (locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER) != null) {
                imHere = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            } else if (locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER) != null) {
                imHere = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            }

        }
    }

    @Override
    public void onLocationChanged(final Location location) {
        imHere = location;
    }

    @Override
    public void onProviderDisabled(final String provider) {
    }

    @Override
    public void onProviderEnabled(final String provider) {
    }

    @Override
    public void onStatusChanged(final String provider, final int status, final Bundle extras) {
    }

}