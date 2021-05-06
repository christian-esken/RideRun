package org.riderun.app.provider;

public enum ProviderType {
    // Visits of an Attraction. Taking a guided tour, Outside visit, roller coaster ride
    Count, // TODO Rename to Visit?
    // An attraction at a Location, e.g. "Guided Tour" or "Taron" (roller coaster name)
    Attraction,
    /** A physical location, typically located in a city , e.g. "Tower Bridger", or "Phantasialand".
     *
     * Note: There are attractions that are outside of cities, span multiple cities or regions.
     * Examples include "Chinese Wall" and "Limes (Roman Empire)" (Length: 550 km). The latter can
     * be attached to a Country instead of a City. These attractions could also be assigned to
     *  a new ProviderType like "Area" that contains a Geo Rect, Circle, or Polygon.
     */
    Location,
    /** A city. It is recommend to have as few City providers as possible, as matching
     * cities between different providers would be complex. */
    City,
    /**
     * A country, based on ISO-3166 country codes. There should be a single country provider
     * that provides all countries. Please do not introduce a new CountryProvider without good
     * reason. One reason could be if the data set contains old countries that
     * exist not any longer. Or if it is the Mars planet, or a fictional world.
     *
     * At the moment the CountryProvider is implemented also in CityRCDBProvider, but
     * is highly recommended to extract that to a separate class with a hardcoded list of all
     * countries, similar to Continent.
     */
    Country,
    /**
     * A continent. This provider is hardcoded in the Continent class.
     */
    Continent
}
