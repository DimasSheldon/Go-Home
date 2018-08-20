package sheldon.com.android.gohome.activities;

import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import sheldon.com.android.gohome.R;
import sheldon.com.android.gohome.asynctask.LoopJ;
import sheldon.com.android.gohome.asynctask.LoopJListener;
import sheldon.com.android.gohome.fragments.ControlFragment;
import sheldon.com.android.gohome.fragments.MonitorFragment;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, LoopJListener {

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private LoopJ client;
    private Handler mHandler;
    private ArrayList<JSONObject> prevData;
    public static int countUpdateCtrl = 0;
    public static int countUpdateMntr = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        client = new LoopJ(this);
        mHandler = new Handler();
        prevData = new ArrayList<>();

        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch (id) {
            case R.id.nav_monitoring:
                viewPager.setCurrentItem(0);
                break;
            case R.id.nav_control:
                viewPager.setCurrentItem(1);
                break;
            case R.id.nav_management:
//                viewPager.setCurrentItem(1);
                break;
            case R.id.nav_logout:
                Toast.makeText(this, "Logout", Toast.LENGTH_SHORT).show();
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new MonitorFragment(), "MONITOR");
        adapter.addFragment(new ControlFragment(), "CONTROL");
        viewPager.setAdapter(adapter);
    }

    @Override
    public void authenticate(String authStatus) {

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

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }
    }

    private final Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            if (!LoopJ.isBusy) {
                Log.d("MAIN_ACTIVITY", "run: synced");
                updateFragment();
            } else {
                Log.d("MAIN_ACTIVITY", "run: syncing");
            }
            mHandler.postDelayed(mRunnable, 1000);
        }
    };

    private void updateFragment() {

        client.synchronize(LoopJ.token, LoopJ.uname);
        JSONObject response = LoopJ.syncResponse;

        if (response != null) {

            JSONObject currData;
            Iterator<?> keys = response.keys();

            while (keys.hasNext()) {
                String key = (String) keys.next();
                try {
                    // set data for Analog Input and Digital Input
                    if (key.contains("AI") || key.contains("DI")) {
                        currData = new JSONObject(response.get(key).toString());

                        if (currData.getString("status").equals("ACTIVE")) {
                            countUpdateMntr++;
                            MonitorFragment.updateData(currData);
                        }
                    } else {
                        currData = new JSONObject(response.get(key).toString());

                        if (currData.getString("status").equals("ACTIVE")) {
                            countUpdateCtrl++;
                            ControlFragment.updateData(currData);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            MonitorFragment.removeUnusedWidgets();
            ControlFragment.removeUnusedWidgets();
            MonitorFragment.resetPosition();
            ControlFragment.resetPosition();
            countUpdateMntr = 0;
            countUpdateCtrl = 0;
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        mRunnable.run();
    }


    @Override
    public void onPause() {
        super.onPause();
        mHandler.removeCallbacks(mRunnable);
    }
}