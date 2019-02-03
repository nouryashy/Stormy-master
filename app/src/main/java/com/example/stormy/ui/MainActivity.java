package com.example.stormy.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.stormy.DailyForecastActivity;
import com.example.stormy.HourlyForecastActivity;
import com.example.stormy.R;
import com.example.stormy.weather.Current;
import com.example.stormy.weather.Day;
import com.example.stormy.weather.Forecast;
import com.example.stormy.weather.Hour;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;


import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;




public class MainActivity extends AppCompatActivity {
    public static final String TAG = MainActivity.class.getSimpleName();
    public static final String DAILY_FORECAST="DAIYLY_FORECAST";
    private Forecast mForecast;

      TextView mTimeLabel;
      TextView mTempratureLabel;
      TextView mHumidityValue;
      TextView mPrecipValue;
      TextView mSummaryLabel;
      ImageView mIconImageView;
      ImageView mRefreshImageView;
      ProgressBar mProgressBar;
      Button mHourlyButton;
      Button mDailyButton;





    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
         mTimeLabel=(TextView)findViewById(R.id.timeLabel);
         mTempratureLabel=(TextView)findViewById(R.id.temperature_label);
         mHumidityValue=(TextView)findViewById(R.id.humidityValue);
         mPrecipValue=(TextView)findViewById(R.id.precipValue);
         mSummaryLabel=(TextView)findViewById(R.id.summaryLabel);
         mIconImageView=(ImageView) findViewById(R.id.iconImageView);
         mRefreshImageView=(ImageView) findViewById(R.id.refreshImageView);
         mProgressBar=(ProgressBar)findViewById(R.id.progressBar);
         mHourlyButton =(Button)findViewById(R.id.hourlyButton);
        mDailyButton =(Button)findViewById(R.id.dailyButton);

        mProgressBar.setVisibility(View.INVISIBLE);

        final double latitude = 30.5917389;
        final double longitude = 31.5014131;
        mRefreshImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getForecast(latitude,longitude);

            }
        });

        getForecast(latitude,longitude);

        Log.d(TAG,"Main UI  code is running ! ");


    }
//refactor the code in this method to organize the code
    private void getForecast(double latitude,double longitude ) {
        String apiKey = "f9abce82044723eb1285ab0e0466063c";

        String units = "?units=ca";
        String forecaseURL = "https://api.darksky.net/forecast/" + apiKey +
                "/" + latitude + "," + longitude + units;
        //to check network connectivity oly this single line and the next line is to okhttp connection
        if (isNoNetworkAvaliable()){
            toggleRefresh();
            //making okhttp connection
        OkHttpClient client = new OkHttpClient();
        // building request which will be send to the server
        Request request = new Request.Builder()
                .url(forecaseURL).build();
        //calling object "request"
        Call call = client.newCall(request);
        //when add this line the failure and on response will be generated and will add try and catch in the responce method
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        toggleRefresh();
                    }
                });
                alertUserAboutError();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
               runOnUiThread(new Runnable() {
                   @Override
                   public void run() {
                       toggleRefresh();
                   }
               });
                try {
                    //declaring response as available
                    // will be error and will use try and catch to solve it
                  String jsonData=response.body().string();
                    Log.v(TAG, jsonData);
                    if (response.isSuccessful()) {
                        mForecast=paseForecastDetails(jsonData);
                        //to run the updates on the thread
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                updateDisplay();
                            }
                        });

                    }else{
                       alertUserAboutError();
                    }
                }
                catch (IOException e) {
                    // after typing this next line will declare tag in main class
                    Log.e(TAG, "Exceotion caught:", e);
                }catch (JSONException e){
                    Log.e(TAG, "Exceotion caught:", e);
                }


            }
        }); }
        //the else of if (isNoNetworkAvaliable())
        else {
            Toast.makeText(this,getString(R.string.network_unavalabile_message),
                    Toast.LENGTH_LONG).show();
        }
    }

    private void toggleRefresh() {
        if(mProgressBar.getVisibility()==View.INVISIBLE){


           mProgressBar.setVisibility(View.VISIBLE );
           mRefreshImageView.setVisibility(View.INVISIBLE);
          }else{
            mProgressBar.setVisibility(View.INVISIBLE );
            mRefreshImageView.setVisibility(View.VISIBLE);
        }
    }

    private void updateDisplay() {
        Current current=mForecast.getCurrent();
        mTempratureLabel.setText(current.getmTemperatre()+"");
        mTimeLabel.setText("At"+" "+ current.getFormattedTime()+" "+  "it will be " );
        mHumidityValue.setText(current.getmHummidity() +"");
        mPrecipValue.setText(current.getmPrecipChance()+"%");
       mSummaryLabel .setText(current.getmSummary());
        Drawable drawable=getResources().getDrawable(current.getmIcon());
       mIconImageView.setImageDrawable(drawable);
    }
    //class to parse hourly and daily data

