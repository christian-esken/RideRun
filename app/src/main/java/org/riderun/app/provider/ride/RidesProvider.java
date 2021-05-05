package org.riderun.app.provider.ride;

import org.riderun.app.model.Ride;

import java.util.List;

public interface RidesProvider {
    public List<Ride> allRides();
    public List<Ride> ridesForPark(int parkId);
}
