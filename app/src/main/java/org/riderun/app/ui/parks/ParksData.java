package org.riderun.app.ui.parks;

import org.riderun.app.model.GeoCoordinate;
import org.riderun.app.model.Park;

import java.util.List;

/**
 * Contains the dynamic data of the {@link ParksFragment}. It is usually passed around via the
 * {@link ParksViewModel}.
 */
public class ParksData {
    // Filters
    final String nameFilter;
    // current GeoCoordinate (e.g, users current position, or Map Center)
    final GeoCoordinate geoCoordinate;
    // Maximum number of parks to show
    final int limit;
    // The list of Parks, as a result of hte filters
    final List<Park> parks;

    public ParksData(String nameFilter, GeoCoordinate geoCoordinate, int limit, List<Park> parks) {
        this.nameFilter = nameFilter;
        this.geoCoordinate = geoCoordinate;
        this.limit = limit;
        this.parks = parks;
    }
}
