package com.example.forgecareer.utils;

import android.util.Log;

import com.example.forgecareer.db.Application;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;

public class ApplicationSorter {
    Map<String, Application> applicationMap;
    Queue<Map.Entry<String, Application>> pq;
    private static final String TAG = "ApplicationSorter";

    public ApplicationSorter(Map<String, Application> applicationMap) {
        this.applicationMap = applicationMap;
    }

    public ArrayList<Map.Entry<String, Application>> sortByCreateDate() {
        pq = new PriorityQueue<>(new CreateDateComparator());
        for (Map.Entry<String, Application> entry : applicationMap.entrySet()) {
            pq.add(entry);
        }
        return new ArrayList<>(pq);
    }

    public ArrayList<Map.Entry<String, Application>> sortByInterviewDate() {
        pq = new PriorityQueue<>(new InterviewDateComparator());
        for (Map.Entry<String, Application> entry : applicationMap.entrySet()) {
            pq.add(entry);
        }
        return new ArrayList<>(pq);
    }


    static class CreateDateComparator implements Comparator<Map.Entry<String, Application>> {
        @Override
        public int compare(Map.Entry<String, Application> entry1, Map.Entry<String, Application> entry2) {
            Date date1 = DateParser.stringToDate(entry1.getValue().getCreateDate());
            Date date2 = DateParser.stringToDate(entry2.getValue().getCreateDate());
            return date1.compareTo(date2);
        }
    }

    static class InterviewDateComparator implements Comparator<Map.Entry<String, Application>> {
        @Override
        public int compare(Map.Entry<String, Application> entry1, Map.Entry<String, Application> entry2) {
            Calendar defaultCalendar = Calendar.getInstance();
            defaultCalendar.set(2000, 01, 01);
            Date date1 = defaultCalendar.getTime();
            Date date2 = defaultCalendar.getTime();
            if (entry1.getValue().getStatus().equals("Interview") && !entry1.getValue().getInterviewDate().equals("N/A")) {
                date1 = DateParser.stringToDate(entry1.getValue().getInterviewDate());
            }

            if (entry2.getValue().getStatus().equals("Interview") && !entry2.getValue().getInterviewDate().equals("N/A")) {
                date2 = DateParser.stringToDate(entry2.getValue().getInterviewDate());
            }
            return date1.compareTo(date2);
        }
    }

}
