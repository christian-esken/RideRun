package org.riderun.app.provider.count;

import org.riderun.app.model.Count;
import org.riderun.app.provider.Provider;
import org.riderun.app.provider.count.db.PoiKey;

import java.util.Map;

public interface CountProvider extends Provider {
    /**
     * Returns all Counts know to this Provider
     * @return all counts
     */
    Map<PoiKey, Count> getAll();

    /**
     * Returns all Counts matching the key within this provider name space. At the moment (2021-05)
     * this method will always return 0 or 1 results (not counted, or counted). Once the GUI supports
     * repeats, this method can return any number of results.
     *
     * @param key The key of the poi
     * @return all counts matching the rcdbId
     */
    Map<PoiKey, Count> getByPoiKey(String key);

    /**
     * Replaces ALL counts for the given PoiKey
     * @param key
     * @param counts
     */
    void replaceCount(String key, Count counts);
}
