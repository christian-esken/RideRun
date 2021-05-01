package org.riderun.app.model.journal;

/**
 * Action type of a Journal Entry. It can relate a physical action ("Location visited") or a
 * technical action ()
 */
public enum JournalAction {
    /** Location marked as visited */
    Visited,
    /** Location marked as not visited (e.g. if it was accidentally marked as visited before) */
    Unvisited,
    /** Location added  to a tour */
    LocationAdded,
    /** Location removed from a tour */
    LocationRemoved,
    /** Everything else */
    Generic
}
