package org.riderun.app.model;

public class City {
    private final String name;
    private final int rcdbId;
    private final double longitude;
    private final double latitude;

    public City(String name, int rcdbId, double latitude, double longitude) {
        this.name = name;
        this.rcdbId = rcdbId;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public String getName() {
        return name;
    }

    public int getRcdbId() {
        return rcdbId;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getLatitude() {
        return latitude;
    }
}
