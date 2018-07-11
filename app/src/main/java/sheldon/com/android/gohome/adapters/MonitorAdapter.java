package sheldon.com.android.gohome.adapters;

import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import sheldon.com.android.gohome.R;
import sheldon.com.android.gohome.content.WidgetMonitor;

public class MonitorAdapter extends RecyclerView.Adapter<MonitorAdapter.WidgetViewHolder> {

    public static class WidgetViewHolder extends RecyclerView.ViewHolder {

        CardView cv;
        TextView widgetName;
        TextView widgetStatus;
        ImageView widgetPhoto;

        public WidgetViewHolder(View itemView) {
            super(itemView);
            cv = (CardView) itemView.findViewById(R.id.cv_monitor);
            widgetName = (TextView) itemView.findViewById(R.id.widget_name_monitor);
            widgetStatus = (TextView) itemView.findViewById(R.id.widget_status_monitor);
            widgetPhoto = (ImageView) itemView.findViewById(R.id.widget_photo_monitor);
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
        widgetViewHolder.widgetName.setText(widgets.get(position).getName());
        widgetViewHolder.widgetStatus.setText(widgets.get(position).getStatus());
        widgetViewHolder.widgetPhoto.setImageResource(widgets.get(position).getPhotoId());

        if (position == 0) {
            widgetViewHolder.itemView.setBackgroundColor(Color.RED);
        } else if (position == 1) {
            widgetViewHolder.itemView.setBackgroundColor(Color.BLUE);
        } else {
            widgetViewHolder.itemView.setBackgroundColor(Color.YELLOW);
        }
    }

    @Override
    public int getItemCount() {
        return widgets.size();
    }
}