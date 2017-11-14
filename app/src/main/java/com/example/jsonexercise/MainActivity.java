package com.example.jsonexercise;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.jsonexercise.gson.HeWeather;
import com.google.gson.Gson;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private TextView textViewCity;

    private TextView textViewTemperature;

    private TextView textViewWeatherInfo;

    HeWeather heWeather;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textViewCity = (TextView) findViewById(R.id.text_view_city);
        textViewTemperature = (TextView) findViewById(R.id.text_view_temperature);
        textViewWeatherInfo = (TextView) findViewById(R.id.text_view_weather_info);
        Button buttonLoad = (Button) findViewById(R.id.button_load);
        buttonLoad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendRequestWithOkHttp();
            }
        });
    }

    private void sendRequestWithOkHttp() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            .url("http://guolin.tech/api/weather?cityid=CN101200101&key=4146ca62e6d84dfb81cf8b33b536f630")
                            .build();
                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();
                    parseJSONWithGSON(responseData);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void parseJSONWithGSON(String jsonData) {
        Gson gson = new Gson();
        heWeather = gson.fromJson(jsonData, HeWeather.class);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                HeWeather.HeWeatherBean heWeatherBean = heWeather.getHeWeather().get(0);
                String cityName = heWeatherBean.getBasic().getCity();
                textViewCity.setText("City:" + cityName);
                String degree = heWeatherBean.getNow().getTmp();
                textViewTemperature.setText("Temperature:" + degree);
                String weatherInfo = heWeatherBean.getNow().getCond().getTxt();
                textViewWeatherInfo.setText("WeatherInfo:" + weatherInfo);
            }
        });
    }
}
