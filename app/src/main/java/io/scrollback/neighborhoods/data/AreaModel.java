package io.scrollback.neighborhoods.data;


public class AreaModel {
    String name;
    double latitude;
    double longitude;
    String roomId;

    public AreaModel(String name, double latitude, double longitude, String roomId) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.roomId = roomId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
}
