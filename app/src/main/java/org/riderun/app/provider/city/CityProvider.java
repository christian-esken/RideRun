package org.riderun.app.provider.city;

import org.riderun.app.model.City;

import java.util.List;

public interface CityProvider {
    // Returns all cities konw to this Provider
    List<City> cities();

    /**
     * Returns the city for the given cityId
     * @param cityId The cityId
     * @return The matching city. null if no such city exists
     */
    public City byCityId(int cityId);
}
