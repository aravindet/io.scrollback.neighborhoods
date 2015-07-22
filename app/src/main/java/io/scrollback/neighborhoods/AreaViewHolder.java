package io.scrollback.neighborhoods;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import io.scrollback.neighborhoods.data.AreaModel;

public class AreaViewHolder extends RecyclerView.ViewHolder {

    private final TextView name;
    private final TextView description;
    private final TextView distance;

    public AreaViewHolder(View itemView) {
        super(itemView);

        name = (TextView) itemView.findViewById(R.id.area_name);
        description = (TextView) itemView.findViewById(R.id.area_description);
        distance = (TextView) itemView.findViewById(R.id.area_distance);
    }

    public void bind(AreaModel model) {
        name.setText(model.getName());

        final double meters = model.getDistFromLocation();

        if (meters != 0.0) {
            if (meters > 1000) {
                distance.setText(String.format("%.2f km away", meters / 1000.0));
            } else {
                distance.setText(String.format("%d meters away", Math.round(meters)));
            }
        }

        description.setText(model.getDescription());
    }
}
