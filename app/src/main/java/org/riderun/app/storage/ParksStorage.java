package org.riderun.app.storage;

import org.riderun.app.model.Park;

import java.util.List;

public interface ParksStorage {
    List<Park> byName(String name, int count);
    List<Park> closeby(double longitude, double latitude, int count);
}
