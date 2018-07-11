package sheldon.com.android.gohome.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import sheldon.com.android.gohome.R;
import sheldon.com.android.gohome.content.WidgetMonitor;
import sheldon.com.android.gohome.adapters.MonitorAdapter;

public class MonitorFragment extends Fragment {
    private List<WidgetMonitor> widgets;
    private RecyclerView rv;

    public MonitorFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_monitor, container, false);

        rv = (RecyclerView) rootView.findViewById(R.id.rv_monitor);
        rv.setHasFixedSize(true);

        initializeData();
        initializeAdapter();

        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        rv.setLayoutManager(llm);

        return rootView;
    }

    private void initializeData() {
        widgets = new ArrayList<>();
        widgets.add(new WidgetMonitor("Suhu Ruangan", "NA", R.mipmap.ic_temperature_foreground));
        widgets.add(new WidgetMonitor("Kelembaban", "NA", R.mipmap.ic_humidity_foreground));
        widgets.add(new WidgetMonitor("Intensitas Cahaya", "NA", R.mipmap.ic_light_bulb_white_foreground));
    }

    private void initializeAdapter() {
        MonitorAdapter adapter = new MonitorAdapter(widgets);
        rv.setAdapter(adapter);
    }
}