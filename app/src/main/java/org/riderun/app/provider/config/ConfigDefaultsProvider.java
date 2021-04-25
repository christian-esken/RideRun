package org.riderun.app.provider.config;

import org.riderun.app.model.GeoCoordinate;
import org.riderun.app.ui.parks.ParksPreselection;

public class ConfigDefaultsProvider implements ConfigProvider {
    @Override
    public int parkLimit() {
        return 50;
    }

    @Override
    public ParksPreselection parkPreselection() {
        return ParksPreselection.Location;
    }

    @Override
    public GeoCoordinate geoCoordinate() {
        // For now, lets choose a position in central Europe.
        return new GeoCoordinate(50, 0);
    }
}
