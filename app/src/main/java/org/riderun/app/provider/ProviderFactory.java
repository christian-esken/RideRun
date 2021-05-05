package org.riderun.app.provider;

import org.riderun.app.provider.city.CityProvider;
import org.riderun.app.provider.city.mock.CityMockProvider;
import org.riderun.app.provider.city.rcdb.CityRCDBProvider;
import org.riderun.app.provider.count.CountProvider;
import org.riderun.app.provider.count.db.RcdbCountProvider;
import org.riderun.app.provider.country.CountryProvider;
import org.riderun.app.provider.park.ParksProvider;
import org.riderun.app.provider.park.mock.ParksMockProvider;
import org.riderun.app.provider.park.rcdb.ParksRCDBProvider;
import org.riderun.app.provider.ride.RidesProvider;
import org.riderun.app.provider.ride.mock.RidesMockedProvider;
import org.riderun.app.provider.ride.rcdb.RidesRCDBProvider;


/**
 * This factory is able to produce providers for the different data SETS. Examples for data
 * SETS are RCDB (Roller Coaster Database), Coaster Count, Unesco World Heritage Sites, and so on.
 *
 * Each of the providers may use a different set of sites to visit, cities, countries. Additionally
 * the primary keys of the providers can differ. Thus ony a matching set of providers can
 * cooperate, and this factory allows tp "wire" the correct providers.
 *
 * Note. At the moment we have just two provider SETS, "RCDB" and "MOCK". The choice between them
 * is static. This will change in the future, and will require to pass a "providerSet" argument
 * to the factory methods.
 *
 * When implementing aq new provider SET, it is HIGHLY recommended to try to use standard keys, e.g.
 * 2-letter ISO country codes. Doing so will make it much easier in the future to use multiple
 * providers at the same time, e.g. to show all POI's in Finland.
 */
public class ProviderFactory {
    private final static boolean MOCK = false;

    public static ParksProvider parksProvider() {
        return MOCK ? ParksMockProvider.instance() : ParksRCDBProvider.instance();
    }

    public static RidesProvider ridesProvider() {
        return MOCK ? RidesMockedProvider.instance() : RidesRCDBProvider.instance();
    }

    public static CountryProvider countryProvider() {
        return MOCK ? CityRCDBProvider.instance() : CityRCDBProvider.instance(); // TODO mock provider
    }

    public static CityProvider cityProvider() {
        return MOCK ? CityMockProvider.instance() : CityRCDBProvider.instance();
    }

    public static CountProvider countProvider() {
        return MOCK ? RcdbCountProvider.instance() : RcdbCountProvider.instance(); // TODO MOCK provider
    }

    /**
     * Returns whether the mock mode is active
     * @return true if the data is mocked
     */
    public static boolean useMockData() { return MOCK; }
}
