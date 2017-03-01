package com.sdacademy.gieysztor.michal.weatherbytutor;

import android.app.IntentService;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

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
        Intent intent = new Intent();
        intent.setAction("WEATHER_RESPONSE");
        Log.i("TEST", "getWeather - " + city);

        JSONObject jsonObject = sendRequest(city);

        JSONObject mainJsonObject = jsonObject.optJSONObject("main");

        if (mainJsonObject != null) {
            intent.putExtra("TEMPERATURE", mainJsonObject.optDouble("temp"));
            intent.putExtra("PRESSURE", mainJsonObject.optInt("pressure"));
        }

        JSONArray weatherJsonArray = jsonObject.optJSONArray("weather");
        if (weatherJsonArray != null && weatherJsonArray.length() > 0) {
            JSONObject weatherElement = weatherJsonArray.optJSONObject(0);

            if (weatherElement != null) {
                intent.putExtra("MAIN", weatherElement.optString("main"));
            }
        }

        intent.putExtra("DATE", jsonObject.optLong("dt") * 1000L);
        intent.putExtra("CITY", jsonObject.optString("name"));
//        intent.putExtra("TEMPERATURE", 26.1);
//        intent.putExtra("PRESSURE", 1018);
//        intent.putExtra("MAIN", "cloudy");
//        intent.putExtra("DATE", System.currentTimeMillis());
//        intent.putExtra("CITY", city);

        LocalBroadcastManager broadcastManager = LocalBroadcastManager.getInstance(this);
        broadcastManager.sendBroadcast(intent);
    }

    private JSONObject sendRequest(String city) {
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

        OkHttpClient client = new OkHttpClient();
        try {
            Response response = client.newCall(request).execute();
            String stringResponse = response.body().string();
            JSONObject jsonObject = new JSONObject(stringResponse);
            return jsonObject;
        } catch (IOException e) {
            e.printStackTrace();
            return new JSONObject();
        } catch (JSONException e) {
            e.printStackTrace();
            return new JSONObject();
        }


    }
}
