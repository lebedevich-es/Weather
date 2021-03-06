package com.weather;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;

public class LocatrListener implements LocationListener {

    private static final int MIN_TIME = 5000;
    public static Location imHere;

    public static void SetUpLocationListener(final Context context) {
        final LocationManager locationManager = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);

        final LocationListener locationListener = new LocatrListener();
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            System.out.println("tttt");
            return;
        }
        System.out.println("uuuuuuuuu");
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);

        imHere = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        System.out.println("im = "+imHere);
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