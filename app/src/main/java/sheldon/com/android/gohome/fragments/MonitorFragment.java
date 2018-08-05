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

import java.util.ArrayList;
import java.util.List;

import sheldon.com.android.gohome.R;
import sheldon.com.android.gohome.asynctask.Authenticator;
import sheldon.com.android.gohome.asynctask.Synchronizer;
import sheldon.com.android.gohome.asynctask.SynchronizerListener;
import sheldon.com.android.gohome.content.WidgetMonitor;
import sheldon.com.android.gohome.adapters.MonitorAdapter;

public class MonitorFragment extends Fragment implements SynchronizerListener {
    private List<WidgetMonitor> widgets;
    private RecyclerView rv;

    private Synchronizer client;

    private ArrayList<String> labels, values;
    private ArrayList<Integer> mfIcons, mfColors, mfIconColors;

    private Handler mHandler;

    public MonitorFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        client = new Synchronizer(this);
        mHandler = new Handler();

        widgets = new ArrayList<>();
        labels = new ArrayList<>();
        values = new ArrayList<>();
        mfIcons = new ArrayList<>();
        mfColors = new ArrayList<>();
        mfIconColors = new ArrayList<>();

        mRunnable.run();
//        try {
//            if () {
//                mRunnable.wait();
//            }
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
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
    public void getAttributes(ArrayList<String> labels, ArrayList<String> values, ArrayList<String> icons, ArrayList<String> colors) {
        this.labels = labels;
        this.values = values;

        for (String icon : icons) {
            if (icon.equals("ion ion-thermometer")) {
                mfIcons.add(R.mipmap.ic_temperature_foreground);
            } else if (icon.equals("ion ion-android-alert")) {
                mfIcons.add(R.drawable.logo_white);
            } else if (icon.equals("ion ion-ios-lightbulb")) {
                mfIcons.add(R.mipmap.ic_light_bulb_white_foreground);
            } else if (icon.equals("ion ion-waterdrop")) {
                mfIcons.add(R.mipmap.ic_humidity_foreground);
            } else {
                mfIcons.add(R.drawable.logo_white);
            }
        }

        for (String color : colors) {
            if (color.contains("red")) {
                mfColors.add(Color.parseColor("#d32f2f"));
                mfIconColors.add(Color.parseColor("#9a0007"));
            } else if (color.contains("blue")) {
                mfColors.add(Color.parseColor("#1565c0"));
                mfIconColors.add(Color.parseColor("#003c8f"));
            } else if (color.contains("gray")) {
                mfColors.add(Color.GRAY);
                mfIconColors.add(Color.DKGRAY);
            } else if (color.contains("orange")) {
                mfColors.add(Color.parseColor("#ff8f00"));
                mfIconColors.add(Color.parseColor("#c56000"));
            } else {
                mfColors.add(Color.GRAY);
                mfIconColors.add(Color.DKGRAY);
            }
        }

        initializeData(labels, values, mfIcons, mfColors, mfIconColors);
        initializeAdapterLLM();

        Log.d("ATTR_LABELS", "MonitorFragment: " + labels);
        Log.d("ATTR_VALUES", "MonitorFragment: " + values);
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
            updateData();
            mHandler.postDelayed(mRunnable, 5000);
        }
    };

    public void updateData() {
        client.synchronize(Authenticator.token, Authenticator.uname);

        if (!(widgets.isEmpty())) {
            widgets.clear();
            labels.clear();
            values.clear();
            mfColors.clear();
            mfIconColors.clear();
            mfIcons.clear();
        }
    }
}