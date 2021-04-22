package org.riderun.app.model;

public class City {
    private final String name;
    private final int cityId;
    private final String country2letter;
    private final int rcdbId;
    private GeoCoordinate geoCoordinate;

    public City(String name, int cityId, int rcdbId, String country2letter, double latitude, double longitude) {
        this.name = name;
        this.cityId = cityId;
        this.rcdbId = rcdbId;
        this.country2letter = country2letter;
        this.geoCoordinate = new GeoCoordinate(latitude, longitude, GeoPrecision.City);
    }

    public City(String name, int cityId, int rcdbId, String country2letter) {
        this.name = name;
        this.cityId = cityId;
        this.rcdbId = rcdbId;
        this.country2letter = country2letter;
        this.geoCoordinate = GeoCoordinate.empty();
    }


    public String getName() {
        return name;
    }

    public int getCityId() {
        return cityId;
    }

    public int getRcdbId() {
        return rcdbId;
    }

    public String getCountry2letter() {
        return country2letter;
    }

    public GeoCoordinate getGeoCoordinate() {
        return geoCoordinate;
    }
}
