package com.example.stormy.ui;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.stormy.R;
import com.example.stormy.adapters.DayAdapter;
import com.example.stormy.ui.MainActivity;
import com.example.stormy.weather.Day;

import java.util.Arrays;
//change to extends lisActivity to ignor the declration of list view in main activity

public class DailyForecastActivity extends ListActivity {
    private Day[] mDays;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_forecast);
        Intent intent= getIntent();
        //Parcelable is java interface that make data easy to transport from one way to another
        Parcelable[] parcelables  =intent.getParcelableArrayExtra(MainActivity.DAIYLY_FORECAST);
        mDays= Arrays.copyOf(parcelables,parcelables.length,Day[].class);
        DayAdapter adapter=new DayAdapter(this, mDays);
        setListAdapter(adapter);


    }

    // to show atoast message when pressed the daily info

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
     String dayOfTheWeek=mDays[position].getDayOfTheWeek();
     String conditions=mDays[position].getSummary();
     String highTemp= mDays[position].getTemperatureMax()+"";

        String message =String.format("On%s the high will be %s and it will be %s",dayOfTheWeek
                ,highTemp
                ,conditions);
        Toast.makeText(this,message,Toast.LENGTH_LONG).show();
    }

}
