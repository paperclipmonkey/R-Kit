package com.example.michaelwaterworth.r_kit;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.Calendar;
import java.util.List;


public class MainActivity extends AppCompatActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {
    private static final String ISFIRSTRUN = "IsFirstRun";

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    /**
     * Called when the user touches the button
     */
    private void startService() {
        SchedulerService.startScheduler(getApplicationContext());
    }

    /**
     * Called when the user touches the button
     */
    public void startIntro(View view) {
        Intent intent = new Intent(this, IntroActivity.class);
        startActivity(intent);
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }


    private void addSchedule(String taskName, String title, String description) {
        Calendar now = Calendar.getInstance();
        now.add(Calendar.MINUTE, 1);
        Task t1 = new Task();
        t1.setDate(now);
        t1.setNotificationTitle(title);
        t1.setNotifDesc(description);
        t1.setClassName(taskName);
        t1.setIsService();
        t1.save();
    }


    private boolean hasSigned() {
        // Restore preferences
        SharedPreferences settings = getPreferences(0);
        return settings.getBoolean(ISFIRSTRUN, false);
    }

    private void setSignedTrue() {
        SharedPreferences settings = getPreferences(0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean("silentMode", true);

        // Commit the edit
        editor.apply();
    }

    public List<Task> getUpcomingTasks() {
        Calendar cal = Calendar.getInstance();
        long sTime = cal.getTimeInMillis() / 1000;

        return Task.find(Task.class, "datetime > ?", "" + sTime);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null && savedInstanceState.containsKey("signed")) {
            setSignedTrue();
        }

        if (!hasSigned()) {
            //Show intro screens
            //TODO
            //startIntro();
        }

        setContentView(R.layout.activity_main);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));

        if (!isMyServiceRunning(SchedulerService.class)) {
            startService();
        }
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        Log.d("Main", "Position: " + position);
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (position == 0) {
            fragmentManager.beginTransaction()
                    .replace(R.id.container, MainFragment.newInstance(position + 1))
                    .commit();
            return;
        }
        if (position == 1) {
            fragmentManager.beginTransaction()
                    .replace(R.id.container, DataFragment.newInstance(position + 1))
                    .commit();
            return;
        }
        if (position == 2) {
            fragmentManager.beginTransaction()
                    .replace(R.id.container, DataFragment.newInstance(position + 1))
                    .commit();
            return;
        }
//        fragmentManager.beginTransaction()
//                .replace(R.id.container, PlaceholderFragment.newInstance(position + 1))
//                .commit();
    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                mTitle = getString(R.string.title_section1);
                break;
            case 2:
                mTitle = getString(R.string.title_section2);
                break;
            case 3:
                mTitle = getString(R.string.title_section3);
                break;
        }
    }

    private void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
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
        if (id == R.id.action_biovici) {
            addSchedule("BioviciReaderTask", "Cortisol check", "Time to measure your cortisol level");
            return true;
        }
        if (id == R.id.action_tapping) {
            addSchedule("TappingTask", "Tapping Speed", "New R-Kit task available");
            return true;
        }
        if (id == R.id.action_spatial) {
            addSchedule("SpatialMemoryTask", "Spatial Memory", "Click to test your spatial memory");
            return true;
        }

        if (id == R.id.action_diary) {
            addSchedule("DiaryTask", "Diary Task", "Click to add a new diary entry");
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
