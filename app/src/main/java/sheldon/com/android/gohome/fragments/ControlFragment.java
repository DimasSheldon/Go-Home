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

import sheldon.com.android.gohome.R;
import sheldon.com.android.gohome.adapters.ControlAdapter;
import sheldon.com.android.gohome.asynctask.LoopJ;
import sheldon.com.android.gohome.asynctask.LoopJListener;
import sheldon.com.android.gohome.content.WidgetControl;

public class ControlFragment extends Fragment implements LoopJListener {
    private ArrayList<WidgetControl> widgets;
    private RecyclerView rv;

    private LoopJ client;

    private ArrayList<String> cfLabels, cfValues, colors;
    private ArrayList<Integer> cfColors, cfIconColors;

    private Handler mHandler;

    public ControlFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        client = new LoopJ(this);
        mHandler = new Handler();

        widgets = new ArrayList<>();

        colors = new ArrayList<>();

        cfLabels = new ArrayList<>();
        cfValues = new ArrayList<>();
        cfColors = new ArrayList<>();
        cfIconColors = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_control, container, false);

        rv = (RecyclerView) rootView.findViewById(R.id.rv_control);
        rv.setHasFixedSize(true);

        //empty widgets
        initiateEmptyWidgets();

        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        rv.setLayoutManager(llm);

        return rootView;
    }

    @Override
    public void authenticate(String authStatus) {
    }

    private void getAttributes() {
        assignAttributes("DO");

        for (String color : colors) {
            switch (color) {
                case "bg-red":
                    cfColors.add(Color.parseColor("#d32f2f"));
                    cfIconColors.add(Color.parseColor("#9a0007"));
                    break;
                case "bg-blue":
                    cfColors.add(Color.parseColor("#1565c0"));
                    cfIconColors.add(Color.parseColor("#003c8f"));
                    break;
                case "bg-orange":
                    cfColors.add(Color.parseColor("#ff8f00"));
                    cfIconColors.add(Color.parseColor("#c56000"));
                    break;
                default:
                    cfColors.add(Color.GRAY);
                    cfIconColors.add(Color.DKGRAY);
                    break;
            }
        }

        initializeData(cfLabels, cfValues, cfColors, cfIconColors);
        initializeAdapterLLM();
    }

    private void initializeData(ArrayList<String> labels,
                                ArrayList<String> status,
                                ArrayList<Integer> cvColors,
                                ArrayList<Integer> iconColors) {

        for (int i = 0; i < labels.size(); i++) {
            widgets.add(
                    new WidgetControl(
                            labels.get(i),
                            status.get(i),
                            cvColors.get(i),
                            iconColors.get(i)
                    ));
        }
    }

    private void initializeAdapterLLM() {
        ControlAdapter controlAdapter = new ControlAdapter(widgets);
        rv.setAdapter(controlAdapter);
    }

    private final Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            if (!LoopJ.isBusy) {
                Log.d("LOOP", "run: synced");
                updateData();
            } else {
                Log.d("LOOP", "run: still busy");
            }
//            mHandler.post(mRunnable);
            mHandler.postDelayed(mRunnable, 5000);
        }
    };

    public void updateData() {
        client.synchronize(LoopJ.token, LoopJ.uname);
        if (LoopJ.syncResponse != null && !(widgets.isEmpty())) {
            widgets.clear();
            cfLabels.clear();
            cfValues.clear();
            colors.clear();
            cfColors.clear();
            cfIconColors.clear();

            getAttributes();
        }
    }

    private void initiateEmptyWidgets() {
        for (int i = 1; i <= 5; i++)
            widgets.add(new WidgetControl("Control " + String.valueOf(i), "NA", Color.GRAY, Color.DKGRAY));
        initializeAdapterLLM();
    }

    private void assignAttributes(String key) {
        JSONObject jsonObject = LoopJ.syncResponse;

        for (int i = 1; i < jsonObject.length(); i++) {
            if (jsonObject.has(String.valueOf(i))) {

                try {
                    JSONObject attribute = jsonObject.getJSONObject(String.valueOf(i));

                    if (attribute.names().toString().contains(key)) {
                        cfLabels.add(attribute.getString("label" + key));
                        cfValues.add(attribute.getString("value" + key));
                        colors.add(attribute.getString("color" + key));
                    }

                } catch (JSONException e1) {
                    e1.printStackTrace();
                }
            }
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