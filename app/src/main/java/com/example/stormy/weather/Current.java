package com.example.stormy.weather;

import com.example.stormy.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class Current {
    private String mIcon;
    private Long mTime;
    private double mTemperature;
    private double mHumidity;
    private double mPrecipChance;
    private String mSummary;
    private String mTimeZone;


    //getter and setter methods to can get data from json
    public String getmTimeZone() {
        return mTimeZone;
    }

    public void setmTimeZone(String mTimeZone) {
        this.mTimeZone = mTimeZone;
    }

    public int getmIcon() {


       return Forecast.getIconId(mIcon);


        }


    public void setmIcon(String mIcon) {
        this.mIcon = mIcon;
    }

    public Long getmTime() {
        return mTime;
    }
    // to preforme time in easy way to represent it

    public String getFormattedTime(){
    SimpleDateFormat formatter=new SimpleDateFormat("h:mm a");
    formatter.setTimeZone(TimeZone.getTimeZone(getmTimeZone()));
    // to convert from millisecound
        Date dateTime =new Date(getmTime()*1000);

    String timeString=formatter.format(dateTime);
    return timeString;

}
    public void setmTime(Long mTime) {
        this.mTime = mTime;
    }

    public int getmTemperatre() {
        return (int)Math.round(mTemperature) ;
    }

    public void setmTemperatre(double mTemperatre) {
        this.mTemperature = mTemperatre;
    }

    public double getmHummidity() {
        return mHumidity;
    }

    public void setmHummidity(double mHummidity) {
        this.mHumidity = mHummidity;
    }

    public int getmPrecipChance() {
        double preciplePercentage=mPrecipChance*100;
        return (int)Math.round(preciplePercentage);
    }

    public void setmPrecipChance(double mPrecipChance) {
        this.mPrecipChance = mPrecipChance;
    }

    public String getmSummary() {

        return mSummary;
    }

    public void setmSummary(String mSummary) {
        this.mSummary = mSummary;
    }
}
