package org.riderun.app.model;

public class Ride implements Attraction {
    private final String name;
    private final int rcdbRideId;
    private final int rcdbParkId;
    private final int typeId;
    private final String typeName;
    private final int aufbauId;
    private final String aufbauName;
    private final int statusId;
    private final String statusName;
    private final String openedOp;
    private final String openedDate;

    public Ride(String name, int rcdbRideId, int rcdbParkId, int typeId, String typeName, int aufbauId, String aufbauName, int statusId, String statusName, String openedOp, String openedDate) {
        this.name = name;
        this.rcdbRideId = rcdbRideId;
        this.rcdbParkId = rcdbParkId;
        this.typeId = typeId;
        this.typeName = typeName;
        this.aufbauId = aufbauId;
        this.aufbauName = aufbauName;
        this.statusId = statusId;
        this.statusName = statusName;
        this.openedOp = openedOp;
        this.openedDate = openedDate;
    }

    public Ride(String name, int rcdbRideId, int rcdbParkId) {
        this(name, rcdbRideId, rcdbParkId, 1, "Steel", 105, "Flying", 93, "Operating", "", "2021");
    }
    // description
    // image
    // location / how to find

    // --- Attraction descriptor ---
    @Override
    public String attractionId() {
        return "ride";
    }

    @Override
    public String attractionTitle() {
        return "Roller Coaster Ride";
    }

    // --- getters --------------------------

    public String name() {
        return name;
    }

    public int rcdbId() {
        return rcdbRideId;
    }

    public int rcdbParkId() {
        return rcdbParkId;
    }

    public int typeId() {
        return typeId;
    }

    public String typeName() {
        return typeName;
    }

    public int aufbauId() {
        return aufbauId;
    }

    public String aufbauName() {
        return aufbauName;
    }

    public int statusId() {
        return statusId;
    }

    public String statusName() {
        return statusName;
    }

    public String openedOp() {
        return openedOp;
    }

    public String openedDate() {
        return openedDate;
    }
}
