package com.example.forgecareer.utils;

import android.annotation.SuppressLint;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateParser {


    @SuppressLint("SimpleDateFormat")
    public DateParser() {
        SimpleDateFormat sdFormat = new SimpleDateFormat("MM/dd/yyyy");
    }

    public static String dateToString(Date date) {
        SimpleDateFormat sdFormat = new SimpleDateFormat("MM/dd/yyyy");
        return sdFormat.format(date);
    }

    public static Date stringToDate(String dateString) {
        Date date = null;
        SimpleDateFormat sdFormat = new SimpleDateFormat("MM/dd/yyyy");
        try{
            date = sdFormat.parse(dateString);
        }
        catch (ParseException except) {
            except.printStackTrace();
        }
        return date;
    }

    public static  String getCurrentDateTimeString() {
        SimpleDateFormat sdFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        Date date = new Date();
        return sdFormat.format(date);
    }

    public static String getCurrentDateString() {
        SimpleDateFormat sdFormat = new SimpleDateFormat("MM/dd/yyyy");
        Date date = new Date();
        return sdFormat.format(date);
    }


    public static int getCurrentYear() {
        Calendar cal = Calendar.getInstance();
        return cal.get(Calendar.YEAR);
    }

    public static int getCurrentMonth() {
        Calendar cal = Calendar.getInstance();
        return cal.get(Calendar.MONTH)+1;
    }

    public static int getCurrentDay() {
        Calendar cal = Calendar.getInstance();
        return cal.get(Calendar.DATE);
    }
}
