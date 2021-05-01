package org.riderun.app.model;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Objects;
import java.util.TimeZone;

public class CountEntry implements Comparable<CountEntry> {
    final Instant instant;
    final String timezone;

    public CountEntry(Instant instant, String timezone) {
        this.instant = instant;
        this.timezone = timezone;
    }

    public String formatAsDate() {
        DateTimeFormatter dtf = DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT);
        LocalDateTime ldt = LocalDateTime.ofInstant(instant, ZoneId.of(timezone));
        return dtf.format(ldt) + " "  + timezone;
     }

    public String formatAsDateTime() {
        DateTimeFormatter dtf = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT);
        LocalDateTime ldt = LocalDateTime.ofInstant(instant, ZoneId.of(timezone));

        String defaultTzId = TimeZone.getDefault().getID();
        if (defaultTzId.equals(timezone)) {
            return dtf.format(ldt);
        } else {
            // Add the Timezone if it differs from the default time zone
            return dtf.format(ldt) + " " + timezone;
        }

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CountEntry that = (CountEntry) o;
        return Objects.equals(instant, that.instant) &&
                Objects.equals(timezone, that.timezone);
    }

    @Override
    public int hashCode() {
        return Objects.hash(instant, timezone);
    }

    @Override
    public int compareTo(CountEntry o) {
        return instant.compareTo(o.instant);
    }

    public long visitedAsEpochSeconds() {
        return instant.getEpochSecond();
    }

    public String visitedTimezoneString() {
        return timezone;
    }
}
