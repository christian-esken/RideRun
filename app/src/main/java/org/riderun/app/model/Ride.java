package org.riderun.app.model;

public class Ride implements Attraction {
    private final String name;
    private final Park park;
    private final int rcdbId;
    //private volatile Count count;
    // description
    // image
    // location / how to find

    public Ride(String name, Park park, int rcdbId) {
        this.name = name;
        this.park = park;
        this.rcdbId = rcdbId;
    }

    public String name() {
        return name;
    }

    public int rcdbId() {
        return rcdbId;
    }

    @Override
    public String attractionId() {
        return "ride";
    }

    @Override
    public String attractionTitle() {
        return "Roller Coaster Ride";
    }

    /**
     * Returns the count date (first ride). Only the date portion is returned (w/o time)
     * @return The date of the first ride, null if not ridden yet
     */
    /*
    public Count getCount() {
        return count;
    }
   */
}
