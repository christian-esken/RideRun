package org.riderun.app.model;

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
