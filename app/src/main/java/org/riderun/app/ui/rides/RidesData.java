package org.riderun.app.ui.rides;

import org.riderun.app.model.Count;
import org.riderun.app.model.Park;
import org.riderun.app.model.Ride;
import org.riderun.app.provider.count.db.PoiKey;

import java.util.List;
import java.util.Map;

public class RidesData {
    // The park field may change to a more generic "Location" type to allow counting of other
    // location types. For example "Unesco World Heritage Sites" can also have multiple
    // "attractions" (e.g. outside visit, inside visit, tower, guided tour), but their data model
    // would be different (my be in multiple cities, does not have an rcdb Id.
    final Park park;
    final List<Ride> rides;
    final Map<PoiKey, Count> counts;

    public RidesData(Park park, List<Ride> rides, Map<PoiKey, Count> counts) {
        this.park = park;
        this.rides = rides;
        this.counts = counts;
    }

    /**
     * Returns the Count for the given key.
     * If we allow multi-provider for Count, we ne need to rethink how we handle key clashes.
     * An obvious way would be to bring in namespacing with the namespace being the count provider.
     * A fixed mapping will be sufficient for a long time:
     * - Rollercoasters use the "rr#rcdb" namespace-
     * - Unesco World Heritage Sites use the "rr#uwhs" namespace.
     *
     * @param intKey
     * @return
     */
    final Count countByKey(Integer intKey) {
        String keyString = Integer.toString(intKey);
        for (Map.Entry<PoiKey, Count> entry : counts.entrySet()) {
            if (entry.getKey().poi.equals(keyString)) {
                // Warning: On multi-provider (rcdb, uwhs) this
                return entry.getValue();
            }
        }

        return null;
    }

}
