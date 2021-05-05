package org.riderun.app.storage;

public enum Order {
    ASC, DESC;

    /**
     * Returns the opposite of the current order direction
     * @return the opposite order direction
     */
    public Order reverse() {
        return this == ASC ? DESC : ASC;
    }

    /**
     * Returns a "multiplier" suitable for sorting / Comparable implementations
     * @return
     */
    public int multiplier() {
        return this == ASC ? 1 : -1;
    }
}
