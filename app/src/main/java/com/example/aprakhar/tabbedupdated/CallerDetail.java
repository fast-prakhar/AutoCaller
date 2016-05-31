package com.example.aprakhar.tabbedupdated;

import java.util.Comparator;

/**
 * Created by aprakhar on 14-Feb-16.
 */
public class CallerDetail {
    private String Number;
    private String Date;
    private String Time;
    private String Id;
    private String Enabled;


    public CallerDetail(String number, String date, String time, String id, String enabled) {
        Number = number;
        Date = date;
        Time = time;
        Id = id;
        Enabled = enabled;
    }

    public CallerDetail() {
        Number=null;
        Date=null;
        Time=null;
        Id=null;
        Enabled=null;
    }

    public String getNumber() {
        return Number;
    }

    public void setNumber(String number) {
        this.Number = number;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        this.Date = date;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        this.Time = time;
    }

    public String getId() {
        return Id;
    }

    public void setId(String Id) {
        this.Id = Id;
    }

    public String getEnabled() {
        return Enabled;
    }

    public void setEnabled(String enabled) {
        Enabled = enabled;
    }


    public static Comparator<CallerDetail> CallerComparator = new
            Comparator<CallerDetail>() {
                @Override
                public int compare(CallerDetail lhs, CallerDetail rhs) {
                    String c1 = lhs.getDate() + lhs.getTime();
                    String c2 = rhs.getDate() + rhs.getTime();
                    return  c1.compareTo(c2);
                }
            };
}
