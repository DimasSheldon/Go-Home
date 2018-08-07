package sheldon.com.android.gohome.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import sheldon.com.android.gohome.R;
import sheldon.com.android.gohome.asynctask.LoopJ;
import sheldon.com.android.gohome.asynctask.LoopJListener;
import sheldon.com.android.gohome.content.WidgetMonitor;
import sheldon.com.android.gohome.adapters.MonitorAdapter;

public class MonitorFragment extends Fragment implements LoopJListener {
    private ArrayList<WidgetMonitor> widgets;
    private RecyclerView rv;

    private LoopJ client;

    private ArrayList<String> mfLabels, mfValues, icons, colors;
    private ArrayList<Integer> mfIcons, mfColors, mfIconColors;

    private Handler mHandler;

    public MonitorFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        client = new LoopJ(this);
        mHandler = new Handler();

        widgets = new ArrayList<>();

        mfLabels = new ArrayList<>();
        mfValues = new ArrayList<>();
        icons = new ArrayList<>();
        colors = new ArrayList<>();
        mfIcons = new ArrayList<>();
        mfColors = new ArrayList<>();
        mfIconColors = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_monitor, container, false);

        rv = (RecyclerView) rootView.findViewById(R.id.rv_monitor);
        rv.setHasFixedSize(true);

        //widget kosong
        initiateEmptyWidgets();

        return rootView;
    }

    @Override
    public void authenticate(String authStatus) {
    }


    private void getAttributes() {
        Log.d("LOOPJ_SYNC_RESP", "getAttributes: " + LoopJ.syncResponse);

        try {
            for (int i = 1; i < LoopJ.syncResponse.length(); i++) {
                if (LoopJ.syncResponse.has(String.valueOf(i))) {
                    mfLabels.add(LoopJ.syncResponse.getJSONObject(String.valueOf(i)).getString("label"));
                    mfValues.add(LoopJ.syncResponse.getJSONObject(String.valueOf(i)).getString("value"));
                    icons.add(LoopJ.syncResponse.getJSONObject(String.valueOf(i)).getString("icon"));
                    colors.add(LoopJ.syncResponse.getJSONObject(String.valueOf(i)).getString("color"));
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        for (String icon : icons) {
            switch (icon) {
                case "ion ion-thermometer":
                    mfIcons.add(R.mipmap.ic_temperature_foreground);
                    break;
                case "ion ion-android-alert":
                    mfIcons.add(R.drawable.logo_white);
                    break;
                case "ion ion-ios-lightbulb":
                    mfIcons.add(R.mipmap.ic_light_bulb_white_foreground);
                    break;
                case "ion ion-waterdrop":
                    mfIcons.add(R.mipmap.ic_humidity_foreground);
                    break;
                default:
                    mfIcons.add(R.drawable.logo_white);
                    break;
            }
        }

        for (String color : colors) {
            switch (color) {
                case "bg-red":
                    mfColors.add(Color.parseColor("#d32f2f"));
                    mfIconColors.add(Color.parseColor("#9a0007"));
                    break;
                case "bg-blue":
                    mfColors.add(Color.parseColor("#1565c0"));
                    mfIconColors.add(Color.parseColor("#003c8f"));
                    break;
                case "bg-orange":
                    mfColors.add(Color.parseColor("#ff8f00"));
                    mfIconColors.add(Color.parseColor("#c56000"));
                    break;
                default:
                    mfColors.add(Color.GRAY);
                    mfIconColors.add(Color.DKGRAY);
                    break;
            }
        }

        initializeData(mfLabels, mfValues, mfIcons, mfColors, mfIconColors);
        initializeAdapterLLM();

        Log.d("ATTR_LABELS", "MonitorFragment: " + mfLabels);
        Log.d("ATTR_VALUES", "MonitorFragment: " + mfValues);
        Log.d("ATTR_ICONS", "MonitorFragment: " + mfIcons);
        Log.d("ATTR_COLORS", "MonitorFragment: " + mfColors);
    }

    private void initializeData(ArrayList<String> labels,
                                ArrayList<String> status,
                                ArrayList<Integer> icons,
                                ArrayList<Integer> cvColors,
                                ArrayList<Integer> iconColors) {

        for (int i = 0; i < labels.size(); i++) {
            widgets.add(new WidgetMonitor(
                    labels.get(i),
                    status.get(i),
                    icons.get(i),
                    cvColors.get(i),
                    iconColors.get(i)
            ));
        }
    }

    private void initializeAdapterLLM() {
        MonitorAdapter monitorAdapter = new MonitorAdapter(widgets);
        rv.setAdapter(monitorAdapter);
        monitorAdapter.notifyDataSetChanged();

        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        rv.setLayoutManager(llm);
    }

    private void initiateEmptyWidgets() {
        widgets.add(new WidgetMonitor("AI Monitoring 1", "NA", R.drawable.logo_white, Color.GRAY, Color.DKGRAY));
        widgets.add(new WidgetMonitor("AI Monitoring 2", "NA", R.drawable.logo_white, Color.GRAY, Color.DKGRAY));
        widgets.add(new WidgetMonitor("AI Monitoring 3", "NA", R.drawable.logo_white, Color.GRAY, Color.DKGRAY));
        widgets.add(new WidgetMonitor("AI Monitoring 4", "NA", R.drawable.logo_white, Color.GRAY, Color.DKGRAY));
        widgets.add(new WidgetMonitor("AI Monitoring 5", "NA", R.drawable.logo_white, Color.GRAY, Color.DKGRAY));

        initializeAdapterLLM();
    }

    private final Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            if (!LoopJ.isBusy) {
                Log.d("LOOP", "run: synced");
                updateData();
            } else {
//                Log.d("LOOP", "run: still busy");
            }
            mHandler.post(mRunnable);
//            mHandler.postDelayed(mRunnable, 1000);
        }
    };

    public void updateData() {
        client.synchronize(LoopJ.token, LoopJ.uname);
        if (LoopJ.syncResponse != null && !(widgets.isEmpty())) {
            widgets.clear();
            mfLabels.clear();
            mfValues.clear();
            mfColors.clear();
            mfIconColors.clear();
            mfIcons.clear();

            getAttributes();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mRunnable.run();
    }

    @Override
    public void onPause() {
        super.onPause();
        mHandler.removeCallbacks(mRunnable);
    }
}