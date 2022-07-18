package com.forgetech.forgecareer.utils;

import com.forgetech.forgecareer.db.Application;

import java.util.ArrayList;
import java.util.Map;

public class Utilities {
    public static int findIndex(String[] array, String target) {
        int position = -1;
        for (int i = 0; i < array.length; i++) {
            if (array[i].equals(target)) {
                position = i;
            }
        }
        return position;
    }

    public static String printEntries(ArrayList<Map.Entry<String, Application>> entries) {
        String  res = "";
        for (Map.Entry<String, Application> entry : entries) {
            res += entry.getValue().getCompanyName() + "  priority: " + entry.getValue().getPriority() + "   status:" + entry.getValue().getStatus() + "\n";
        }
        return res;
    }
}
