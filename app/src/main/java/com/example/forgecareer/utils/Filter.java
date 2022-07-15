package com.example.forgecareer.utils;

import android.util.Log;

import com.example.forgecareer.db.Application;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Filter {

    private static final String TAG = "Filter";

    public static Map<String, Application> filterByInterview(Map<String, Application> applicationMap) {
        Map<String, Application> filteredMap = new HashMap<>();
        for (Map.Entry<String, Application> entry : applicationMap.entrySet()) {
            if (entry.getValue().getStatus().equals("Interview")) {
                filteredMap.put(entry.getKey(), entry.getValue());
            }
        }
        return filteredMap;
    }



    public static Map<String, Application> filterByDate(Map<String, Application> applicationMap,
                                                        org.threeten.bp.LocalDate localDate) {
        Map<String, Application> filteredMap = new HashMap<>();
        int year = localDate.getYear();
        int month = localDate.getMonthValue();
        int day = localDate.getDayOfMonth();
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month-1, day, 0, 0, 0);
        Date date = calendar.getTime();
        for (Map.Entry<String, Application> entry : applicationMap.entrySet()) {
            Date interviewDate = DateParser.stringToDate(entry.getValue().getInterviewDate());
            Log.d(TAG, "date1: " + date.toString());
            Log.d(TAG, "interviewDate: " + interviewDate.toString());
            Log.d(TAG, "date.compareTo(interviewDate: " + date.compareTo(interviewDate));
            Calendar cal1 = Calendar.getInstance();
            Calendar cal2 = Calendar.getInstance();
            cal1.setTime(date);
            cal2.setTime(interviewDate);
            if (date.compareTo(interviewDate) <= 0) {
                filteredMap.put(entry.getKey(), entry.getValue());
            }
            else if (cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR) &&
                    cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR)) {
                filteredMap.put(entry.getKey(), entry.getValue());
            }
        }
        return filteredMap;
    }

}
