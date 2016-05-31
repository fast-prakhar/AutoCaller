package com.example.aprakhar.tabbedupdated;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

/**
 * Created by aprakhar on 18-Feb-16.
 */
public class alarmManager {
    Context context;
    public alarmManager(Context context) {
        this.context=context;
    }

    public void addAlarm(String phoneNumber,String id,Long timeInMillis){
        PackageManager pm = context.getPackageManager();
        try {
            PackageInfo p = pm.getPackageInfo("com.android.phone", PackageManager.GET_META_DATA);
            Log.d("WTE",p.packageName);
            Log.d("WTE",p.toString());
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        int rCode = 0;
        Log.d("WWE", "ALARM Added");
        Log.d("WWE", "In addAlarm " + phoneNumber + " " + id);
        Intent I = new Intent(Intent.ACTION_CALL);
        I.setData(Uri.parse("tel:" + phoneNumber));
        //I.setPackage("com.android.phone");
        I.putExtra("ID", id);
        PendingIntent P = PendingIntent.getActivity(context,0,I,0);
        AlarmManager alarm = ((AlarmManager)context.getSystemService(Context.ALARM_SERVICE));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                alarm.setExact(AlarmManager.RTC_WAKEUP, timeInMillis, P);
        }
    }
    public void deleteAlarm(String phoneNumber,String id){
        Log.d("WWE","ALARM Deleted");
        Log.d("WWE", "In Delete " + phoneNumber + " " + id);

        Intent I = new Intent(Intent.ACTION_CALL);
        I.setData(Uri.parse("tel:" + phoneNumber));
        //I.setPackage("com.android.phone");
        I.putExtra("ID", id);
        PendingIntent P = PendingIntent.getActivity(context,0,I,0);
        AlarmManager alarm = ((AlarmManager)context.getSystemService(Context.ALARM_SERVICE));
        alarm.cancel(P);
        P.cancel();

    }
    public void updateTime(String phoneNumber,String id,Long timeInMillis){
        Log.d("WWE","ALARM edited");
        Log.d("WWE", "In editTime " + phoneNumber + " " + id);

        Intent I = new Intent(Intent.ACTION_CALL);
        I.setData(Uri.parse("tel:" + phoneNumber));
        //I.setPackage("com.android.phone");
        I.putExtra("ID", id);
        PendingIntent P = PendingIntent.getActivity(context,0,I,0);
        AlarmManager alarm = ((AlarmManager)context.getSystemService(Context.ALARM_SERVICE));
        alarm.cancel(P);
        P.cancel();
        PendingIntent NP = PendingIntent.getActivity(context,0,I,0);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            alarm.setExact(AlarmManager.RTC_WAKEUP,timeInMillis,NP);
        }
    }
    public void updateNumber(String oldNumber,String newNumber,String id,Long timeInMillis){
        Log.d("WWE","ALARM Added");
        Log.d("WWE", "In update " + oldNumber + " " + newNumber + " " + id);

        Intent I = new Intent(Intent.ACTION_CALL);
        I.setData(Uri.parse("tel:" + oldNumber));
        I.putExtra("ID", id);
        //I.setPackage("com.android.phone");
        PendingIntent P = PendingIntent.getActivity(context,0,I,0);
        AlarmManager alarm = ((AlarmManager)context.getSystemService(Context.ALARM_SERVICE));
        alarm.cancel(P);
        P.cancel();


        Intent N = new Intent(Intent.ACTION_CALL);
        N.setData(Uri.parse("tel:" + newNumber));
        N.putExtra("ID", id);
        //N.setPackage("com.android.phone");
        PendingIntent NP = PendingIntent.getActivity(context,0,N,0);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            alarm.setExact(AlarmManager.RTC_WAKEUP,timeInMillis,NP);
        }


    }

}
