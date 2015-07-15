package io.scrollback.neighborhoods;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import io.scrollback.neighborhoods.data.AreaModel;

public class AreaAdapter extends RecyclerView.Adapter<AreaViewHolder> {

    private final LayoutInflater mInflater;
    private final List<AreaModel> mModels;

    public AreaAdapter(Context context, List<AreaModel> models) {
        mInflater = LayoutInflater.from(context);
        mModels = new ArrayList<>(models);
    }

    @Override
    public AreaViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View itemView = mInflater.inflate(R.layout.area_row, parent, false);

        return new AreaViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(AreaViewHolder holder, int position) {
        final AreaModel model = mModels.get(position);

        holder.bind(model);
    }

    @Override
    public int getItemCount() {
        return mModels.size();
    }

    public void animateTo(List<AreaModel> models) {
        applyAndAnimateRemovals(models);
        applyAndAnimateAdditions(models);
        applyAndAnimateMovedItems(models);
    }

    private void applyAndAnimateRemovals(List<AreaModel> newModels) {
        for (int i = mModels.size() - 1; i >= 0; i--) {
            final AreaModel model = mModels.get(i);

            if (!newModels.contains(model)) {
                removeItem(i);
            }
        }
    }

    private void applyAndAnimateAdditions(List<AreaModel> newModels) {
        for (int i = 0, count = newModels.size(); i < count; i++) {
            final AreaModel model = newModels.get(i);

            if (!mModels.contains(model)) {
                addItem(i, model);
            }
        }
    }

    private void applyAndAnimateMovedItems(List<AreaModel> newModels) {
        for (int toPosition = newModels.size() - 1; toPosition >= 0; toPosition--) {
            final AreaModel model = newModels.get(toPosition);
            final int fromPosition = mModels.indexOf(model);

            if (fromPosition >= 0 && fromPosition != toPosition) {
                moveItem(fromPosition, toPosition);
            }
        }
    }

    public AreaModel removeItem(int position) {
        final AreaModel model = mModels.remove(position);

        notifyItemRemoved(position);

        return model;
    }

    public void addItem(int position, AreaModel model) {
        mModels.add(position, model);

        notifyItemInserted(position);
    }

    public void moveItem(int fromPosition, int toPosition) {
        final AreaModel model = mModels.remove(fromPosition);

        mModels.add(toPosition, model);

        notifyItemMoved(fromPosition, toPosition);
    }
}
