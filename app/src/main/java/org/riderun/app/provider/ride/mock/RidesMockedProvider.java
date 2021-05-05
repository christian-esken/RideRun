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
import java.util.stream.Collectors;

public class RidesMockedProvider implements RidesProvider {
    private final static RidesMockedProvider instance = new RidesMockedProvider();

    private final List<Ride> RIDES = new ArrayList<>(100);

    private RidesMockedProvider() {
        int parkID = 4872; // Phantasialand

        // Phantasialand Rides as of year 2021
        RIDES.add(new Ride("Taron", parkID, 12723));
        RIDES.add(new Ride("Raik", parkID, 13689));
        RIDES.add(new Ride("F.L.Y.", parkID, 15201));
        RIDES.add(new Ride("Crazy Bats", parkID, 980));
        RIDES.add(new Ride("Colorado Adventure", parkID, 978));
        RIDES.add(new Ride("Black Mamba", parkID, 3117));
        // The Winja's have only one ID on RCDB, but can be counted separately on Coaster Count
        RIDES.add(new Ride("Winja's Fear", parkID, 1235));
        RIDES.add(new Ride("Winja's Force", parkID, 1235));
    }

    public static RidesMockedProvider instance() {
        return instance;
    }

    public List<Ride> allRides() {
        return RIDES;
    }

    @Override
    public List<Ride> ridesForPark(int parkId) {
        return RIDES.stream().filter(p -> p.rcdbParkId() == parkId).collect(Collectors.toList());
    }

}
