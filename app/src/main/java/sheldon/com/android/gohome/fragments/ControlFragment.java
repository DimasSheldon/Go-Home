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
import sheldon.com.android.gohome.adapters.ControlAdapter;
import sheldon.com.android.gohome.content.WidgetControl;

public class ControlFragment extends Fragment {
    private List<WidgetControl> widgets;
    private RecyclerView rv;

    public ControlFragment() {
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
        View rootView = inflater.inflate(R.layout.fragment_control, container, false);

        rv = (RecyclerView) rootView.findViewById(R.id.rv_control);
        rv.setHasFixedSize(true);

        initializeData();
        initializeAdapter();

        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        rv.setLayoutManager(llm);

        return rootView;
    }

    private void initializeData() {
        widgets = new ArrayList<>();
        widgets.add(new WidgetControl("Kontrol-1", "OFF", 0));
        widgets.add(new WidgetControl("Kontrol-2", "OFF", 0));
    }

    private void initializeAdapter() {
        ControlAdapter adapter = new ControlAdapter(widgets);
        rv.setAdapter(adapter);
    }
}