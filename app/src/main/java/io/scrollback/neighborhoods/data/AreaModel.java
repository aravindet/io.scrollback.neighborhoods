package io.scrollback.neighborhoods.data;


import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

import io.scrollback.library.Constants;

public class AreaModel {
    private String name;
    private String description;
    private double latitude;
    private double longitude;
    private String roomId;
    private Date selectTime;

    protected void init(String name, String description, double latitude, double longitude, String roomId, Date selectTime) {
        this.name = name;
        this.description = description;
        this.latitude = latitude;
        this.longitude = longitude;
        this.roomId = roomId;
        this.selectTime = selectTime;
    }

    public AreaModel(String name, String description, double latitude, double longitude, String roomId, Date selectTime) {
        init(name, description, latitude, longitude, roomId, selectTime);
    }

    public AreaModel(String name, String description, double latitude, double longitude, String roomId) {
        init(name, description, latitude, longitude, roomId, null);
    }

    public AreaModel(JSONObject json) {
        Date date = null;

        try {
            long t = json.getLong("selectTime");

            if (t != 0) {
                date = new Date(t);
            }
        } catch (JSONException e) {
            Log.d(Constants.TAG, e.getMessage());
        }

        try {
            init(
                json.getString("name"),
                json.getString("description"),
                json.getDouble("latitude"),
                json.getDouble("longitude"),
                json.getString("roomId"),
                date
            );
        } catch (JSONException e) {
            Log.d(Constants.TAG, e.getMessage());
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public void setSelectTime(Date date) {
        this.selectTime = date;
    }

    public void setSelectTime(long milli) {
        setSelectTime(new Date(milli));
    }

    public Date getSelectTime() {
        return this.selectTime;
    }

    public JSONObject toJSONObject() {
        JSONObject json = new JSONObject();

        try {
            json.put("name", name);
            json.put("description", description);
            json.put("latitude", latitude);
            json.put("longitude", longitude);
            json.put("roomId", roomId);
        } catch (JSONException e) {
            Log.d(Constants.TAG, e.getMessage());

            return null;
        }

        if (selectTime != null) {
            try {
                json.put("selectTime", selectTime.getTime());
            } catch (JSONException e) {
                Log.d(Constants.TAG, e.getMessage());
            }
        }

        return json;
    };
}
