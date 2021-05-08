package org.riderun.app.provider.config;

import org.riderun.app.model.GeoCoordinate;
import org.riderun.app.storage.Order;
import org.riderun.app.ui.parks.OrderBy;
import org.riderun.app.ui.parks.ParksPreselection;

public class ConfigDefaultsProvider implements ConfigProvider {
    @Override
    public int parkLimit() {
        return 200;
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

    @Override
    public OrderBy orderBy() {
        return OrderBy.AttractionCount;
    }

    @Override
    public Order orderDirection() {
        return Order.DESC;
    }
}
