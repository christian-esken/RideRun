package org.riderun.app.ui.rides;

import org.riderun.app.model.Park;
import org.riderun.app.model.Ride;

import java.util.List;

public class RidesData {
    // The park field may change to a more generic "Location" type to allow counting of other
    // location types. For example "Unesco World Heritage Sites" can also have multiple
    // "attractions" (e.g. outside visit, inside visit, tower, guided tour), but their data model
    // would be different (my be in multiple cities, does not have an rcdb Id.
    final Park park;
    final List<Ride> rides;

    public RidesData(Park park, List<Ride> rides) {
        this.park = park;
        this.rides = rides;
    }
}
