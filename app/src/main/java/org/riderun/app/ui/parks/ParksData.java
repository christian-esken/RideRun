package org.riderun.app.ui.parks;

import org.riderun.app.model.Park;

import java.util.List;

/**
 * Contains the dynamic data of the {@link ParksFragment}. It is usually passed around via the
 * {@link ParksViewModel}.
 *  Note: It may be possible to store this data directly in {@link ParksViewModel}. as the
 *  data will (likely) not be modified from outside.
 */
public class ParksData {
    final List<Park> parks;

    public ParksData(List<Park> parks) {
        this.parks = parks;
    }
}
