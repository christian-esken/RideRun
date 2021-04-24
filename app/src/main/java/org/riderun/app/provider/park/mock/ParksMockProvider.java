package org.riderun.app.provider.park.mock;

import org.riderun.app.model.Park;
import org.riderun.app.provider.park.ParksProvider;

import java.util.ArrayList;
import java.util.List;

public class ParksMockProvider implements ParksProvider {
    private final static ParksMockProvider instance = new ParksMockProvider();
    private final ParkMockReader parkMock;

    private ParksMockProvider() {
        this.parkMock = ParkMockReader.instance();
    }

    public static ParksMockProvider instance() {
        return instance;
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
}
