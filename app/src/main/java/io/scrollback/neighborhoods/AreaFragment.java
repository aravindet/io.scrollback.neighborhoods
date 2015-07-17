package io.scrollback.neighborhoods;

import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import io.scrollback.library.NavMessage;
import io.scrollback.neighborhoods.data.AreaModel;
import io.scrollback.neighborhoods.data.AreaProvider;

public class AreaFragment extends Fragment implements SearchView.OnQueryTextListener {

    public static AreaFragment newInstance() {
        return new AreaFragment();
    }

    private static final List<AreaModel> Areas = new AreaProvider().getAreas();

    private RecyclerView mRecyclerView;
    private AreaAdapter mAdapter;
    private List<AreaModel> mModels;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_main, container, false);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setHasOptionsMenu(true);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mModels = new ArrayList<>();

        for (AreaModel area: Areas) {
            mModels.add(area);
        }

        mAdapter = new AreaAdapter(getActivity(), mModels);
        mRecyclerView.setAdapter(mAdapter);

        mRecyclerView.addOnItemTouchListener(
            new RecyclerItemClickListener(getActivity().getApplicationContext(), new RecyclerItemClickListener.OnItemClickListener() {
                @Override public void onItemClick(View view, int position) {
                    AreaModel model = mModels.get(position);

                    model.setSelectTime(new Date());

                    new AreaStore(getActivity()).putArea(model);

                    SbFragment.getInstance().postMessage(new NavMessage("{" +
                            "room: '" + model.getRoomId() + "'," +
                            "mode: 'room'" +
                            "}"));

                    ((MainActivity) getActivity()).hideAreaFragment();
                }
            })
        );
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);

        final MenuItem item = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);

        searchView.setQueryHint(getString(R.string.search_hint));
        searchView.setOnQueryTextListener(this);
    }

    @Override
    public boolean onQueryTextChange(String query) {
        final List<AreaModel> filteredModelList = filter(mModels, query);

        mAdapter.animateTo(filteredModelList);
        mRecyclerView.scrollToPosition(0);

        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    private List<AreaModel> filter(List<AreaModel> models, String query) {
        query = query.toLowerCase();

        final List<AreaModel> filteredModelList = new ArrayList<>();

        for (AreaModel model : models) {
            final String name = model.getName().toLowerCase();
            final String description = model.getDescription().toLowerCase();

            if (name.contains(query) || description.contains(query)) {
                filteredModelList.add(model);
            }
        }

        return filteredModelList;
    }
}
