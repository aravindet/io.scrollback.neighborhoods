package io.scrollback.neighborhoods;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import io.scrollback.neighborhoods.data.AreaModel;

public class AreaViewHolder extends RecyclerView.ViewHolder {

    private final TextView name;
    private final TextView description;

    public AreaViewHolder(View itemView) {
        super(itemView);

        name = (TextView) itemView.findViewById(R.id.area_name);
        description = (TextView) itemView.findViewById(R.id.area_description);
    }

    public void bind(AreaModel model) {
        name.setText(model.getName());
        if(model.getDistFromLocation()!=0.0)
            description.setText(String.format("%.2f km away",model.getDistFromLocation()/1000.0));
        description.setText(model.getDescription());
    }
}
