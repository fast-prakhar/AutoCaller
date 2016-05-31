package com.example.aprakhar.tabbedupdated;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Toast;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements Communicator {

    final int  rCode=3;
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private Fragment fragmentB;
    private Fragment fragmentA;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        Log.d("check", toolbar.toString());
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        viewPager = (ViewPager) findViewById(R.id.pager);
        setupViewPager(viewPager);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        final PackageManager pm = getPackageManager();
//get a list of installed apps.
        List<ApplicationInfo> packages = pm.getInstalledApplications(PackageManager.GET_META_DATA);

        for (ApplicationInfo packageInfo : packages) {
            Log.d("APWWE", "Installed package :" + packageInfo.packageName);
            Log.d("APWWE", "Launch Activity :" + pm.getLaunchIntentForPackage(packageInfo.packageName));
        }
        if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CALL_PHONE}, rCode);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case rCode: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {
                    Toast t = Toast.makeText(getApplicationContext(), "Pls give permission", Toast.LENGTH_SHORT);
                    t.show();

                }
                return;
            }
        }
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        fragmentA = new FragmentA();
        fragmentB = new FragmentB();
        adapter.addFragment(fragmentA, "A");
        adapter.addFragment(fragmentB, "B");
        viewPager.setAdapter(adapter);
    }

    public void updateDateNTime(String date, String time) {
        try {
            ((FragmentB) fragmentB).updateContactObject(date, time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateDateTimeListAfterDelete() {
        ((FragmentB) fragmentB).updateDateTimeList();
    }

    @Override
    public void respond(String Number, String Date, String Time, String id, String enabled) {
        ((FragmentB) fragmentB).addData(Number, Date, Time, id, enabled);
        Log.d("WRITTENADDED", "DATA WRITTEN" + Date + Number + Time);
    }

    @Override
    public void signalToAForDateBeingAValidEntryOrNot(boolean flag){
        ((FragmentA) fragmentA).validation(flag);
    }
    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {

            return mFragmentList.get(position);
        }


        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }


    @Override
    public void onTrimMemory(int level) {

        super.onTrimMemory(TRIM_MEMORY_UI_HIDDEN);
    }
}
