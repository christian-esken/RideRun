package org.riderun.app.ui.parks;

import org.riderun.app.model.GeoCoordinate;

public class ParksFilterCriteria {
    // Preselection
    final ParksPreselection preselection;
    // Data from all the preselection filter. We store all of them, so the data

    // Preselection by Nearby: GeoCoordinate (e.g. map center). By default it is the users current
    // position.
    final GeoCoordinate geoCoordinate;
    // Preselection by Location: Should contain continent, country, city (all optional)
    // ...
    // Preselection by Tour: Should contain tour name
    // ...

    // Filters
    final String nameFilter;
    // Maximum number of parks to show
    final int limit;

    // This constructor will get very many parameters. Possibly change to Builder pattern
    public ParksFilterCriteria(ParksPreselection preselection, String nameFilter, GeoCoordinate geoCoordinate, int limit) {
        this.preselection = preselection;
        this.nameFilter = nameFilter;
        this.geoCoordinate = geoCoordinate;
        this.limit = limit;
    }
}
