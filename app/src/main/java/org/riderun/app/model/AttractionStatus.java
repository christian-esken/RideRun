package org.riderun.app.model;

public enum AttractionStatus {
    Operarting(93), SBNO(311), CLosed();

    private final int rcdbStatus;

    AttractionStatus(int rcdbStatus) {
        this.rcdbStatus = rcdbStatus;
    }

    AttractionStatus() {
        this(-1);
    }
}
