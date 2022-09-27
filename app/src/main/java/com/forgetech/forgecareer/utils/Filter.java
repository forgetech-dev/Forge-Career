package com.forgetech.forgecareer.utils;

import android.util.Log;

import com.forgetech.forgecareer.db.Application;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Filter {

    private static final String TAG = "Filter";

    public static Map<String, Application> filterByIntern(Map<String, Application> applicationMap) {
        Map<String, Application> filteredMap = new HashMap<>();
        for (Map.Entry<String, Application> entry : applicationMap.entrySet()) {
            if (entry.getValue().getJobType().equals("Intern")) {
                filteredMap.put(entry.getKey(), entry.getValue());
            }
        }
        return filteredMap;
    }

    public static Map<String, Application> filterByFullTime(Map<String, Application> applicationMap) {
        Map<String, Application> filteredMap = new HashMap<>();
        for (Map.Entry<String, Application> entry : applicationMap.entrySet()) {
            if (entry.getValue().getJobType().equals("Full Time")) {
                filteredMap.put(entry.getKey(), entry.getValue());
            }
        }
        return filteredMap;
    }

    public static Map<String, Application> filterByInterested(Map<String, Application> applicationMap) {
        Map<String, Application> filteredMap = new HashMap<>();
        for (Map.Entry<String, Application> entry : applicationMap.entrySet()) {
            if (entry.getValue().getStatus().equals("Interested")) {
                filteredMap.put(entry.getKey(), entry.getValue());
            }
        }
        return filteredMap;
    }

    public static Map<String, Application> filterByApplied(Map<String, Application> applicationMap) {
        Map<String, Application> filteredMap = new HashMap<>();
        for (Map.Entry<String, Application> entry : applicationMap.entrySet()) {
            if (entry.getValue().getStatus().equals("Applied")) {
                filteredMap.put(entry.getKey(), entry.getValue());
            }
        }
        return filteredMap;
    }

    public static Map<String, Application> filterByOA(Map<String, Application> applicationMap) {
        Map<String, Application> filteredMap = new HashMap<>();
        for (Map.Entry<String, Application> entry : applicationMap.entrySet()) {
            if (entry.getValue().getStatus().equals("OA")) {
                filteredMap.put(entry.getKey(), entry.getValue());
            }
        }
        return filteredMap;
    }

    public static Map<String, Application> filterByInterview(Map<String, Application> applicationMap) {
        Map<String, Application> filteredMap = new HashMap<>();
        for (Map.Entry<String, Application> entry : applicationMap.entrySet()) {
            if (entry.getValue().getStatus().equals("Interview")) {
                filteredMap.put(entry.getKey(), entry.getValue());
            }
        }
        return filteredMap;
    }

    public static Map<String, Application> filterByInterviewOA(Map<String, Application> applicationMap) {
        Map<String, Application> filteredMap = new HashMap<>();
        for (Map.Entry<String, Application> entry : applicationMap.entrySet()) {
            if ((entry.getValue().getStatus().equals("Interview") || entry.getValue().getStatus().equals("OA")) && !entry.getValue().getInterviewDate().equals("N/A")) {
                filteredMap.put(entry.getKey(), entry.getValue());
            }
        }
        return filteredMap;
    }

    public static Map<String, Application> filterByOffer(Map<String, Application> applicationMap) {
        Map<String, Application> filteredMap = new HashMap<>();
        for (Map.Entry<String, Application> entry : applicationMap.entrySet()) {
            if (entry.getValue().getStatus().equals("Offer")) {
                filteredMap.put(entry.getKey(), entry.getValue());
            }
        }
        return filteredMap;
    }

    public static Map<String, Application> filterByRejected(Map<String, Application> applicationMap) {
        Map<String, Application> filteredMap = new HashMap<>();
        for (Map.Entry<String, Application> entry : applicationMap.entrySet()) {
            if (entry.getValue().getStatus().equals("Rejected")) {
                filteredMap.put(entry.getKey(), entry.getValue());
            }
        }
        return filteredMap;
    }

    public static Map<String, Application> filterByReferred(Map<String, Application> applicationMap) {
        Map<String, Application> filteredMap = new HashMap<>();
        for (Map.Entry<String, Application> entry : applicationMap.entrySet()) {
            if (!entry.getValue().getReferer().equals("N/A")) {
                filteredMap.put(entry.getKey(), entry.getValue());
            }
        }
        return filteredMap;
    }

    public static Map<String, Application> filterByUndatedActionRequired(Map<String, Application> applicationMap) {
        Map<String, Application> filteredMap = new HashMap<>();
        for (Map.Entry<String, Application> entry : applicationMap.entrySet()) {
            if ((entry.getValue().getStatus().equals("Interview") || entry.getValue().getStatus().equals("OA")) && entry.getValue().getInterviewDate().equals("N/A")) {
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

    public static Map<String, Application> filterActionNeededByDate(Map<String, Application> applicationMap,
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

    public static Map<String, Application> filterByLoseSearch(Map<String, Application> applicationMap, String searchText) {
        if (searchText.equals("")) {
            return applicationMap;
        }
        Map<String, Application> filteredMap = new HashMap<>();
        for (Map.Entry<String, Application> entry : applicationMap.entrySet()) {
            String companyName = entry.getValue().getCompanyName();
            if  (companyName.toLowerCase().contains(searchText.toLowerCase())) {
                filteredMap.put(entry.getKey(), entry.getValue());
            }
        }
        return filteredMap;
    }

}
