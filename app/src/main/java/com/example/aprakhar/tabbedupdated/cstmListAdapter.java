package com.example.aprakhar.tabbedupdated;

/**
 * Created by aprakhar on 14-Feb-16.
 */

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Date;


import java.text.ParseException;
import java.util.List;

/**
 * Created by aprakhar on 10-Feb-16.
 */
public class cstmListAdapter extends BaseAdapter{
    private Activity activity;
    private List<CallerDetail> callers;
    private LayoutInflater inflater;
    private Communicator communicator;


    public cstmListAdapter(Activity activity, List<CallerDetail> callers) {
        this.activity = activity;
        this.callers = callers;
        communicator = (Communicator)activity;
    }

    @Override
    public int getCount() {
        return callers.size();
    }

    @Override
    public Object getItem(int position) {
        return callers.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (inflater == null)
            inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
        }
        convertView = inflater.inflate(R.layout.list_row, null);
        TextView Number = (TextView) convertView.findViewById(R.id.num);
        final TextView Date = (TextView) convertView.findViewById(R.id.dat);
        TextView Time = (TextView) convertView.findViewById(R.id.tim);
        TextView ID = (TextView) convertView.findViewById(R.id.ID);
        final Switch onOff = (Switch) convertView.findViewById(R.id.switch1);
        ImageView delete = (ImageView) convertView.findViewById(R.id.delete);
        final CallerDetail c = callers.get(position);
        if(c.getEnabled().equals("1")){
            onOff.setChecked(true);
        }
        else {
            onOff.setChecked(false);
        }
        Number.setText(c.getNumber());
        try {

            Date.setText(new Utilities().formattedDate(c.getDate()));
            Time.setText(new Utilities().formattedTime(c.getTime()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        ID.setText(c.getId());
        onOff.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    DatabaseHandler d = new DatabaseHandler(activity.getApplicationContext());
                    d.editEnabled(c);
                    try {
                        long timeInMillis = new Utilities().getTimeInMillis(c.getDate(), c.getTime());
                        Date date = new Date();
                        if (timeInMillis <= (date.getTime())) {
                            Toast t = Toast.makeText(activity, "Validation Failed.Try again", Toast.LENGTH_SHORT);
                            t.show();
                            onOff.setChecked(false);
                            c.setEnabled("0");
                        } else {
                            new alarmManager(activity).addAlarm(c.getNumber(), c.getId(), timeInMillis);
                            c.setEnabled("1");
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                } else {
                    c.setEnabled("0");
                    DatabaseHandler d = new DatabaseHandler(activity.getApplicationContext());
                    d.editEnabled(c);
                    new alarmManager(activity).deleteAlarm(c.getNumber(), c.getId());

                }
                //callers.remove(position);
                //communicator.updateDateTimeListAfterDelete();
                notifyDataSetChanged();
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("WWE", "DELETE CLICKED");
                CallerDetail c = callers.get(position);
                notifyDataSetChanged();
                DatabaseHandler d = new DatabaseHandler(activity.getApplicationContext());
                d.deleteEntry(c);
                new alarmManager(activity).deleteAlarm(c.getNumber(), c.getId());
                callers.remove(position);
                communicator.updateDateTimeListAfterDelete();
                notifyDataSetChanged();

            }
        });

        return convertView;
    }
}

