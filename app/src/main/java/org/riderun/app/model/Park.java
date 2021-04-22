package org.riderun.app.model;

public class Park {
    private final String name;
    private final int rcdbId;
    private final City city;
    private GeoCoordinate geoCoordinate;
    // description
    // image
    // location / how to find
    // Adress

    public Park(String name, int rcdbId, City city, double longitude, double latitude) {
        this.name = name;
        this.rcdbId = rcdbId;
        this.city = city;
        this.geoCoordinate = new GeoCoordinate(latitude, longitude, GeoPrecision.Park);
    }

    public Park(String name, int rcdbId, City city) {
        this.name = name;
        this.rcdbId = rcdbId;
        this.city = city;
        this.geoCoordinate = GeoCoordinate.empty();
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

    public GeoCoordinate getGeoCoordinate() {
        return geoCoordinate.isEmpty() ? city.getGeoCoordinate() : geoCoordinate;
    }
}
