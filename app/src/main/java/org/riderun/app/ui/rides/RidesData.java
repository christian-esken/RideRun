package org.riderun.app.ui.rides;

import org.riderun.app.model.Park;
import org.riderun.app.model.Ride;

import java.util.List;

public class RidesData {
    final Park park;
    final List<Ride> rides;

    public RidesData(Park park, List<Ride> rides) {
        this.park = park;
        this.rides = rides;
    }
}
