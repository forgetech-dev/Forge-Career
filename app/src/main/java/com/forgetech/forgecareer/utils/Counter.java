package com.forgetech.forgecareer.utils;

import com.forgetech.forgecareer.db.Application;

import java.util.Map;

public class Counter {
    public Counter() {
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

    public static int countApplied(Map<String, Application> applicationMap) {
        int count = 0;
        for (Map.Entry<String, Application> entry : applicationMap.entrySet()) {
            Application application= entry.getValue();
            if (application.getStatus().equals("Applied")) {
                count += 1;
            }
        }
        return count;
    }

    public static int countOA(Map<String, Application> applicationMap) {
        int count = 0;
        for (Map.Entry<String, Application> entry : applicationMap.entrySet()) {
            Application application= entry.getValue();
            if (application.getStatus().equals("OA")) {
                count += 1;
            }
        }
        return count;
    }

    public static int countInterview(Map<String, Application> applicationMap) {
        int count = 0;
        for (Map.Entry<String, Application> entry : applicationMap.entrySet()) {
            Application application= entry.getValue();
            if (application.getStatus().equals("Interview")) {
                count += 1;
            }
        }
        return count;
    }

    public static int countRejected(Map<String, Application> applicationMap) {
        int count = 0;
        for (Map.Entry<String, Application> entry : applicationMap.entrySet()) {
            Application application= entry.getValue();
            if (application.getStatus().equals("Rejected")) {
                count += 1;
            }
        }
        return count;
    }

    public static int countOffer(Map<String, Application> applicationMap) {
        int count = 0;
        for (Map.Entry<String, Application> entry : applicationMap.entrySet()) {
            Application application= entry.getValue();
            if (application.getStatus().equals("Offer")) {
                count += 1;
            }
        }
        return count;
    }

}
