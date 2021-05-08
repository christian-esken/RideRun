package org.riderun.app.provider;

import org.riderun.app.provider.city.CityProvider;
import org.riderun.app.provider.city.mock.CityMockProvider;
import org.riderun.app.provider.city.rcdb.CityRCDBProvider;
import org.riderun.app.provider.count.CountProvider;
import org.riderun.app.provider.count.db.RcdbCountProvider;
import org.riderun.app.provider.country.CountryProvider;
import org.riderun.app.provider.park.ParksProvider;
import org.riderun.app.provider.park.mock.ParksMockProvider;
import org.riderun.app.provider.parkuserdata.ParksUserDataProvider;
import org.riderun.app.provider.parkuserdata.heap.ParksUserDataHeapProvider;
import org.riderun.app.provider.ride.RidesProvider;
import org.riderun.app.provider.ride.mock.RidesMockedProvider;

public class MockProviderBundle implements ProviderBundle {
    @Override
    public ParksProvider siteProvider() {
        return ParksMockProvider.instance();
    }

    @Override
    public ParksUserDataProvider siteUserDataProvider() {
        return ParksUserDataHeapProvider.instance();
    }

    @Override
    public RidesProvider attractionProvider() {
        return RidesMockedProvider.instance();
    }

    @Override
    public CountryProvider countryProvider() {
        return CityRCDBProvider.instance(); // TODO Add mock provider?
    }

    @Override
    public CityProvider cityProvider() {
        return CityMockProvider.instance();
    }

    @Override
    public CountProvider countProvider() {
        return RcdbCountProvider.instance(); // TODO MOCK provider
    }

}
