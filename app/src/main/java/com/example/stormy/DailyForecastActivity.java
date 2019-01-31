package com.example.stormy;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.widget.ArrayAdapter;

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
        Parcelable[] parcelables  =intent.getParcelableArrayExtra(MainActivity.DAILY_FORECAST);
        mDays= Arrays.copyOf(parcelables,parcelables.length,Day[].class);
        DayAdapter adapter=new DayAdapter(this, mDays);
        setListAdapter(adapter);
    }




}