private Forecast paseForecastDetails (String jsonData)throws JSONException {
        Forecast forecast=new Forecast();
        forecast.setCurrent(getCurrentDetails(jsonData));
        forecast.setHourlyForecast(getHourlyForecast(jsonData));
        forecast.setDailyForecast(getDailyForecast(jsonData));
        return forecast;

}
    //methode to add Daily
    private Day[] getDailyForecast(String jsonData)throws JSONException {
        JSONObject forecast=new JSONObject(jsonData);
        String timeZone =forecast.getString("timezone");
        //decleare json list named daily
        JSONObject daily=forecast.getJSONObject("daily");
        //creating the json array
        JSONArray data= daily.getJSONArray("data");
        //creating the java array to get data from json array
        Day[] days=new  Day[data.length()];
        //forloop to loop every item in the array named data in json
        for (int i=0;i<data.length();i++){
            //decleare json list named Day
            JSONObject jsonDay =data.getJSONObject(i);
            //decleare every object in the array and set the data to it
            Day day=new Day();
            day.setSummary(jsonDay.getString("summary"));
            day.setTemperatureMax(jsonDay.getDouble("temperatureMax"));
            day.setIcon(jsonDay.getString("icon"));
            day.setTime(jsonDay.getLong("time"));
            day.setTimezone(timeZone);
            // to store hour in the array
            days[i]=day;


        }
        return days;

    }
    //methode to add hourly
    private Hour[] getHourlyForecast(String jsonData) throws JSONException{
        JSONObject forecast=new JSONObject(jsonData);
        String timeZone =forecast.getString("timezone");
        //decleare json list named hourly
        JSONObject hourly=forecast.getJSONObject("hourly");
        //creating the json array
        JSONArray data= hourly.getJSONArray("data");
        //creating the java array to get data from json array
        Hour[] hours=new Hour[data.length()];
        //forloop to loop every item in the array named data in json
        for(int i =0 ; i<data.length();i++){
            //decleare json list named Hour
            JSONObject jsonHour =data.getJSONObject(i);
            //decleare every object in the array and set the data to it
            Hour hour=new Hour();
            hour.setSummary(jsonHour.getString("summary"));
           hour.setTemperature(jsonHour.getDouble("temperature"));
            hour.setIcon(jsonHour.getString("icon"));
            hour.setTime(jsonHour.getLong("time"));
            hour.setTimezone(timeZone);
            // to store hour in the array
            hours[i]=hour;

        }
       return hours;
    }

    // creating it after writing   mCurrent=getCurrentDetails(jsonData); in if statment in onresponse
    private Current getCurrentDetails(String jsonData) throws JSONException {

        JSONObject forecast=new JSONObject(jsonData);
        String timeZone =forecast.getString("timezone");
        Log.i(TAG,"From JSON"+timeZone);

    //decleare json list named currently
        JSONObject currently=forecast.getJSONObject("currently");

//decleare every object in the list and set the data to it
        Current current =new Current();
        current.setmHummidity(currently.getDouble("humidity"));
        current.setmTime(currently.getLong("time"));
        current.setmIcon(currently.getString("icon"));
        current.setmPrecipChance(currently.getDouble("precipProbability"));
        current.setmSummary(currently.getString("summary"));
        current.setmTemperatre(currently.getDouble("temperature"));
        current.setmTimeZone(timeZone);
        Log.d(TAG, current.getFormattedTime());


        return current;

    }

    // To check network connectavity (generated method from "if (isNoNetworkAvaliable())")
    private boolean isNoNetworkAvaliable() {
        ConnectivityManager manager= (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo =manager.getActiveNetworkInfo();
        boolean isAvalabile=false;
        if (networkInfo!=null&&networkInfo.isConnected()){
            isAvalabile=true;
        }
        return isAvalabile;

    }
// to add alert fragment
    private void alertUserAboutError() {
        AlertDialogFragment dialog = new AlertDialogFragment();
        dialog.show(getSupportFragmentManager(),"error_dialog");
    }


    public void StartHourActivity(View view) {
        Intent intent=new Intent(this, HourlyForecastActivity.class);
       // intent.putExtra(HOURLY_FORECAST,)
    }

    public void StartDayActivity(View view) {
        Intent intent=new Intent(this, DailyForecastActivity.class);
        intent.putExtra(DAILY_FORECAST,mForecast.getDailyForecast());
        startActivity(intent);
    }
}
