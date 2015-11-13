package com.qide.bindersizetest.app;

import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ServiceInfo;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;


public class MainActivity extends ActionBarActivity {
    private static final String TAG = "qide";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final int flag = PackageManager.GET_ACTIVITIES |
                PackageManager.GET_META_DATA |
                PackageManager.GET_SERVICES;

        final Button button1 = (Button) findViewById(R.id.callPM);
        button1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                PackageManager pm = getPackageManager();
                List<PackageInfo> packageInfoList = pm.getInstalledPackages(flag);
                Log.i(TAG, "succeeded:" + packageInfoList.size());
            }
        });

        final Button button2 = (Button) findViewById(R.id.callPMForEach);
        button2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                PackageManager pm = getPackageManager();
                List<ApplicationInfo> list = pm.getInstalledApplications(0);
                List<Sizable> result = new ArrayList<Sizable>(list.size());
                int total = 0;
                for (ApplicationInfo info : list) {
                    try {
                        PackageInfo pi = pm.getPackageInfo(info.packageName, flag);
                        int size = getParcelableSize(pi);
                        total += size;
                        result.add(new Sizable(pi.packageName, size));
                    } catch (PackageManager.NameNotFoundException e) {
                        Log.e(TAG, "no name", e);
                    }
                }
                Log.i(TAG, "********************************");
                Log.i(TAG, "");
                Log.i(TAG, "total size: " + total);
                Collections.sort(result);
                for (Sizable s : result) {
                    Log.i(TAG, s.toString());
                }
                Log.i(TAG, "********************************");
            }
        });


        final Button button3 = (Button) findViewById(R.id.callPMForVenezia);
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PackageManager pm = getPackageManager();
                try {
                    PackageInfo pi = pm.getPackageInfo("com.amazon.venezia", flag);
                    Log.i(TAG, "**********");
                    Log.i(TAG, pi.applicationInfo.packageName + ":\t" + getParcelableSize(pi.applicationInfo));
                    Log.i(TAG, "**********");
                    int activityTotal = 0;
                    for (ActivityInfo ai : pi.activities) {
                        int s = getParcelableSize(ai);
                        Log.i(TAG, ai.name + ":\t" + s);
                        activityTotal += s;
                    }
                    Log.i(TAG, pi.activities.length + " activities total:\t" + activityTotal);
                    Log.i(TAG, "**********");
                    int serviceTotal = 0;
                    for (ServiceInfo si : pi.services) {
                        int s = getParcelableSize(si);
                        Log.i(TAG, si.name + ":\t" + s);
                        serviceTotal += s;
                    }
                    Log.i(TAG, pi.services.length + " services total:" + serviceTotal);
                    Log.i(TAG, "**********");

                } catch (PackageManager.NameNotFoundException e) {
                    Log.e(TAG, "no name", e);
                }
            }
        });
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    static int getParcelableSize(Parcelable p) {
        Parcel parcel = Parcel.obtain();
        p.writeToParcel(parcel, 0);
        int size = parcel.dataSize();
        parcel.recycle();
        return size;
    }

}
