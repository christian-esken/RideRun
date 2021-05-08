package org.riderun.app.ui.parks;

import org.riderun.app.model.GeoCoordinate;
import org.riderun.app.storage.Order;

import java.util.Objects;

public class ParksFilterCriteria {
    // A hint what field was modified
    final ModificationHint modificationHint;

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

    // WHERE: Filters
    final String parkNameFilter;

    // ORDER BY:
    final OrderBy orderBy;
    final Order orderDirection;

    // LIMIT: Maximum number of parks to show
    final int limit;

    // Filter type. Used as a "Modification hint"
    public enum ModificationHint {
        Unmodified,
        All, // If the config is new
        PreselType,
        PreselCriteriaContinent,
        PreselCriteriaCountry,
        PreselCriteriaCity,
        PreselGeoCoordinate,
        PreselCriteriaTourName,
        ParkName,
        OrderByOrLimit;

        /**
         * Returns if this modification hint contains the given one. this.contains(this) is always
         * true. And ALL.contains(non-null-value) is also always true.
         * <br> The use case for this is to check whehter an announced change (e.g. PreselType) has
         * an effect on a certain GUI element. For example, a Country change has an effect on the city
         * Spinner, which should now only show cities matching the new country.
         *
         * @param modificationHint the hint to compare
         * @return true if this is contained
         */
        boolean contains(ModificationHint modificationHint) {
            if (this == modificationHint) {
                return true;
            }
            if (modificationHint == null) {
                return false;
            }
            if (this == All) {
                return true;
            }
            // Two different criterias. Not contained in each other
            return false;
        }
    }

    // This constructor will get very many parameters. Possibly change to Builder pattern
    private ParksFilterCriteria(ModificationHint modificationHint,
                                ParksPreselection preselection,
                                GeoCoordinate geoCoordinate,
                                String locationContinent,
                                String locationCountryCode2letter,
                                Integer locationCityId,
                                String tourName,
                                String parkNameFilter,
                                OrderBy orderBy,
                                Order orderDirection,
                                int limit) {
        this.modificationHint = modificationHint;
        this.preselection = preselection;
        this.geoCoordinate = geoCoordinate;
        this.locationContinent = locationContinent;
        this.locationCountryCode2letter = locationCountryCode2letter;
        this.locationCityId = locationCityId;
        this.orderBy = orderBy;
        this.orderDirection = orderDirection;
        this.tourName = tourName;
        this.parkNameFilter = parkNameFilter;
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
                Objects.equals(orderBy, that.orderBy) &&
                Objects.equals(orderDirection, that.orderDirection) &&
                Objects.equals(tourName, that.tourName) &&
                Objects.equals(parkNameFilter, that.parkNameFilter);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                preselection,
                geoCoordinate,
                locationContinent,
                locationCountryCode2letter,
                locationCityId,
                orderBy,
                orderDirection,
                tourName,
                parkNameFilter, limit);
    }

    public static class Builder {
        ModificationHint modificationHint = ModificationHint.Unmodified;

        ParksPreselection preselection = ParksPreselection.Likes;

        GeoCoordinate geoCoordinate = GeoCoordinate.empty();
        String locationContinent;
        String locationCountryCode2letter;
        Integer locationCityId;
        String tourName;

        String nameFilter = "";

        // ORDER BY:
        OrderBy orderBy;
        Order orderDirection;

        // LIMIT
        int limit = 50;

        /**
         * Use {@link Builder#build()} instead
         */
        private Builder() {
        }

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
            builder.nameFilter = source.parkNameFilter;
            builder.locationContinent = source.locationContinent;
            builder.locationCountryCode2letter = source.locationCountryCode2letter;
            builder.locationCityId = source.locationCityId;
            builder.orderBy = source.orderBy;
            builder.orderDirection = source.orderDirection;
            builder.tourName = source.tourName;
            builder.limit = source.limit;

            return builder;
        }

        public ParksFilterCriteria build() {
            return new ParksFilterCriteria(
                    modificationHint,
                    preselection,
                    geoCoordinate,
                    locationContinent,
                    locationCountryCode2letter,
                    locationCityId,
                    tourName,
                    nameFilter,
                    orderBy,
                    orderDirection,
                    limit
                    );
        }

        public Builder preselection(ParksPreselection preselection) {
            this.preselection = preselection;
            this.modificationHint = ModificationHint.PreselType;
            return this;
        }

        public Builder geoCoordinate(GeoCoordinate geoCoordinate) {
            this.geoCoordinate = geoCoordinate;
            this.modificationHint = ModificationHint.PreselGeoCoordinate;
            return this;
        }

        public Builder locationContinent(String locationContinent) {
            this.locationContinent = locationContinent;
            this.modificationHint = ModificationHint.PreselCriteriaContinent;
            return this;
        }

        public Builder locationCountryCode2letter(String locationCountryCode2letter) {
            this.locationCountryCode2letter = locationCountryCode2letter;
            this.modificationHint = ModificationHint.PreselCriteriaCountry;
            return this;
        }

        public Builder locationCityId(Integer locationCityId) {
            this.locationCityId = locationCityId;
            this.modificationHint = ModificationHint.PreselCriteriaCity;
            return this;
        }

        public Builder tourName(String tourName) {
            this.tourName = tourName;
            this.modificationHint = ModificationHint.PreselCriteriaTourName;
            return this;
        }

        public Builder nameFilter(String nameFilter) {
            this.nameFilter = nameFilter;
            this.modificationHint = ModificationHint.ParkName;
            return this;
        }

        public Builder orderBy(OrderBy orderBy) {
            this.orderBy = orderBy;
            this.modificationHint = ModificationHint.OrderByOrLimit;
            return this;
        }

        public Builder orderDirection(Order orderDirection) {
            this.orderDirection = orderDirection;
            this.modificationHint = ModificationHint.OrderByOrLimit;
            return this;
        }

        /**
         * Call this to override automatic ModificationHint. There is no need to call
         * this after modifying just a single field of this Builder.
         * Typically one would call this with the "All" as argument.
         * @param modificationHint the hint
         * @return This Builder
         */
        public Builder modificationHint(ModificationHint modificationHint) {
            this.modificationHint = modificationHint;
            return this;
        }

        public Builder limit(int limit) {
            this.limit = limit;
            return this;
        }
    }
}
