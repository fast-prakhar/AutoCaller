package com.example.aprakhar.tabbedupdated;

/**
 * Created by aprakhar on 14-Feb-16.
 */

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class FragmentB extends Fragment {

    private ListView lv1;
    private List<CallerDetail> list;
    private cstmListAdapter adapter;
    private EditText numberEditText;
    private TextView numberEditCancel;
    private TextView numberEditConfirm;
    private CallerDetail selectedContact;
    private static List<Date> dateTime;
    private static String oldNumber;
    static Communicator communicator;
    private int listPosition;
    private alarmManager aManager;
    private Context context;
    private Switch onOff;

    public FragmentB() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_b, container, false);


    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        context=getContext();
        aManager = new alarmManager(context);
        communicator = (Communicator) getActivity();
        numberEditText = (EditText) getActivity().findViewById(R.id.numberEditText);
        numberEditCancel = (TextView) getActivity().findViewById(R.id.numberEditCancel);
        numberEditConfirm = (TextView) getActivity().findViewById(R.id.numberEditConfirm);
        lv1 = (ListView) getActivity().findViewById(R.id.lv1);
        list = new DatabaseHandler(getContext()).getContactList();
        Collections.sort(list, CallerDetail.CallerComparator);
        try {
            dateTime = createDateStringForComparison(list);
        } catch (ParseException e) {
            Log.d("WWE", "Exception thrown");
            e.printStackTrace();
        }
        adapter = new cstmListAdapter(getActivity(), list);
        lv1.setAdapter(adapter);
        lv1.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                return false;
            }
        });
        registerForContextMenu(lv1);
    }


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater menuInflater = getActivity().getMenuInflater();
        menuInflater.inflate(R.menu.context_menu, menu);
        Log.d("WWE", "CONTEXT MENU CREATED");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo adapterContextMenuInfo =
                (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        View v = adapterContextMenuInfo.targetView;
        Log.d("WVEW",v.toString());
        onOff = (Switch) v.findViewById(R.id.switch1);
        Log.d("WVEWS", String.valueOf(onOff.isChecked()));
        // Switch onOff =((Switch)adapterContextMenuInfo.targetView);
        listPosition = adapterContextMenuInfo.position;
        selectedContact = list.get(listPosition);
        Log.d("WWE", "NUMBER " + selectedContact.getNumber());
        Log.d("WWE", "DATE " + selectedContact.getDate());
        Log.d("WWE", "Time " + selectedContact.getTime());
        Log.d("WWE", "ID " + selectedContact.getId());
        Log.d("WWE", "Enabled " + selectedContact.getEnabled());

        switch (item.getItemId()) {
            case R.id.contextNumber:
                final RelativeLayout relativeLayout = (RelativeLayout) getActivity().findViewById(R.id.relativeLayout);
                relativeLayout.setVisibility(View.VISIBLE);
                numberEditText.setText(selectedContact.getNumber());
                numberEditConfirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        oldNumber=selectedContact.getNumber();
                        String newNumber = numberEditText.getText().toString().trim();
                        try {
                            if(checkDateTimeConstraint(dateTime, selectedContact.getDate(), selectedContact.getTime(), selectedContact, listPosition)) {
                                    try {
                                        aManager.updateNumber(oldNumber, newNumber, selectedContact.getId(),
                                                new Utilities().getTimeInMillis(selectedContact.getDate(),
                                                        selectedContact.getTime()));
                                        selectedContact.setEnabled("1");
                                        onOff.setChecked(true);
                                        CallerDetail c = new CallerDetail();
                                        c.setId(selectedContact.getId());
                                        c.setEnabled("1");
                                        new DatabaseHandler(getContext()).editEnabled(c);
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        //DATABASE UPDATION START
                        CallerDetail c = new CallerDetail();
                        c.setNumber(newNumber);
                        c.setId(selectedContact.getId());
                        new DatabaseHandler(getContext()).editNumber(c);
                        //DATABASE UPDATION ENDS

                        selectedContact.setNumber(newNumber);



                        relativeLayout.setVisibility(View.GONE);
                        adapter.notifyDataSetChanged();
                        new FragmentA().closeKeyboard(getContext(), numberEditText.getWindowToken());//FUTURE CHange reqrd
                        Log.d("WWE", "NUMBER AFTER EDIT" + numberEditText.getText().toString());
                    }
                });
                numberEditCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        relativeLayout.setVisibility(View.GONE);
                    }
                });

                Log.d("WWE", "Relative layout working Fine");
                break;
            case R.id.contextDate:
                editFragmentDate editDate = new editFragmentDate();
                editDate.setContext(getActivity());
                editDate.show(getActivity().getFragmentManager(), "Edit Date");
                break;
            case R.id.contexTime:
                editFragmentTime editTime = new editFragmentTime();
                editTime.setContext(getActivity());
                editTime.show(getActivity().getFragmentManager(), "Edit Time");
                break;
        }

        return super.onContextItemSelected(item);
    }

    public void updateContactObject(String date, String time) throws ParseException {
        if (date != null) {
            boolean flag = checkDateTimeConstraint(dateTime, date, time, selectedContact, listPosition);
            if (flag == true) {
                Log.d("WWETIME", "INSIDE DATE TRUE");
                selectedContact.setDate(date);
                selectedContact.setEnabled("1");
                onOff.setChecked(true);
                aManager.updateTime(selectedContact.getNumber(),selectedContact.getId(),
                        new Utilities().getTimeInMillis(selectedContact.getDate(),selectedContact.getTime()));
                Collections.sort(list, CallerDetail.CallerComparator);
                new DatabaseHandler(getContext()).editDate(selectedContact);
                dateTime = createDateStringForComparison(list);
                adapter.notifyDataSetChanged();
            } else {
                Log.d("WWETIME", "INSIDE DATE FALSE");
                Toast t = Toast.makeText(getContext(), "Validation Failed.Try again", Toast.LENGTH_SHORT);
                t.show();
            }

        }
        if (time != null) {
            boolean flag = checkDateTimeConstraint(dateTime, date, time, selectedContact, listPosition);
            if (flag == true) {
                Log.d("WWETIME", "INSIDE TIME TRUE");
                selectedContact.setTime(time);
                selectedContact.setEnabled("1");
                onOff.setChecked(true);
                aManager.updateTime(selectedContact.getNumber(), selectedContact.getId(),
                        new Utilities().getTimeInMillis(selectedContact.getDate(), selectedContact.getTime()));

                Collections.sort(list, CallerDetail.CallerComparator);
                new DatabaseHandler(getContext()).editTime(selectedContact);
                dateTime = createDateStringForComparison(list);
                adapter.notifyDataSetChanged();
            } else {
                Log.d("WWETIME", "INSIDE TIME FALSE");
                Toast t = Toast.makeText(getContext(), "Validation Failed.Try again", Toast.LENGTH_SHORT);
                t.show();
            }

        }
    }

    public void addData(String Number, String Date, String Time, String id, String enabled) {

        CallerDetail callerDetail = new CallerDetail();
        callerDetail.setNumber(Number);
        callerDetail.setDate(Date);
        callerDetail.setTime(Time);
        callerDetail.setId(id);
        callerDetail.setEnabled(enabled);
        try {
            boolean flag = checkDateTimeConstraint(dateTime, Date, Time, null, -1);
            if (flag == true) {
                aManager.addAlarm(Number,id,new Utilities().getTimeInMillis(Date,Time));
                list.add(callerDetail);
                DatabaseHandler dh = new DatabaseHandler(context);
                dh.addContact(callerDetail);
                Collections.sort(list, CallerDetail.CallerComparator);
                dateTime = createDateStringForComparison(list);
                adapter.notifyDataSetChanged();
            }
            communicator.signalToAForDateBeingAValidEntryOrNot(flag);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Log.d("CHANGED", "ADDED " + (list.size()));
    }

    public List<Date> createDateStringForComparison(List<CallerDetail> list) throws ParseException {
        List<Date> arr = new ArrayList<Date>();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String T;
        Date d1;
        for (int i = 0; i < list.size(); i++) {
            T = new Utilities().parsedDate(list.get(i).getDate()) + " " +
                    new Utilities().parsedTime(list.get(i).getTime());
            d1 = simpleDateFormat.parse(T);
            arr.add(d1);
            Log.d("WWETIME", "Parsed Date is: " + arr.get(i) + "**" + list.get(i).getDate() + " " + list.get(i).getTime());
        }
        return arr;

    }

    public boolean checkDateTimeConstraint(List<Date> dateTime, String date, String time, CallerDetail c,
                                           int listPosition) throws ParseException {
        String c1;
        if (c == null) {
            c1 = new Utilities().parsedDate(date) + " " + new Utilities().parsedTime(time);
        } else {
            if (time == null) {
                c1 = new Utilities().parsedDate(date) + " " + new Utilities().parsedTime(c.getTime());
            } else {
                c1 = new Utilities().parsedDate(c.getDate()) + " " + new Utilities().parsedTime(time);
            }
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date dateToBeChecked = simpleDateFormat.parse(c1);
        boolean flag = true;
        long duration;
        long difference;
        Date currentDate = new Date();
        duration = dateToBeChecked.getTime() - currentDate.getTime();
        Log.d("WWETIME", "DURATION " + String.valueOf(duration));
        difference = TimeUnit.MILLISECONDS.toSeconds(duration);
        if (difference <= 0) {
            flag = false;
            return flag;
        }
        for (int i = 0; i < dateTime.size(); i++) {
            if (listPosition != -1 && listPosition == i) {
                Log.d("WWETIME", "INSIDE Continue");
                continue;
            }
            duration = dateTime.get(i).getTime() - dateToBeChecked.getTime();
            Log.d("WWETIME", "Every time " + (dateTime.get(i).getTime()) + " " +
                    dateToBeChecked.getTime());
            Log.d("WWETIME", "Dur " + duration);
            difference = Math.abs(TimeUnit.MILLISECONDS.toSeconds(duration));
            if (difference < 180) {
                flag = false;
                return flag;
            }

        }
        return flag;
    }



    public void updateDateTimeList(){
        Collections.sort(list, CallerDetail.CallerComparator);
        try {
            dateTime = createDateStringForComparison(list);
        } catch (ParseException e) {
            Log.d("WWE", "Exception thrown");
            e.printStackTrace();
        }

    }
}

