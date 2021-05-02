package org.riderun.app.model;

public interface Attraction {
    /**
     * Returns the attraction ID, e.g. "ride" or "uwhs"
     * @return
     */
    String attractionId();

    /**
     * User readable attraction name, e.g. "Roller Coaster" or "Unesco World Heritage Site"
     * @return
     */
    String attractionTitle();
}
