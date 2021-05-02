package org.riderun.app.provider.ride.mock;

import org.riderun.app.model.City;
import org.riderun.app.model.Count;
import org.riderun.app.model.Park;
import org.riderun.app.model.Ride;
import org.riderun.app.provider.ride.RidesProvider;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

public class RidesMockedProvider implements RidesProvider {
    private final static RidesMockedProvider instance = new RidesMockedProvider();

    private final List<Ride> RIDES = new ArrayList<>(100);

    private RidesMockedProvider() {
        // TODO Replace this City and Park by using a city from CityMock / ParkMock
        //City city = new City("Brühl", 1, 25873, "DE", 50.833333, 6.9);
        Park park = new Park("Phantasialand", 4872, 25873, 50.833333, 6.9);

        // TODO Replace this by a new RideMock, similar to ParkMock and CityMock
        RIDES.add(new Ride("Taron", park, 12723));
        RIDES.add(new Ride("Raik", park, 13689));
        RIDES.add(new Ride("F.L.Y.", park, 15201));
        RIDES.add(new Ride("Crazy Bats", park, 980));
        RIDES.add(new Ride("Colorado Adventure", park, 978));
        RIDES.add(new Ride("Black Mamba", park, 3117));
        // The Winja's have only one ID on RCDB, but can be counted separately on Coaster Count
        RIDES.add(new Ride("Winja's Fear", park, 1235));
        RIDES.add(new Ride("Winja's Force", park, 1235));
    }

    public static RidesMockedProvider instance() {
        return instance;
    }

    public List<Ride> rides() {
        return RIDES;
    }
}
