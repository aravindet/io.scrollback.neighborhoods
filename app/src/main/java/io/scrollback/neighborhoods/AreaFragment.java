package io.scrollback.neighborhoods;

import android.location.Location;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

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

    private AreaAdapter currentAdapter;
    private List<AreaModel> currentModel;

    private double lastSeenLatitude = 0;
    private double lastSeenLongitude = 0;

    private List<AreaModel> allAreas = new ArrayList<>();
    private List<AreaModel> recentAreas = new ArrayList<>();

    private View mSearchEditFrame;

    private boolean isRecentActive;

    private void setAdapter(boolean isRecent) {
        if (mRecyclerView == null) {
            return;
        }

        if (isRecent && recentAreas.size() > 0) {
            currentModel = recentAreas;

            isRecentActive = true;
        } else {
            if (lastSeenLatitude != 0 && lastSeenLongitude != 0) {
                Collections.sort(allAreas, new AreaStore.AreaSorter(lastSeenLatitude, lastSeenLongitude));
            }

            currentModel = allAreas;

            isRecentActive = false;
        }

        currentAdapter = new AreaAdapter(getActivity(), currentModel);
        mRecyclerView.setAdapter(currentAdapter);
    }

    public void setLocation(Location location) {
        lastSeenLatitude = location.getLatitude();
        lastSeenLongitude = location.getLongitude();

        if (!isRecentActive) {
            setAdapter(false);
        }
    }

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

        List<AreaModel> all = new AreaStore(getActivity()).getAll();

        int size = all.size();

        if (size > 0) {
            Collections.sort(all, new Comparator<AreaModel>() {
                public int compare(final AreaModel a, final AreaModel b) {
                    Date dateA = a.getSelectTime();
                    Date dateB = b.getSelectTime();

                    if (dateA == null && dateB == null) {
                        return 0;
                    } else if (dateA == null && dateB != null) {
                        return 1;
                    } else if (dateB == null && dateA != null) {
                        return -1;
                    } else {
                        if (dateA.before(dateB)) {
                            return 1;
                        } else if (dateA.after(dateB)) {
                            return -1;
                        } else {
                            return 0;
                        }
                    }
                }
            });

            recentAreas = all.subList(Math.max(size - 5, 0), size);
        }

        for (AreaModel area: Areas) {
            allAreas.add(area);
        }

        setAdapter(true);

        mRecyclerView.addOnItemTouchListener(
            new RecyclerItemClickListener(getActivity().getApplicationContext(), new RecyclerItemClickListener.OnItemClickListener() {
                @Override public void onItemClick(View view, int position) {
                    AreaModel model = currentModel.get(position);

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

        final MenuItem menuItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(menuItem);

        menuItem.setShowAsAction(MenuItemCompat.SHOW_AS_ACTION_ALWAYS);

        mSearchEditFrame = searchView.findViewById(android.support.v7.appcompat.R.id.search_edit_frame);

        ViewTreeObserver observer = mSearchEditFrame.getViewTreeObserver();

        observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            int oldVisibility = mSearchEditFrame.getVisibility();

            @Override
            public void onGlobalLayout() {

                int currentVisibility = mSearchEditFrame.getVisibility();

                if (currentVisibility != oldVisibility) {
                    if (currentVisibility == View.VISIBLE) {
                        setAdapter(false);
                    } else {
                        setAdapter(true);
                    }

                    oldVisibility = currentVisibility;
                }

            }
        });

        searchView.setQueryHint(getString(R.string.search_hint));
        searchView.setOnQueryTextListener(this);
    }

    @Override
    public boolean onQueryTextChange(String query) {
        final List<AreaModel> filteredModelList = filter(currentModel, query);

        currentAdapter.animateTo(filteredModelList);
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
