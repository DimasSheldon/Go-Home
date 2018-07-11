package sheldon.com.android.gohome.adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import sheldon.com.android.gohome.R;
import sheldon.com.android.gohome.activities.MainActivity;
import sheldon.com.android.gohome.content.WidgetControl;

public class ControlAdapter extends RecyclerView.Adapter<ControlAdapter.WidgetViewHolder> {

    public static class WidgetViewHolder extends RecyclerView.ViewHolder {

        CardView cv;
        TextView widgetName;
        TextView widgetStatus;
        Switch widgetToggleStat;

        public WidgetViewHolder(View itemView) {
            super(itemView);
            cv = (CardView) itemView.findViewById(R.id.cv_control);
            widgetName = (TextView) itemView.findViewById(R.id.widget_name_control);
            widgetStatus = (TextView) itemView.findViewById(R.id.widget_status_control);
            widgetToggleStat = (Switch) itemView.findViewById(R.id.switch_toggle);
        }
    }

    List<WidgetControl> widgets;

    public ControlAdapter(List<WidgetControl> widgets) {
        this.widgets = widgets;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public WidgetViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_control, viewGroup, false);
        WidgetViewHolder wvh = new WidgetViewHolder(v);
        return wvh;
    }

    @Override
    public void onBindViewHolder(final WidgetViewHolder widgetViewHolder, int i) {
        widgetViewHolder.widgetName.setText(widgets.get(i).getName());
        widgetViewHolder.widgetStatus.setText(widgets.get(i).getStatus());
        widgetViewHolder.widgetToggleStat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return widgets.size();
    }
}