package org.riderun.app.ui.parks;

import org.riderun.app.model.GeoCoordinate;
import org.riderun.app.model.Park;

import java.util.List;

/**
 * Contains the dynamic data of the {@link ParksFragment}. It is usually passed around via the
 * {@link ParksViewModel}.
 */
public class ParksData {
    final ParksFilterCriteria filterCriteria;
    // The list of Parks, as a result of hte filters
    final List<Park> parks;

    // This constructor will get very many parameters. Possibly change to Builder pattern
    public ParksData(ParksFilterCriteria filterCriteria, List<Park> parks) {
        this.filterCriteria = filterCriteria;
        this.parks = parks;
    }
}
