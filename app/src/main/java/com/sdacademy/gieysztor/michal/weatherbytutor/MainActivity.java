package com.sdacademy.gieysztor.michal.weatherbytutor;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    Button wroclawButton;
    Button tokioButton;
    Button losAngelesButton;

    TextView temperatureTextView;
    TextView pressureTextView;
    TextView lastUpdatedTextView;
    TextView weatherTextView;

    ImageView weatherIconImageView;

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            double temperature = intent.getDoubleExtra("TEMPERATURE", 0);
            String city = intent.getStringExtra("CITY");
            int pressure = intent.getIntExtra("PRESSURE", 0);
            String main = intent.getStringExtra("MAIN");
            long date = intent.getLongExtra("DATE", 0);

            temperatureTextView.setText(temperature + "");
            pressureTextView.setText(pressure + " hPa");
            weatherTextView.setText("" + main);
            lastUpdatedTextView.setText("" + convertDate(date));
        }
    };

    private String convertDate (long date){
        SimpleDateFormat format = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
        return format.format(new Date(date));
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        temperatureTextView = (TextView) findViewById(R.id.temperature);
        pressureTextView = (TextView) findViewById(R.id.pressure);
        lastUpdatedTextView = (TextView) findViewById(R.id.last_updated);
        weatherTextView = (TextView) findViewById(R.id.weather);


        wroclawButton = (Button) findViewById(R.id.button_wroclaw);
        tokioButton = (Button) findViewById(R.id.button_tokio);
        losAngelesButton = (Button) findViewById(R.id.button_losAngeles);


        wroclawButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, WeatherIntentService.class);
                intent.setAction("GET_WEATHER");
                intent.putExtra("CITY", "Wroclaw");

                startService(intent);
            }
        });

        tokioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, WeatherIntentService.class);
                intent.setAction("GET_WEATHER");
                intent.putExtra("CITY", "Tokio");

                startService(intent);
            }
        });

        losAngelesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, WeatherIntentService.class);
                intent.setAction("GET_WEATHER");
                intent.putExtra("CITY", "Los Angeles");

                startService(intent);
            }
        });

        IntentFilter filter = new IntentFilter();
        filter.addAction("WEATHER_RESPONSE");

        LocalBroadcastManager broadcastManager = LocalBroadcastManager.getInstance(this);
        broadcastManager.registerReceiver(broadcastReceiver, filter);
    }

    @Override
    protected void onDestroy() {
        LocalBroadcastManager broadcastManager = LocalBroadcastManager.getInstance(this);
        broadcastManager.unregisterReceiver(broadcastReceiver);
    }
}
