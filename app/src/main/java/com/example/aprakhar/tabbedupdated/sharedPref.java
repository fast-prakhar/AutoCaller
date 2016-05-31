package com.example.aprakhar.tabbedupdated;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by aprakhar on 17-Feb-16.
 */
public class sharedPref {
    public String defvalue = "NULL";

    public String getVal(String id, Activity activity) {
        SharedPreferences sP = activity.getSharedPreferences("hello", Context.MODE_PRIVATE);
        String currentVal = sP.getString(id, defvalue);
        return currentVal;
    }

    public boolean setVal(String id, String value, Activity activity) {
        SharedPreferences sP = activity.getSharedPreferences("hello", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sP.edit();
        editor.putString(id, value);
        for (int i = 1; i <= 3; i++) {
            if (editor.commit() == true)
                return true;
        }
        return false;
    }

    public boolean removeVal(String id, Activity activity) {
        SharedPreferences sP = activity.getSharedPreferences("hello", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sP.edit();
        editor.remove(id);
        for (int i = 1; i <= 3; i++) {
            if (editor.commit() == true)
                return true;
        }
        return false;
    }

}
