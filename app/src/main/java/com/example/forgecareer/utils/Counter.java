package com.example.forgecareer.utils;

import com.example.forgecareer.db.Application;

import java.util.Map;

public class Counter {
    public Counter() {
    }

    public static int countApplied(Map<String, Application> applicationMap) {
        int count = 0;
        for (Map.Entry<String, Application> entry : applicationMap.entrySet()) {
            Application application= entry.getValue();
            if (!application.getStatus().equals("Interested")) {
                count += 1;
            }
        }
        return count;
    }

    public static int countTodo(Map<String, Application> applicationMap) {
        int count = 0;
        for (Map.Entry<String, Application> entry : applicationMap.entrySet()) {
            Application application= entry.getValue();
            if (application.getStatus().equals("Interested")) {
                count += 1;
            }
        }
        return count;
    }
}
