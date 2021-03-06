package org.riderun.app.provider.country;

import org.riderun.app.model.Continent;
import org.riderun.app.model.Country;

import java.util.List;

public interface CountryProvider {
    // Returns all countries known to this Provider
    List<Country> allCountries();

    /**
     * Returns the country for the given countryId
     * @param cc The 2 letter country code
     * @return The matching country. null if no such country exists
     */
    Country countryBy2letterCC(String cc);

    List<Continent> allContinents();
}
