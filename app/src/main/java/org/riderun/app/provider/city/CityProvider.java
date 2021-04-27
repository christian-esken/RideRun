package org.riderun.app.provider.city;

import org.riderun.app.model.City;

import java.util.List;

import androidx.annotation.NonNull;

public interface CityProvider {
    // Returns all cities konw to this Provider
    List<City> cities();

    /**
     * Returns the city for the given cityId. If the city is not present a default value depending
     * on asssignUnknownCityIfNotFound is returned. If it is false, null wil be returned, otherwise
     * a dummy city w/o Geo coordinates and country will be returned.
     *
     * @param cityId The cityId
     * @param assignUnknownCityIfNotFound defines what to return if city is not found
     * @return The matching city
     */
    public City byCityId(int cityId, boolean assignUnknownCityIfNotFound);

    List<City> byCountryCode(String cc2letter);

    /**
     * Returns all cities with the exact city name
     * @param cityName city name
     * @return Matching cities. Empty if no city matches
     */
    List<City> byCityName(@NonNull String cityName);
}
