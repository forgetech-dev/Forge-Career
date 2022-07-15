package com.example.forgecareer.utils;

import com.example.forgecareer.db.Application;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;

public class ApplicationSorter {
    Map<String, Application> applicationMap;
    Queue<Map.Entry<String, Application>> pq;


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


    static class CreateDateComparator implements Comparator<Map.Entry<String, Application>> {
        @Override
        public int compare(Map.Entry<String, Application> entry1, Map.Entry<String, Application> entry2) {
            Date date1 = DateParser.stringToDate(entry1.getValue().getCreateDate());
            Date date2 = DateParser.stringToDate(entry2.getValue().getCreateDate());
            return date1.compareTo(date2);
        }
    }

}
