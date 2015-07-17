package io.scrollback.neighborhoods;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.scrollback.neighborhoods.data.AreaModel;

public class AreaStore {
    private static SharedPreferences storage;

    private static final String STORE_NAME = "AREA_STORE";

    AreaStore(Context c) {
        if (storage == null){
            storage = c.getSharedPreferences(STORE_NAME, Context.MODE_PRIVATE );
        }
    }

    private AreaModel parseString(String json) {
        AreaModel area = null;

        if (json != null) {
            try {
                JSONObject areaObject = new JSONObject(json);

                area = new AreaModel(areaObject);
            } catch (JSONException e) {
                Log.d(Constants.TAG, e.getMessage());
            }
        }

        return area;
    }

    public AreaModel getArea(String roomId) {
        AreaModel area = null;

        return parseString(storage.getString(roomId, null));
    }

    public void putArea(AreaModel area) {
        SharedPreferences.Editor e = storage.edit();

        if (area != null) {
            JSONObject json = area.toJSONObject();

            if (json != null) {
                e.putString(area.getRoomId(), json.toString());
                e.apply();
            }
        }
    }

    public void removeArea(AreaModel area) {
        SharedPreferences.Editor e = storage.edit();

        e.remove(area.getRoomId());
        e.apply();
    }

    public void removeArea(String roomId) {
        SharedPreferences.Editor e = storage.edit();

        e.remove(roomId);
        e.apply();
    }

    public List<AreaModel> getAll() {
        List<AreaModel> list = new ArrayList<>();

        Map<String, ?> all = storage.getAll();

        for (Map.Entry<String, ?> entry : all.entrySet()) {
            AreaModel area = parseString(entry.getValue().toString());

            if (area != null) {
                list.add(area);
            }
        }

        return list;
    }
}
