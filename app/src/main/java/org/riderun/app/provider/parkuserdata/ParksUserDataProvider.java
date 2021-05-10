package org.riderun.app.provider.parkuserdata;

import org.riderun.app.model.SiteUserData;
import org.riderun.app.provider.Provider;

public interface ParksUserDataProvider extends Provider {
    SiteUserData byRcdbId(int rcdbId);
}