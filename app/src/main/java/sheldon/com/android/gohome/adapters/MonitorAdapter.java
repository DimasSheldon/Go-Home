package sheldon.com.android.gohome.adapters;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import sheldon.com.android.gohome.R;
import sheldon.com.android.gohome.content.WidgetMonitor;

public class MonitorAdapter extends RecyclerView.Adapter<MonitorAdapter.WidgetViewHolder> {

    public static class WidgetViewHolder extends RecyclerView.ViewHolder {

        CardView cv;
        TextView widgetLabel;
        TextView widgetStatus;
        ImageView widgetIcon;
        View view;

        public WidgetViewHolder(View itemView) {
            super(itemView);
            cv = (CardView) itemView.findViewById(R.id.cv_monitor);
            widgetLabel = (TextView) itemView.findViewById(R.id.widget_name_monitor);
            widgetStatus = (TextView) itemView.findViewById(R.id.widget_status_monitor);
            widgetIcon = (ImageView) itemView.findViewById(R.id.widget_photo_monitor);

            view = itemView;
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // item clicked
                    Toast.makeText(v.getContext(), "Position:" +
                                    Integer.toString(getPosition()), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    List<WidgetMonitor> widgets;

    public MonitorAdapter(List<WidgetMonitor> widgets) {
        this.widgets = widgets;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public WidgetViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_monitor, viewGroup, false);
        WidgetViewHolder wvh = new WidgetViewHolder(v);
        return wvh;
    }

    @Override
    public void onBindViewHolder(WidgetViewHolder widgetViewHolder, int position) {
        Log.d("OnBindMonitor", String.valueOf(position));
        widgetViewHolder.cv.setBackgroundColor(widgets.get(position).cvColor());
        widgetViewHolder.widgetIcon.setImageResource(widgets.get(position).getIcon());
        widgetViewHolder.widgetIcon.setBackgroundColor(widgets.get(position).iconColor());
        widgetViewHolder.widgetLabel.setText(widgets.get(position).getLabel());
        widgetViewHolder.widgetStatus.setText(widgets.get(position).getStatus());
    }

    @Override
    public int getItemCount() {
        return widgets.size();
    }
}