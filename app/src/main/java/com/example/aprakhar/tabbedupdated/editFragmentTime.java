package com.example.aprakhar.tabbedupdated;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.widget.TimePicker;

import java.util.Calendar;

/**
 * Created by aprakhar on 24-Feb-16.
 */
public class editFragmentTime extends DialogFragment implements TimePickerDialog.OnTimeSetListener {
    Context cont;


    public void setContext(Context cont) {
        this.cont = cont;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);
        return new TimePickerDialog(getActivity(), this, hour, minute, DateFormat.is24HourFormat(getActivity()));

    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int min) {
        String TIME;
        TIME = new Utilities().timeToNumber(hourOfDay,min);
        Log.d("WWE", "IN editFragmentTime " + TIME);
        ((MainActivity)cont).updateDateNTime(null,TIME);
    }

}
