package org.riderun.app.model;

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
}
