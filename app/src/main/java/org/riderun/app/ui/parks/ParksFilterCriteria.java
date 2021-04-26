package org.riderun.app.ui.parks;

import org.riderun.app.model.GeoCoordinate;

import java.util.Objects;

public class ParksFilterCriteria {
    // Preselection
    final ParksPreselection preselection;
    // Data from all the preselection filter. We store all of them, so the data

    // Preselection by Nearby: GeoCoordinate (e.g. map center). By default it is the users current
    // position.
    final GeoCoordinate geoCoordinate;
    // Preselection by Location: continent, country, city (all optional)
    final String locationContinent;
    final String locationCountryCode2letter;
    final Integer locationCityId;

    // ...
    // Preselection by Tour: tour name
    final String tourName;

    // Filters
    final String nameFilter;
    // Maximum number of parks to show
    final int limit;

    // This constructor will get very many parameters. Possibly change to Builder pattern
    private ParksFilterCriteria(ParksPreselection preselection,
                               GeoCoordinate geoCoordinate,
                               String locationContinent,
                               String locationCountryCode2letter,
                               Integer locationCityId,
                               String tourName,
                               String nameFilter,
                               int limit) {
        this.preselection = preselection;
        this.geoCoordinate = geoCoordinate;
        this.nameFilter = nameFilter;
        this.locationContinent = locationContinent;
        this.locationCountryCode2letter = locationCountryCode2letter;
        this.locationCityId = locationCityId;
        this.tourName = tourName;
        this.limit = limit;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ParksFilterCriteria that = (ParksFilterCriteria) o;
        return limit == that.limit &&
                preselection == that.preselection &&
                Objects.equals(geoCoordinate, that.geoCoordinate) &&
                Objects.equals(locationContinent, that.locationContinent) &&
                Objects.equals(locationCountryCode2letter, that.locationCountryCode2letter) &&
                Objects.equals(locationCityId, that.locationCityId) &&
                Objects.equals(tourName, that.tourName) &&
                Objects.equals(nameFilter, that.nameFilter);
    }

    @Override
    public int hashCode() {
        return Objects.hash(preselection, geoCoordinate, locationContinent, locationCountryCode2letter, locationCityId, tourName, nameFilter, limit);
    }

    public static class Builder {
        ParksPreselection preselection = ParksPreselection.All;

        GeoCoordinate geoCoordinate = GeoCoordinate.empty();
        String locationContinent;
        String locationCountryCode2letter;
        Integer locationCityId;
        String tourName;

        String nameFilter = "";
        int limit = 50;


        /**
         * Creates a builder with default values
         * @return the Builder
         */
        public static Builder builder() {
            return new Builder();
        }

        /**
         * Creates a builder from an existing ParksFilterCriteria.
         * @param source for copying
         * @return the Builder
         */
        public static Builder builder(ParksFilterCriteria source) {
            Builder builder = new Builder();
            builder.preselection = source.preselection;
            builder.geoCoordinate = source.geoCoordinate;
            builder.nameFilter = source.nameFilter;
            builder.locationContinent = source.locationContinent;
            builder.locationCountryCode2letter = source.locationCountryCode2letter;
            builder.locationCityId = source.locationCityId;
            builder.tourName = source.tourName;
            builder.limit = source.limit;

            return builder;
        }

        public ParksFilterCriteria build() {
            return new ParksFilterCriteria(preselection,
                    geoCoordinate,
                    locationContinent,
                    locationCountryCode2letter,
                    locationCityId,
                    tourName,
                    nameFilter,
                    limit
                    );
        }

        public Builder preselection(ParksPreselection preselection) {
            this.preselection = preselection;
            return this;
        }

        public Builder geoCoordinate(GeoCoordinate geoCoordinate) {
            this.geoCoordinate = geoCoordinate;
            return this;
        }

        public Builder locationContinent(String locationContinent) {
            this.locationContinent = locationContinent;
            return this;
        }

        public Builder locationCountryCode2letter(String locationCountryCode2letter) {
            this.locationCountryCode2letter = locationCountryCode2letter;
            return this;
        }

        public Builder locationCityId(Integer locationCityId) {
            this.locationCityId = locationCityId;
            return this;
        }

        public Builder tourName(String tourName) {
            this.tourName = tourName;
            return this;
        }

        public Builder nameFilter(String nameFilter) {
            this.nameFilter = nameFilter;
            return this;
        }

        public Builder limit(int limit) {
            this.limit = limit;
            return this;
        }
    }
}
