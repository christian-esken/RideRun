package org.riderun.app.model;

/**
 * A Geo Coordinate in ... format. Will probably be WGS 84.
 */
public class GeoCoordinate {
    private final static GeoCoordinate EMPTY = new GeoCoordinate(-1000,-1000, GeoPrecision.None);
    private final double longitude;
    private final double latitude;
    private final GeoPrecision geoPrecision;

    /**
     * Creates a geo position with a certain precision, e.g. the city center, country center
     * or an exact position.
     *
     * @param latitude Latitude
     * @param longitude Longitude
     */
    public GeoCoordinate(double latitude, double longitude, GeoPrecision geoPrecision) {
        this.longitude = longitude;
        this.latitude = latitude;
        this.geoPrecision = geoPrecision;
    }

    /**
     * Creates an exact geo position, e.g. the map center, the map upper left boundary or the
     * users current position
     * @param latitude Latitude
     * @param longitude Longitude
     */
    public GeoCoordinate(double latitude, double longitude) {
        this(latitude, longitude, GeoPrecision.Exact);
    }


    public static GeoCoordinate empty() {
        return EMPTY;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public GeoPrecision getGeoPrecision() {
        return geoPrecision;
    }

    public boolean isEmpty() {
        return geoPrecision == GeoPrecision.None;
    }

    /**
     * Returns human readable geo coordinates. If the expectedPrecision does not match
     * the precision of this GeoCoordinate, this precision will be added to the String.
     * This has the purpose of showing to the user that it is NOT the GeoCoordinate of
     * the Park, but that of the City.
     *
     * @param expectedPrecision
     * @return human readable geo coordinates
     */
    public String toStringShort(GeoPrecision expectedPrecision) {
        if (isEmpty()) {
            return "";
        }
        String geo = "(" + longitude + "," + latitude;
        if (expectedPrecision != geoPrecision) {
            geo += " - " + geoPrecision;
        }
        return geo + ")";
    }

    /**
     * Returns the distance between this point and the other point, suitable for sorting by distance.
     * The returned value is bigger if the distance is bigger. It has no other meaning, e.g. you
     * MUST NOT assume that it is a true Geo Distance (neither Spherical Earth nor Ellipsoidal Earth
     * nor any other model). Note: The implementation currently uses "Taxicab geometry"/
     * "Manhattan distance" as seen in https://en.wikipedia.org/wiki/Taxicab_geometry . Callers
     * SHOULD NOT rely on this, though. It ia also discouraged to persist this value.
     *
     * <br>
     * Performance note: We may call this very often, e.g. O(Parks ^2), meaning
     *   10000 ^2  = 100_000_000. The method is marked final to make it easier for JIT to
     *   compile/inline the code).
     *
     * @param other the other coordinate
     * @return The distance. Never negative.
     */
    public final double sortingDistance(GeoCoordinate other) {
        // Hint: The usual flat-surface formula is sqrt(xdiff^2 + ydiff^2).
        //
        // For sorting purposes we do not need the sqrt, leading to xdiff^2 + ydiff^2 .
        // The formula below drops also the ^2, which makes it less "precise" but very fast.
        return Math.abs(latitude - other.latitude) + Math.abs(longitude - other.longitude);
    }
}
