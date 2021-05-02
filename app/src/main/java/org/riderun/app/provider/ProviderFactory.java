package org.riderun.app.provider;

import org.riderun.app.provider.city.CityProvider;
import org.riderun.app.provider.city.mock.CityMockProvider;
import org.riderun.app.provider.city.rcdb.CityRCDBProvider;
import org.riderun.app.provider.count.CountProvider;
import org.riderun.app.provider.count.db.RcdbCountProvider;
import org.riderun.app.provider.park.ParksProvider;
import org.riderun.app.provider.park.mock.ParksMockProvider;
import org.riderun.app.provider.park.rcdb.ParksRCDBProvider;
import org.riderun.app.provider.ride.RidesProvider;
import org.riderun.app.provider.ride.mock.RidesMockedProvider;

public class ProviderFactory {
    private final static boolean MOCK = false;

    public static ParksProvider parksProvider() {
        return MOCK ? ParksMockProvider.instance() :ParksRCDBProvider.instance();
    }

    public static RidesProvider ridesProvider() {
        return MOCK ? RidesMockedProvider.instance() : RidesMockedProvider.instance(); // TODO Real provider
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
