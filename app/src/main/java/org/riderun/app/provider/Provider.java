package org.riderun.app.provider;

/**
 * Provider Interface that includes the provider metadata. All data providers that
 * needs to be persisted to the device database should implement this Provider interface, as
 * it makes eases persisting. All providers with the same ProviderType can be persisted centrally
 * to the same DB table(s), by "namespacing" them by putting name() in a "provider" column.
 */
public interface Provider {
    /**
     * Returns the id of the provider. This ID must be unique between all Providers of the same
     * ProviderType (as of type(). Once a provider has officially be added, its ID MUST
     * NOT be changed, as it is used as a namespace in Maps and the database.
     * Examples: "rcdb" or "uwhs"
     * @return The provider id / namespace
     */
    public String id();

    /**
     * Returns the readable provider name, e.g. "Roller Coaster Database" or "Unesco World Heritage Sites"
     * @return The provider name
     */
    public String name();

    /**
     * The provider type
     * @return The provider type
     */
    public ProviderType type();
}
