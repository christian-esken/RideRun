package org.riderun.app.provider;

import org.riderun.app.provider.city.CityProvider;
import org.riderun.app.provider.city.rcdb.CityRCDBProvider;
import org.riderun.app.provider.count.CountProvider;
import org.riderun.app.provider.count.db.RcdbCountProvider;
import org.riderun.app.provider.country.CountryProvider;
import org.riderun.app.provider.park.ParksProvider;
import org.riderun.app.provider.park.rcdb.ParksRCDBProvider;
import org.riderun.app.provider.parkuserdata.ParksUserDataProvider;
import org.riderun.app.provider.parkuserdata.heap.ParksUserDataHeapProvider;
import org.riderun.app.provider.ride.RidesProvider;
import org.riderun.app.provider.ride.rcdb.RidesRCDBProvider;

public class RcdbProviderBundle implements ProviderBundle {
    @Override
    public ParksProvider siteProvider() {
        return ParksRCDBProvider.instance();
    }

    @Override
    public ParksUserDataProvider siteUserDataProvider() {
        return ParksUserDataHeapProvider.instance(); // TODO real/persisting provider
    }

    @Override
    public RidesProvider attractionProvider() {
        return RidesRCDBProvider.instance();
    }

    @Override
    public CountryProvider countryProvider() {
        return CityRCDBProvider.instance();
    }

    @Override
    public CityProvider cityProvider() {
        return CityRCDBProvider.instance();
    }

    @Override
    public CountProvider countProvider() {
        return RcdbCountProvider.instance();
    }

}
