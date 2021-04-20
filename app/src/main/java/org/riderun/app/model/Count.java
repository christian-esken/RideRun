package org.riderun.app.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 *  The count for one Ride. In other words, the user-specific data for a ride. At the moment
 *  we keep the dynamic data in a different class (not In Ride). Reasons:
 *  1. Make it easier to send type specific update notifications. 2. Count is much more likely to
 *  change than Ride. 3. Count is user specific data, while Ride is master data. 4. User data
 *  like Count must be secured / exportable, while the other data can be reimported w/o harm.
 * <br/>
 *  Please note that there is no back reference from Count to Ride. It is expected to work out, as
 *  we never pass around Count instances w/o Ride.
 */
public class Count {
    private volatile LocalDateTime countDate;
    private final List<LocalDateTime> repeats = new ArrayList<>(0);

    public Count() {
        this(null);
    }

    public Count(LocalDateTime countDate) {
        this.countDate = countDate;
    }

    /**
     * Returns the count date (first ride). Only the date portion is returned (w/o time)
     * @return The date of the first ride, null if not ridden yet
     */
    public LocalDate getCountDate() {
        return countDate == null ? null : countDate.toLocalDate();
    }

    /**
     * Sets the date and time of the first ride. null to "uncount"/reset.
     * @param countDate count date, may be null
     */
    public void setCountDate(LocalDateTime countDate) {
        this.countDate = countDate;
    }

    /**
     * Returns the list of ride repeats
     * @return
     */
    public List<LocalDateTime> getRepeats() {
        return repeats;
    }
}
