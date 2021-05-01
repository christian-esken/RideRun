package org.riderun.app.model.journal;

import java.time.Instant;

/**
 * Represents a
 */
public class JournalEntry {
    String provider;
    JournalAction action;
    // The journal entry is always related to a point in time. It does not matter in which time zone
    // the action took place.
    Instant time;

    String newValue;
    String oldValue; // old value. May be suitable to allow "undo"
}
