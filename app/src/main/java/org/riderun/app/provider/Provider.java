package org.riderun.app.provider;

public interface Provider {
    /**
     * Returns the id of the provider. This ID must be unique between all Providers. And it MUST
     * NOT be changed, as it is used as a namespace in Maps and the database.
     * be wisemay never change
     * @return
     */
    public String id();
    public String name();
    public ProviderType type();
}
