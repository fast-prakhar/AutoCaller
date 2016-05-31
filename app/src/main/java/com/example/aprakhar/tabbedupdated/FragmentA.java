package com.example.aprakhar.tabbedupdated;

/**
 * Created by aprakhar on 14-Feb-16.
 */

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;


public class FragmentA extends Fragment implements ImageView.OnClickListener {

    private static final int PICK_CONTACT_REQUEST = 1;
    private static ImageView imageView;
    private static EditText editText;
    private static String NUMBER;
    private static String DATE;
    private static String TIME;
    private static TextView tv2;
    private static sharedPref shared;
    private static String recentlyUsed;
    private static Context context;
    private static ImageButton submitButton;

    static ViewPager viewPager;
    static String TAG = "WWE";
    static Communicator communicator;

    public FragmentA() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.temp, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getActivity().getWindow().getDecorView().clearFocus();

        viewPager = (ViewPager) getActivity().findViewById(R.id.pager);
        shared = new sharedPref();
        submitButton = (ImageButton) getActivity().findViewById(R.id.submit_button);
        communicator = (Communicator) getActivity();
        imageView = (ImageView) getActivity().findViewById(R.id.iV1);
        editText = (EditText) getActivity().findViewById(R.id.eT1);
        imageView.setOnClickListener(this);
        context = getContext();
        closeKeyboard(getContext(), editText.getWindowToken());
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeKeyboard(getContext(), editText.getWindowToken());
                NUMBER = editText.getText().toString().trim();
                DialogFragment d = new FragmentDate();
                d.show(getActivity().getFragmentManager(), "Date");

            }
        });
        editText.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() >= 10) {
                    submitButton.setVisibility(View.VISIBLE);
                }
                else submitButton.setVisibility(View.INVISIBLE);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        tv2 = (TextView) getActivity().findViewById(R.id.tV2);
        recentlyUsed = shared.getVal("RECENT", getActivity());
        if (recentlyUsed.equals(shared.defvalue) == false) {
            tv2.setText(recentlyUsed);
            Log.d(TAG, "THERE WAS A RECENT NUMBER DEFINED ");
        } else {
            Log.d(TAG, "THERE WAS NO RECENT NUMBER DEFINED ");
        }
        tv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText.setText(tv2.getText());
                tv2.setText("");
            }
        });

    }

    //For resetting the edittext to its init mode as in case there were 10 digits already entered and we are swiping from third fragment
    //to second fragment ,it will automatically load the date picker(which is being called on completion of 10 digits
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        try {
            if (!isVisibleToUser) {
                if (isVisible()) {
                    if (editText.length() != 0) {
                        tv2.setText(editText.getText());
                        if (shared.setVal("RECENT", editText.getText().toString().trim(), getActivity()) == true) {
                            Log.d(TAG, "SUCCESS IN STORING TO SHARED PREFERENCES");
                        } else {
                            Log.d(TAG, "FAILURE IN STORING TO SHARED PREFERENCES");
                        }
                        editText.setText("");
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.setUserVisibleHint(isVisibleToUser);
    }

    @Override
    public void onClick(View v) {

        Intent getContact = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        getContact.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
        startActivityForResult(getContact, PICK_CONTACT_REQUEST);


        //closeKeyboard(getContext(), editText.getWindowToken());
        //String t = editText.getText().toString();
        //communicator.respond("TEMP","TEMP",t);

    }

    public static void closeKeyboard(Context c, IBinder windowToken) {
        InputMethodManager mgr = (InputMethodManager) c.getSystemService(Context.INPUT_METHOD_SERVICE);
        mgr.hideSoftInputFromWindow(windowToken, 0);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICK_CONTACT_REQUEST) {
            if (resultCode == getActivity().RESULT_OK) {
                Utilities u = new Utilities();
                NUMBER = u.getSelectedNumber(data, getActivity());
                Log.d(TAG, "NUMBER SELECTED IS " + NUMBER);
                editText.setText(NUMBER);
                Log.d(TAG, "NUMBER ADDED TO EDITEXT is " + editText.getText().toString());

            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public static class FragmentDate extends DialogFragment implements DatePickerDialog.OnDateSetListener {
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
            DATE = new Utilities().dateToNumber(year, monthOfYear, dayOfMonth);
            DialogFragment time = new FragmentTime();
            time.show(getFragmentManager(), "Time");
            Log.d(TAG, "DATE SELECTED IS " + DATE);
        }

    }

    public static class FragmentTime extends DialogFragment implements TimePickerDialog.OnTimeSetListener {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);
            return new TimePickerDialog(getActivity(), this, hour, minute, DateFormat.is24HourFormat(getActivity()));

        }

        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int min) {
            int id = 0;
            TIME = new Utilities().timeToNumber(hourOfDay, min);
            Log.d(TAG, "TIME SELECTED IS " + TIME);
            if (shared.getVal("ID", getActivity()).equals(shared.defvalue) == true) {
                shared.setVal("ID", "1", getActivity());
                id = 1;
            } else {
                id = Integer.parseInt(shared.getVal("ID", getActivity()));
                id++;
                shared.setVal("ID", String.valueOf(id), getActivity());
            }
            //DatabaseHandler dh = new DatabaseHandler(context);
            //CallerDetail callerDetail = new CallerDetail(NUMBER, DATE, TIME, String.valueOf(id), "1");
            //dh.addContact(callerDetail);
            communicator.respond(NUMBER, DATE, TIME, String.valueOf(id), "1");
            //viewPager.setCurrentItem(1, true);
           // Log.d(TAG, "DATA PASSED TO HANDLER" + TIME + " " + DATE + " " + NUMBER);


        }


    }
    public void validation(boolean flag){
        if(flag == true){
            viewPager.setCurrentItem(1,true);
        }
        else{
            Toast t = Toast.makeText(getContext(), "Validation Failed.Try again", Toast.LENGTH_SHORT);
            t.show();
        }
    }

}
