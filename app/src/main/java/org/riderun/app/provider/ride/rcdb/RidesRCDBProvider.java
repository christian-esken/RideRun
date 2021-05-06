package org.riderun.app.provider.ride.rcdb;

import org.riderun.app.model.Ride;
import org.riderun.app.provider.ProviderType;
import org.riderun.app.provider.ride.RidesProvider;

import java.util.List;
import java.util.stream.Collectors;

public class RidesRCDBProvider implements RidesProvider {
    private final static RidesRCDBProvider instance = new RidesRCDBProvider();
    private final RidesRCDBReader ridesRcdbReader;

    private RidesRCDBProvider() {
        this.ridesRcdbReader = RidesRCDBReader.instance();
    }

    public static RidesRCDBProvider instance() {
        return instance;
    }

    @Override
    public List<Ride> allRides() {
        return ridesRcdbReader.allRides();
    }

    @Override
    public List<Ride> ridesForPark(int parkId) {
        return ridesRcdbReader.allRides().stream().filter(p -> p.rcdbParkId() == parkId).collect(Collectors.toList());
    }

    // --- Provider -------------
    @Override
    public String id() {
        return "rcdb";
    }

    @Override
    public String name() {
        return "Roller Coaster Database";
    }

    @Override
    public ProviderType type() {
        return ProviderType.Attraction;
    }
}
