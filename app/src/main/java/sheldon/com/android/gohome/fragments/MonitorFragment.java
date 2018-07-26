package sheldon.com.android.gohome.fragments;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
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
import sheldon.com.android.gohome.activities.LoginActivity;
import sheldon.com.android.gohome.asynctask.Authenticator;
import sheldon.com.android.gohome.asynctask.Synchronizer;
import sheldon.com.android.gohome.asynctask.SynchronizerListener;
import sheldon.com.android.gohome.content.WidgetMonitor;
import sheldon.com.android.gohome.adapters.MonitorAdapter;

public class MonitorFragment extends Fragment implements SynchronizerListener {
    private List<WidgetMonitor> widgets;
    private RecyclerView rv;
    private Authenticator authenticator;
    private Synchronizer client;

    private ArrayList<Integer> icons, cvColors, iconColors;
    private ProgressDialog progressDialog;

    public MonitorFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        progressDialog = new ProgressDialog(getActivity(), R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setCanceledOnTouchOutside(false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_monitor, container, false);
        client = new Synchronizer(this);
        authenticator = new Authenticator();

        rv = (RecyclerView) rootView.findViewById(R.id.rv_monitor);
        rv.setHasFixedSize(true);

        widgets = new ArrayList<>();

        client.synchronize(authenticator.getToken());
        progressDialog.setMessage("Synchronizing...");
        progressDialog.show();

        return rootView;
    }


    @Override
    public void getAttributes(ArrayList<String> labels, ArrayList<String> icons, ArrayList<String> colors) {
        this.icons = new ArrayList<>();
        this.cvColors = new ArrayList<>();
        this.iconColors = new ArrayList<>();

        for (String icon : icons) {
            if (icon.equals("ion ion-thermometer")) {
                this.icons.add(R.mipmap.ic_temperature_foreground);
            } else if (icon.equals("ion ion-android-alert")) {
                this.icons.add(R.drawable.logo_white);
            } else if (icon.equals("ion ion-ios-lightbulb")) {
                this.icons.add(R.mipmap.ic_light_bulb_white_foreground);
            } else if (icon.equals("ion ion-waterdrop")) {
                this.icons.add(R.mipmap.ic_humidity_foreground);
            } else {
                this.icons.add(R.drawable.logo_white);
            }
        }

        for (String color : colors) {
            if (color.contains("red")) {
                this.cvColors.add(Color.parseColor("#d32f2f"));
                this.iconColors.add(Color.parseColor("#9a0007"));
            } else if (color.contains("blue")) {
                this.cvColors.add(Color.parseColor("#1565c0"));
                this.iconColors.add(Color.parseColor("#003c8f"));
            } else if (color.contains("gray")) {
                this.cvColors.add(Color.GRAY);
                this.iconColors.add(Color.DKGRAY);
            } else if (color.contains("orange")) {
                this.cvColors.add(Color.parseColor("#ff8f00"));
                this.iconColors.add(Color.parseColor("#c56000"));
            } else {
                this.cvColors.add(Color.GRAY);
                this.iconColors.add(Color.DKGRAY);
            }
        }

        Log.d("ATTR_LABELS", "getAttributes: " + labels);
        Log.d("ATTR_ICONS", "getAttributes: " + this.icons);
        Log.d("ATTR_COLORS", "getAttributes: " + this.cvColors);

        initializeData(labels, this.icons, this.cvColors, this.iconColors);
        initializeAdapter();
        initializeLLM();

        progressDialog.dismiss();
    }

    private void initializeData(ArrayList<String> labels, ArrayList<Integer> icons,
                                ArrayList<Integer> cvColors, ArrayList<Integer> iconColors) {

        for (int i = 0; i < labels.size(); i++) {
            widgets.add(new WidgetMonitor(labels.get(i), "NA", icons.get(i),
                    cvColors.get(i), iconColors.get(i)));
        }
    }

    private void initializeAdapter() {
        MonitorAdapter adapter = new MonitorAdapter(widgets);
        rv.setAdapter(adapter);
    }

    private void initializeLLM() {
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        rv.setLayoutManager(llm);
    }
}