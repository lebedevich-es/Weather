package com.weather;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.weather.model.Forecast;
import com.weather.utils.Contract;
import com.weather.utils.WeatherManager;

public class MainActivity extends AppCompatActivity implements Contract.View, SwipeRefreshLayout.OnRefreshListener {

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private WeatherManager weatherManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.refresh);
        mSwipeRefreshLayout.setOnRefreshListener(this);

        weatherManager = new WeatherManager(this, this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.title_update:
                ;
            case R.id.title_about_program:
                ;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void showData(Forecast forecast) {

    }

    @Override
    public void showError(String message) {
        new AlertDialog.Builder(this).setMessage(message).create().show();
    }

    @Override
    public void showProgress(boolean isInProgress) {

        mSwipeRefreshLayout.setRefreshing(isInProgress);
    }

    @Override
    public void onRefresh() {

        LocatrListener.SetUpLocationListener(this);
        weatherManager.getWeather(LocatrListener.imHere.getLatitude(), LocatrListener.imHere.getLongitude());
    }

}

