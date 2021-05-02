package org.riderun.app.provider.count.db;

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
}
