package org.riderun.app.model;

import org.riderun.app.storage.Order;

import java.util.Comparator;
import java.util.Objects;

/**
 * A city has a name, has a Geo location, is based in a country amd has an internal ID.
 * The identity of a city is the Internal ID, which means updates to the Geo Coordinate, name or any other field
 * is irrelevant. {@link #equals(Object)} and {@link #hashCode()} reflect this behaviour.
 */
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

    /**
     * Create a new city without geo position
     * @param name
     * @param cityId
     * @param rcdbId
     * @param country2letter
     */
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        City city = (City) o;
        return cityId == city.cityId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(cityId);
    }

    public static Comparator<City> orderByName(Order order) {
        return new Comparator<City>() {
            @Override
            public int compare(City a, City b) {
                int result = a.getName().compareTo(b.getName());
                return order == Order.ASC ? result : -result;
            }
        };
    }

    @Override
    public String toString() {
        // The output is explicitly tailored to the needs of the the
        // city Spinner in the ParksFragment. If we need diffenent toString()
        // methods in the future, then City can be subclasssed, overrinding toString().
        return name;
    }
}
