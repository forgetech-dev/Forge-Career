package com.example.forgecareer.utils;

import android.annotation.SuppressLint;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateParser {
    SimpleDateFormat sdFormat ;

    @SuppressLint("SimpleDateFormat")
    public DateParser() {
        sdFormat = new SimpleDateFormat("MM/dd/yyyy");
    }

    public String dateToString(Date date) {
        return sdFormat.format(date);
    }

    public Date stringToDate(String dateString) {
        Date date = null;
        try{
            date = sdFormat.parse(dateString);
        }
        catch (ParseException except) {
            except.printStackTrace();
        }
        return date;
    }
}
