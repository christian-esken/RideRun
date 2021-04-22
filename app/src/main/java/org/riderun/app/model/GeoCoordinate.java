package org.riderun.app.model;

public class GeoCoordinate {
    private final static GeoCoordinate EMPTY = new GeoCoordinate(-1000,-1000, GeoPrecision.None);
    private final double longitude;
    private final double latitude;
    private final GeoPrecision geoPrecision;

    public GeoCoordinate(double latitude, double longitude, GeoPrecision geoPrecision) {
        this.longitude = longitude;
        this.latitude = latitude;
        this.geoPrecision = geoPrecision;
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
}
