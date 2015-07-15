package io.scrollback.neighborhoods;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import io.scrollback.neighborhoods.data.AreaModel;

public class AreaViewHolder extends RecyclerView.ViewHolder {

    private final TextView title;

    public AreaViewHolder(View itemView) {
        super(itemView);

        title = (TextView) itemView.findViewById(R.id.area_title);
    }

    public void bind(AreaModel model) {
        title.setText(model.getName());
    }
}
