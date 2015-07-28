package io.scrollback.neighborhoods;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import io.scrollback.neighborhoods.data.AreaModel;

public class AreaStore {
    private static SharedPreferences storage;

    private static final String STORE_NAME = "AREA_STORE";

    AreaStore(Context c) {
        if (storage == null){
            storage = c.getSharedPreferences(STORE_NAME, Context.MODE_PRIVATE);
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
    public static class AreaSorter implements Comparator<AreaModel> {

        double latitude,longitude;

        public AreaSorter(double latitude, double longitude) {
            this.latitude = latitude;
            this.longitude = longitude;
        }

        @Override
        public int compare(AreaModel o1, AreaModel o2) {
            double o1Dist = distFrom(o1.getLatitude(),o1.getLongitude(),latitude,longitude);
            o1.setDistFromLocation(o1Dist);
            double o2Dist = distFrom(o2.getLatitude(),o2.getLongitude(),latitude,longitude);
            o2.setDistFromLocation(o2Dist);

            return Double.compare(o1Dist,o2Dist);
        }
        public double distFrom(double lat1, double lng1, double lat2, double lng2) {
            double earthRadius = 6371000; //meters
            double dLat = Math.toRadians(lat2-lat1);
            double dLng = Math.toRadians(lng2-lng1);
            double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
                    Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                            Math.sin(dLng/2) * Math.sin(dLng/2);
            double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
            double dist = (float) (earthRadius * c);

            return dist;
        }
    }
}
