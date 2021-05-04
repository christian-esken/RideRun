package org.riderun.app.model;

import org.riderun.app.storage.Order;

import java.util.Comparator;

public class Country {
    public final Continent continent;
    // Country codes. See ISO, e.g. https://www.iso.org/obp/ui/#search
    public final String countryName;
    public final int locationId;
    public final String cc2Letter; // ISO-3166

    public Country(Continent continent, String countryName, int locationId, String cc2Letter) {
        this.continent = continent;
        this.countryName = countryName;
        this.locationId = locationId;
        this.cc2Letter = cc2Letter;
    }

    public static Comparator<Country> orderByCountryName(Order order) {
        return new Comparator<Country>() {
            @Override
            public int compare(Country a, Country b) {
                int result = a.countryName.compareTo(b.countryName);
                return order == Order.ASC ? result : -result;
            }
        };
    }

    @Override
    public String toString() {
        // The output is explicitly tailored to the needs of the the
        // city Spinner in the ParksFragment. If we need diffenent toString()
        // methods in the future, then City can be subclasssed, overrinding toString().
        return countryName;
    }
}
