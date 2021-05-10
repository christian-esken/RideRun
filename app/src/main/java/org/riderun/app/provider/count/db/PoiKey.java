package org.riderun.app.provider.count.db;

import java.util.Objects;

/**
 * The DB model for a POI key. This basically is Comprised of a namespace (provider) and a key
 * in the namespace.
 */
public class PoiKey {
    public final String provider;
    public final String poi;

    public PoiKey(String provider, String poi) {
        this.provider = provider;
        this.poi = poi;
    }

    public PoiKey(String provider, int poiId) {
        this.provider = provider;
        this.poi = Integer.toString(poiId);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PoiKey poiKey = (PoiKey) o;
        return Objects.equals(provider, poiKey.provider) &&
                Objects.equals(poi, poiKey.poi);
    }

    @Override
    public int hashCode() {
        return Objects.hash(provider, poi);
    }
}
