package com.example.aprakhar.tabbedupdated;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.DatePicker;

import java.util.Calendar;

/**
 * Created by aprakhar on 24-Feb-16.
 */
public class editFragmentDate extends DialogFragment implements DatePickerDialog.OnDateSetListener {
    Context cont;

    public editFragmentDate(){

    }
    public void setContext (Context context) {
        this.cont=context;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        String DATE;
        DATE = new Utilities().dateToNumber(year, monthOfYear, dayOfMonth);
        Log.d("WWE", "DATE EDITED IS " + DATE);
        ((MainActivity)cont).updateDateNTime(DATE, null);
    }
}
