package org.riderun.app.model;

import java.time.Instant;
import java.util.Iterator;
import java.util.SortedSet;
import java.util.TimeZone;
import java.util.TreeSet;

/**
 *  The count for one Ride. In other words, the user-specific data for a ride.
 *
 *  At the moment
 *  we keep the dynamic data in a different class (not In Ride). Reasons:
 *  1. Make it easier to send type specific update notifications. 2. Count is much more likely to
 *  change than Ride. 3. Count is user specific data, while Ride is master data. 4. User data
 *  like Count must be secured / exportable, while the other data can be reimported w/o harm.
 * <br/>
 *  Please note that there is no back reference from Count to Ride. It is expected to work out, as
 *  we never pass around Count instances w/o Ride.
 */
public class Count {
    private final SortedSet<CountEntry> counts = new TreeSet<>();

    /**
     * Creates a new Object, with default values: No counts, no comment, not liked, and unmodified
     */
    public Count() {
    }

    public boolean isEmpty() {
        return counts.isEmpty();
    }

    /**
     * Returns the count date (first ride). Only the date portion is returned (w/o time)
     * @return The date of the first ride, null if not ridden yet
     */
    public CountEntry getFirstEntry() {
        return counts.isEmpty()  ? null : counts.first();
    }

    /**
     * Returns the last/newest count count date (last ride).
     * @return The date of the first ride, null if not ridden yet
     */
    public CountEntry getLastEntry() {
        return counts.isEmpty()  ? null : counts.last();
    }

    public Iterator<CountEntry> iterator() {
        return counts.iterator();
    }

    /**
     * Adds a count. using the current time and the users default time zone
     */
    public void addCountNow(String comment) {
        counts.add(new CountEntry(Instant.now(), TimeZone.getDefault().getID(), comment));
    }

    public void addCount(Instant instant, String timezone, String comment) {
        counts.add(new CountEntry(instant, timezone, comment));
    }

    public void removeCount(CountEntry countEntry) {
        counts.remove(countEntry);
    }

}
