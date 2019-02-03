package com.example.stormy;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;

import com.example.stormy.adapters.HourAdapter;
import com.example.stormy.ui.MainActivity;
import com.example.stormy.weather.Hour;

import java.util.Arrays;

public class HourlyForecastActivity extends AppCompatActivity {
    private Hour[] mHours;
    RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hourly_forecast);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        Intent intent = getIntent();
        // to fill the data by paraceple  way in mhours array  and make the adapter to show it from mhours into recycle  view
        Parcelable[] parcelables = intent.getParcelableArrayExtra(MainActivity.HOURLY_FORECAST);

        mHours = Arrays.copyOf(parcelables, parcelables.length, Hour[].class);
        HourAdapter adapter = new HourAdapter(this, mHours);
        mRecyclerView.setAdapter(adapter);
        //essential component of recycleview
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);

    }

}
