package org.riderun.app.model;

public interface PrimaryKey<ID> {
    /**
     * Returns the attraction ID, e.g. the rcdbId 3812 , or a text string  "de-hh-miwula"
     * @return
     */
    ID pk();
}
