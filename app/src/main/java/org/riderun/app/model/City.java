package org.riderun.app.model;

public class City {
    private final String name;
    private final int cityId;
    private final String country2letter;
    private final int rcdbId;
    private final double longitude;
    private final double latitude;

    public City(String name, int cityId, int rcdbId, String country2letter, double latitude, double longitude) {
        this.name = name;
        this.cityId = cityId;
        this.rcdbId = rcdbId;
        this.country2letter = country2letter;
        this.longitude = longitude;
        this.latitude = latitude;
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

    public double getLongitude() {
        return longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public String getCountry2letter() {
        return country2letter;
    }
}
