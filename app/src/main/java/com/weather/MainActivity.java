package com.weather;

import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.weather.constants.Constants;
import com.weather.model.City;
import com.weather.model.Forecast;
import com.weather.utils.Contract;
import com.weather.utils.WeatherManager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements Contract.View {

    private WeatherManager weatherManager;
    private SharedPreferences sPref;
    private TextView mCityName;
    private TextView mCityCountry;
    private TextView mForecast;
    private ImageView mImage;
    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mCityName = (TextView) findViewById(R.id.tvCityName);
        mCityCountry = (TextView) findViewById(R.id.tvCityCountry);
        mForecast = (TextView) findViewById(R.id.tvForecast);
        mImage = (ImageView) findViewById(R.id.ivImage);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);

        ImageButton mImageButton = (ImageButton) findViewById(R.id.ibMenu);
        mImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupMenu(v);
            }
        });

        ImageButton mIbRefresh = (ImageButton) findViewById(R.id.ibRefresh);
        mIbRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRefresh();
            }
        });

        weatherManager = new WeatherManager(this, this);

        sPref = getSharedPreferences(Constants.PREF, MODE_PRIVATE);

        String flag = sPref.getString(Constants.FLAG, "");
        String name = sPref.getString(Constants.CITY_NAME, "");
        String country = sPref.getString(Constants.CITY_COUNTRY, "");

        if (flag.equals("")) {
//            onRefresh();
        }
        if (!flag.equals("")) {
            City mCity = new City(name, country);
            weatherManager.getWeatherFromDb(mCity);
        }
    }

    private void showPopupMenu(View v) {

        PopupMenu popupMenu = new PopupMenu(this, v);
        popupMenu.inflate(R.menu.menu_main);

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.titleAboutProgram:
                        Toast.makeText(getApplicationContext(), "The application about weather.", Toast.LENGTH_SHORT).show();
                        return true;
                    default:
                        return false;
                }
            }
        });
        popupMenu.show();
    }

    @Override
    public void showData(Forecast forecast) {

        if (forecast != null) {

            String ATTRIBUTE_DATE = "date";
            String ATTRIBUTE_TEMP = "temp";
            String ATTRIBUTE_ICON = "icon";
            String ATTRIBUTE_DESCRIPTION = "description";
            String ATTRIBUTE_HUMIDITY = "humidity";
            String ATTRIBUTE_SPEED = "speed";
            String ATTRIBUTE_CLOUDS = "clouds";

            sPref.edit().putString(Constants.CITY_NAME, forecast.getCity().getName()).apply();
            sPref.edit().putString(Constants.CITY_COUNTRY, forecast.getCity().getCountry()).apply();
            sPref.edit().putString(Constants.FLAG, "flag").apply();

            Map<String, Object> m;

            ListView lvSimple;

            SimpleDateFormat dateFormat = new SimpleDateFormat("E \n dd.MM ", Locale.US);

            ArrayList<Map<String, Object>> data = new ArrayList<>();

            for (int i = 0; i < forecast.size(); i++) {

                m = new HashMap<>();

                m.put(ATTRIBUTE_DATE, dateFormat.format(forecast.get(i).getDate()));
                m.put(ATTRIBUTE_TEMP, (int) (forecast.get(i).getTemp() - Constants.TEMP) + "°C");
                m.put(ATTRIBUTE_ICON, forecast.get(i).getIcon());
                m.put(ATTRIBUTE_DESCRIPTION, forecast.get(i).getDescription());
                m.put(ATTRIBUTE_HUMIDITY, forecast.get(i).getHumidity() + "%");
                m.put(ATTRIBUTE_SPEED, forecast.get(i).getSpeed() + "mph");
                m.put(ATTRIBUTE_CLOUDS, forecast.get(i).getClouds() + "%");

                data.add(m);
            }

            String[] from = {ATTRIBUTE_DATE, ATTRIBUTE_TEMP, ATTRIBUTE_ICON, ATTRIBUTE_DESCRIPTION, ATTRIBUTE_HUMIDITY, ATTRIBUTE_SPEED, ATTRIBUTE_CLOUDS};
            int[] to = {R.id.tvDate, R.id.tvTemp, R.id.ivSmall, R.id.tvDescription, R.id.tvHumidity, R.id.tvSpeed, R.id.tvClouds};

            SimpleAdapter sAdapter = new SimpleAdapter(this, data, R.layout.item, from, to);
            sAdapter.setViewBinder(new ViewBinder());
            lvSimple = (ListView) findViewById(R.id.list);
            lvSimple.setAdapter(sAdapter);

            mCityName.setText(forecast.getCity().getName());
            mCityCountry.setText(forecast.getCity().getCountry());
            mForecast.setText(forecast.get(0).getDescription() + " " + (int) (forecast.get(0).getTemp() - Constants.TEMP) + "°C");
            mImage.setImageResource(getResources().getIdentifier(Constants.IMG + forecast.get(0).getIcon(), "mipmap", getApplicationContext().getPackageName()));
        }
    }

    private class ViewBinder implements SimpleAdapter.ViewBinder {

        @Override
        public boolean setViewValue(final View view, final Object data, final String textRepresentation) {

            if (view.getId() == R.id.ivSmall) {
                ImageView iv = (ImageView) view;
                iv.setImageResource(getResources().getIdentifier(Constants.IMG + data, "mipmap", getApplicationContext().getPackageName()));
                return true;
            }
            return false;
        }
    }

    @Override
    public void showError(String message) {

        new AlertDialog.Builder(this).setMessage(message).create().show();
    }

    @Override
    public void showProgress(boolean isInProgress) {

        if (isInProgress) {
            mProgressBar.setVisibility(ProgressBar.VISIBLE);
        } else {
            mProgressBar.setVisibility(ProgressBar.INVISIBLE);
        }
    }

    @Override
    public void onRefresh() {

        LocatrListener.SetUpLocationListener(this);
        weatherManager.getWeather(LocatrListener.imHere.getLatitude(), LocatrListener.imHere.getLongitude());
    }

}

