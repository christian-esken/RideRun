package org.riderun.app.storage;

import org.riderun.app.model.Park;
import org.riderun.app.storage.mock.ParkMock;

import java.util.ArrayList;
import java.util.List;

public class ParksMockStorage implements ParksStorage {
    private final static ParksMockStorage instance = new ParksMockStorage();
    private final ParkMock parkMock;

    private ParksMockStorage() {
        this.parkMock = ParkMock.instance();
    }

    public static ParksMockStorage instance() {
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
