package org.riderun.app.model;

import java.time.LocalDate;

public class Ride {
    private final String name;
    private final Park park;
    private final int rcdbId;
    private volatile LocalDate count;
    // description
    // image
    // location / how to find

    public Ride(String name, Park park, int rcdbId) {
        // Not yet counted
        this(name, park, rcdbId, null);
    }

    public Ride(String name, Park park, int rcdbId, LocalDate countDate) {
        this.name = name;
        this.park = park;
        this.rcdbId = rcdbId;
        this.count = countDate;
    }

    public String name() {
        return name;
    }

    public int rcdbId() {
        return rcdbId;
    }

    public LocalDate getCountDate() {
        return count;
    }

    public void setCountDate(LocalDate count) {
        this.count = count;
    }
}
