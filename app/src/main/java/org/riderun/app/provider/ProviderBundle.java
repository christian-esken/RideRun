package org.riderun.app.provider;

import org.riderun.app.provider.city.CityProvider;
import org.riderun.app.provider.count.CountProvider;
import org.riderun.app.provider.country.CountryProvider;
import org.riderun.app.provider.park.ParksProvider;
import org.riderun.app.provider.parkuserdata.ParksUserDataProvider;
import org.riderun.app.provider.ride.RidesProvider;

/**
 * A ProviderBundle contains a compatible set of providers. Example 1: the "Roller Coaster" bundle
 * contains d location provider (theme parks), an Attraction provider (rollercoasters per park),
 * and a city provider (cities containing parks). Example 2: The "Unesco World Heritage Site"
 * bundle contains a  location provider (world heritage sites),  an Attraction provider (Castle visit,
 * Gulded Tour, ...) and a city provider (cities with world heritage sites). Example 3: The "mock"
 * provider works on artificial data. It  can be used fur unit or integration tests, or for
 * development tests. Mock providers can use in memory storage, making sure that the real
 * database is not modified/corrupted during tests.
 */
public interface ProviderBundle {
    ParksProvider siteProvider();
    RidesProvider attractionProvider();
    CountryProvider countryProvider();
    CityProvider cityProvider();
    CountProvider countProvider();
    ParksUserDataProvider siteUserDataProvider();
}
