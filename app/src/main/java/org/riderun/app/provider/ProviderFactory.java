package org.riderun.app.provider;

import java.util.HashMap;
import java.util.Map;


/**
 * This factory is able to produce providers for the different data SETS. Examples for data
 * SETS are RCDB (Roller Coaster Database), Coaster Count, Unesco World Heritage Sites, and so on.
 *
 * Each of the providers may use a different set of sites to visit, cities, countries. Additionally
 * the primary keys of the providers can differ. Thus ony a matching set of providers can
 * cooperate, and this factory allows tp "wire" the correct providers.
 *
 * Note. At the moment we have just two provider SETS, "RCDB" and "MOCK". The choice between them
 * is static. This will change in the future, and will require to pass a "providerSet" argument
 * to the factory methods.
 *
 * When implementing aq new provider SET, it is HIGHLY recommended to try to use standard keys, e.g.
 * 2-letter ISO country codes. Doing so will make it much easier in the future to use multiple
 * providers at the same time, e.g. to show all POI's in Finland.
 */
public class ProviderFactory {
    private final static boolean USE_MOCK = false;
    private final static Map<Bundle, ProviderBundle> providerBundles = new HashMap<>();

    public enum Bundle {RCDB, MOCK};

    static {
        providerBundles.put(Bundle.MOCK, new MockProviderBundle());
        providerBundles.put(Bundle.RCDB, new RcdbProviderBundle());
    }

    public static ProviderBundle get(Bundle bundle) {
        return USE_MOCK ? providerBundles.get(Bundle.MOCK) : providerBundles.get(bundle);
    }

}
