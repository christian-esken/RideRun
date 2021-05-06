package org.riderun.app.provider.parkuserdata;

import org.riderun.app.model.ParkUserData;
import org.riderun.app.provider.Provider;

public interface ParksUserDataProvider extends Provider {
    ParkUserData byRcdbId(int rcdbId);
}