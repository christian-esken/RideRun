package org.riderun.app.provider.ride;

import org.riderun.app.model.Ride;
import org.riderun.app.provider.Provider;

import java.util.List;

public interface RidesProvider extends Provider {
    public List<Ride> allRides();
    public List<Ride> ridesForPark(int parkId);
}
