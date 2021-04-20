package org.riderun.app.model;

public class Park {
    private final String name;
    private final int rcdbId;
    private final  City city;
    private final double longitude;
    private final double latitude;
    // description
    // image
    // location / how to find

    public Park(String name, int rcdbId, City city, double longitude, double latitude) {
        this.name = name;
        this.rcdbId = rcdbId;
        this.city = city;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public String getName() {
        return name;
    }

    public int getRcdbId() {
        return rcdbId;
    }

    public City getCity() {
        return city;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getLatitude() {
        return latitude;
    }
}
