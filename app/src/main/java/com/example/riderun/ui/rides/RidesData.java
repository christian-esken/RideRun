package com.example.riderun.ui.rides;

import com.example.riderun.model.Park;
import com.example.riderun.model.Ride;

import java.util.List;

public class RidesData {
    final Park park;
    final List<Ride> rides;

    public RidesData(Park park, List<Ride> rides) {
        this.park = park;
        this.rides = rides;
    }
}
