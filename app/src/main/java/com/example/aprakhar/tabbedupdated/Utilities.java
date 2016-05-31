package com.example.aprakhar.tabbedupdated;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by aprakhar on 15-Feb-16.
 */
public class Utilities {


    public static String formattedTime(String t) throws ParseException {
        String z = "";
        int c = 0;
        if (t.charAt(0) == '0') {
            if (t.charAt(1) == '0') {
                z = t.substring(0, 2) + ":" + t.substring(2) + " AM";
            } else
                z = t.charAt(1) + ":" + t.substring(2) + " AM";
        } else {
            c = Integer.valueOf(t.substring(0, 2));
            if (c < 12) {
                z = String.valueOf(c) + ":" + t.substring(2) + " AM";
            } else if (c == 12) {
                z = String.valueOf(c) + ":" + t.substring(2) + " PM";
            } else {
                z = String.valueOf(c - 12) + ":" + t.substring(2) + " PM";
            }

        }
        return z;
    }

    public static String formattedDate(String d) {
        int month;
        int day;
        month = Integer.valueOf(d.substring(4, 6));
        day = Integer.valueOf(d.substring(6));
        //year=d.substring(0,4);
        d = String.valueOf(day) + "/" + String.valueOf(month) + "/" + d.substring(0, 4);
        return d;
    }

    public static String dateToNumber(int year, int monthOfYear, int dayOfMonth) {
        String month;
        String day;
        String currentYear;
        currentYear = String.valueOf(year);
        monthOfYear = monthOfYear + 1;
        if (monthOfYear / 10 == 0) {
            month = "0" + String.valueOf(monthOfYear);
        } else {
            month = String.valueOf(monthOfYear);
        }
        if (dayOfMonth / 10 == 0) {
            day = "0" + String.valueOf(dayOfMonth);
        } else {
            day = String.valueOf(dayOfMonth);
        }
        return currentYear + month + day;
    }

    public static String timeToNumber(int hourOfDay, int min) {
        String hour;
        String minute;
        if (hourOfDay / 10 == 0) {
            hour = "0" + String.valueOf(hourOfDay);
        } else {
            hour = String.valueOf(hourOfDay);
        }

        if (min / 10 == 0) {
            minute = "0" + String.valueOf(min);
        } else {
            minute = String.valueOf(min);
        }

        return hour + minute;

    }


    /*
    * Returns the number after removing spaces and converting to a 10 digit number in first fragment
    * */

    public String getSelectedNumber(Intent data, Activity activity) {
        Uri contact = data.getData();
        String projection[] = {ContactsContract.CommonDataKinds.Phone.NUMBER};
        Cursor c = activity.getContentResolver().query(contact, projection, null, null, null);
        c.moveToFirst();
        int column = c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
        String number = c.getString(column);
        return number.replaceAll("\\s+", "");

    }

    public static String parsedDate(String date) {
        String parsedD;
        parsedD = date.substring(0, 4) + "-" + date.substring(4, 6) + "-" + date.substring(6);
        return parsedD;
    }

    public static String parsedTime(String time) {
        String parsedT = time.substring(0, 2) + ":" + time.substring(2);
        return parsedT;
    }

    public static List<Date> createDateStringForComparison(List<CallerDetail> list) throws ParseException {
        List<Date> arr = new ArrayList<Date>();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String T;
        Date d1;
        for (int i = 0; i < list.size(); i++) {
            T = parsedDate(list.get(i).getDate()) + " " + parsedTime(list.get(i).getTime());
            d1 = simpleDateFormat.parse(T);
            arr.add(d1);
            Log.d("WWETIME", "Parsed Date is: " + arr.get(i) + "**" + list.get(i).getDate() + " " + list.get(i).getTime());
        }
        return arr;

    }

    public long getTimeInMillis(String date,String time) throws ParseException {
        long timeInMillis;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        timeInMillis=simpleDateFormat.parse(parsedDate(date)+" "+parsedTime(time)).getTime();
        return timeInMillis;
    }
}

