package org.riderun.app.provider.config;

import org.riderun.app.model.GeoCoordinate;
import org.riderun.app.storage.Order;
import org.riderun.app.ui.parks.OrderBy;
import org.riderun.app.ui.parks.ParksPreselection;

public interface ConfigProvider {
    /**
     * Returns the  users preferred maximum number of parks to be shown in the park selector.
     * May depend on the phone (display size, speed) but also on the users preference.
     * Similar to the "LIMIT" clause in SQL.
     * @return park limit
     */
    int parkLimit();

    /**
     * Returns the users preferred preselection. Similiar to the "WHERE" clause in SQL.
     * @return the users preferred preselection
     */
    ParksPreselection parkPreselection();

    /**
     * Returns the users last geo search position. May be the users current location, or the
     * last postion in the map.
     *
     * Similiar to the "ORDER BY" clause in SQL.
     * @return the users preferred preselection
     */
    GeoCoordinate geoCoordinate();

    OrderBy orderBy();

    Order orderDirection();
}
