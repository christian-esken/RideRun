package org.riderun.app.provider.parkuserdata.heap;

import org.riderun.app.model.SiteUserData;
import org.riderun.app.provider.ProviderType;
import org.riderun.app.provider.parkuserdata.ParksUserDataProvider;

import java.util.HashMap;
import java.util.Map;

import static org.riderun.app.provider.ProviderType.UserNotes;

public class ParksUserDataHeapProvider implements ParksUserDataProvider {
    private static ParksUserDataProvider instance;
    // Map: rcdbId -> ParkUserData
    Map<Integer, SiteUserData> userData = new HashMap<>();

    public static synchronized ParksUserDataProvider instance() {
        if (instance == null) {
            instance = new ParksUserDataHeapProvider();
        }
        return instance;
    }

    @Override
    public SiteUserData byRcdbId(final int rcdbId) {
        return userData.computeIfAbsent(rcdbId, (x) -> {
            if ((rcdbId == 4874) || (rcdbId == 4872) || (rcdbId == 4886) || (rcdbId == 4796) || rcdbId == 4855 || rcdbId == 18544 || rcdbId == 5205 || rcdbId == 5216) {
                // For a demo,. lets make some parks "favorite"
                return new SiteUserData("heap", Integer.toString(rcdbId), true, null, null);
            }
            return new SiteUserData("heap", Integer.toString(rcdbId));
        });
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
