package org.riderun.app.provider.park.rcdb;

import org.riderun.app.model.Park;
import org.riderun.app.provider.ProviderType;
import org.riderun.app.provider.park.ParksProvider;

import java.util.ArrayList;
import java.util.List;

public class ParksRCDBProvider implements ParksProvider {
    private final static ParksRCDBProvider instance = new ParksRCDBProvider();
    private final ParkRCDBReader parkMock;

    private ParksRCDBProvider() {
        this.parkMock = ParkRCDBReader.instance();
    }

    public static ParksRCDBProvider instance() {
        return instance;
    }

    @Override
    public List<Park> all() {
        return parkMock.parks();
    }

    @Override
    public List<Park> byName(String name, int count) {
        List<Park> matchingParks = new ArrayList<>(count);
        List<Park> parks = parkMock.parks();
        for (Park park : parks) {
            if (park.getName().toLowerCase().contains(name.toLowerCase())) {
                matchingParks.add(park);
            }
            if (matchingParks.size() >= count) {
                break; // limit reached
            }
        }

        return matchingParks;
    }

    @Override
    public List<Park> closeby(double longitude, double latitude, int count) {
        return null;
    }

    // --- Provider -------------
    @Override
    public String id() {
        return "rcdb";
    }

    @Override
    public String name() {
        return "Roller Coaster Database Parks";
    }

    @Override
    public ProviderType type() {
        return ProviderType.Location;
    }

}
