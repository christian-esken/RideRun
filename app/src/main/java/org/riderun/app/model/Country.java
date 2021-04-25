package org.riderun.app.model;

import org.riderun.app.storage.Order;

import java.util.Comparator;

public class Country {
    public final String continentName;
    public final String continentCode;
    public final String countryName;
    public final String cc2Letter;

    public Country(String continentName, String continentCode, String countryName, String cc2Letter) {
        this.continentName = continentName;
        this.continentCode = continentCode;
        this.countryName = countryName;
        this.cc2Letter = cc2Letter;
    }

/* not (yet) in use
    public static Comparator<Country> orderByCountryName(Order order) {
        return new Comparator<Country>() {
            @Override
            public int compare(Country a, Country b) {
                int result = a.countryName.compareTo(b.countryName);
                return order == Order.ASC ? result : -result;
            }
        };
    }
 */
}
