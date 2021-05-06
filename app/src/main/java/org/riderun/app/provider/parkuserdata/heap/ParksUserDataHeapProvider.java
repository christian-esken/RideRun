package org.riderun.app.provider.parkuserdata.heap;

import org.riderun.app.model.ParkUserData;
import org.riderun.app.provider.ProviderType;
import org.riderun.app.provider.parkuserdata.ParksUserDataProvider;

import java.util.HashMap;
import java.util.Map;

import static org.riderun.app.provider.ProviderType.UserNotes;

public class ParksUserDataHeapProvider implements ParksUserDataProvider {
    private static ParksUserDataProvider instance;
    // Map: rcdbId -> ParkUserData
    Map<Integer, ParkUserData> userData = new HashMap<>();

    public static synchronized ParksUserDataProvider instance() {
        if (instance == null) {
            instance = new ParksUserDataHeapProvider();
        }
        return instance;
    }

    @Override
    public ParkUserData byRcdbId(int rcdbId) {
        return userData.computeIfAbsent(rcdbId, (x) -> new ParkUserData(rcdbId));
    }

    @Override
    public String id() {
        return "heap";
    }

    @Override
    public String name() {
        return "User Data about a location, park or attraction - Not yet persistet";
    }

    @Override
    public ProviderType type() {
        return UserNotes;
    }
}
