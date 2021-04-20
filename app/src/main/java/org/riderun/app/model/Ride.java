package org.riderun.app.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Ride {
    private final String name;
    private final Park park;
    private final int rcdbId;
    private volatile Count count;
    // description
    // image
    // location / how to find

    public Ride(String name, Park park, int rcdbId) {
        // Not yet counted
        this(name, park, rcdbId, null);
    }

    public Ride(String name, Park park, int rcdbId, Count count) {
        this.name = name;
        this.park = park;
        this.rcdbId = rcdbId;
        this.count = count == null ? new Count() : count;
    }

    public String name() {
        return name;
    }

    public int rcdbId() {
        return rcdbId;
    }

    /**
     * Returns the count date (first ride). Only the date portion is returned (w/o time)
     * @return The date of the first ride, null if not ridden yet
     */
    public LocalDate getCountDate() {
        return count.getCountDate();
    }

    public void setCountDate(LocalDateTime countDateTime) {
        count.setCountDate(countDateTime);
    }
}
