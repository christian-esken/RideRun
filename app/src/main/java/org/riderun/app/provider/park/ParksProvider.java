package org.riderun.app.provider.park;

import org.riderun.app.model.Park;

import java.util.List;

public interface ParksProvider {
    List<Park> byName(String name, int count);
    List<Park> closeby(double longitude, double latitude, int count);
}