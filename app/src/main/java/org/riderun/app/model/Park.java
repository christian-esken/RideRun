package org.riderun.app.model;

public class Park {
    private final String name;
    private final int rcdbId;
    private final Integer cityId;
    private GeoCoordinate geoCoordinate;
    // description
    // image
    // location / how to find
    // Adress

    public Park(String name, int rcdbId, Integer cityId, double longitude, double latitude) {
        this.name = name;
        this.rcdbId = rcdbId;
        this.cityId = cityId;
        this.geoCoordinate = new GeoCoordinate(latitude, longitude, GeoPrecision.Park);
    }

    public Park(String name, int rcdbId, Integer cityId) {
        this.name = name;
        this.rcdbId = rcdbId;
        this.cityId = cityId;
        this.geoCoordinate = GeoCoordinate.empty();
    }

    public String getName() {
        return name;
    }

    public int getRcdbId() {
        return rcdbId;
    }

    public Integer getCityId() {
        return cityId;
    }

    public GeoCoordinate getGeoCoordinate() {
        return GeoCoordinate.empty();
        // TODO Move getGeoCoordinate to a helper method that goes through the geo hierarchy.
        //  -> geoCoordinate.isEmpty() ? city.getGeoCoordinate() : geoCoordinate;
    }
}
