package com.sdacademy.gieysztor.michal.weatherbytutor;

import android.app.IntentService;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import org.json.JSONObject;

import okhttp3.Request;

/**
 * Created by RENT on 2017-02-28.
 */

public class WeatherIntentService extends IntentService {
    public WeatherIntentService() {
        super("WeatherIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            String action = intent.getAction();
            if ("GET_WEATHER".equals(action)) {
                String city = intent.getStringExtra("CITY");
                getWeather(city);
            }
//            Log.i("weatherService", "Nazwa akcji: " + action);
        }

    }

    private void getWeather(String city) {
        Log.i("TEST", "getWeather - " + city);

        JSONObject jsonObject = sendRequest(city);

        Intent intent = new Intent();
        intent.setAction("WEATHER_RESPONSE");
        intent.putExtra("TEMPERATURE", 26.1);
        intent.putExtra("PRESSURE", 1018);
        intent.putExtra("MAIN", "cloudy");
        intent.putExtra("DATE", System.currentTimeMillis());
        intent.putExtra("CITY", city);

        LocalBroadcastManager broadcastManager = LocalBroadcastManager.getInstance(this);
        broadcastManager.sendBroadcast(intent);
    }

    private JSONObject sendRequest (String city){
        Request.Builder builder = new Request.Builder();
        String base_url = "http://api.openweathermap.org/data/2.5/weather?";
        String city_url = "q=" + city;
        String lang_url = "lang=pl";
        String units_url = "units=metric";
        String appId_url = "appid=8ba00e1208dbb514da4b9df230d82bf1";
        String url = base_url
                + city_url + "&"
                + lang_url + "&"
                + units_url + "&"
                + appId_url + "&";

        builder.url(url);
        builder.get();

        Request request = builder.build();


    }
}
