package org.riderun.app.provider.park;

import org.riderun.app.model.Park;
import org.riderun.app.provider.Provider;

import java.util.List;

public interface ParksProvider extends Provider {
    List<Park> all();
    List<Park> byName(String name, int count);
    List<Park> closeby(double longitude, double latitude, int count);
}
