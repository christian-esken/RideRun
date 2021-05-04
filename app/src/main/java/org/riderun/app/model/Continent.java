package org.riderun.app.model;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum Continent {
    AF("AF", "Africa", 1),
    AM("AM", "America", 2), // Short code self-invented
    AS("AS", "Asia", 3),
    EU("EU", "Europe", 4),
    OC("OC", "Oceania",5),
    AN("AN", "Antarctica", 6),
    // Subcontinents
    NA("NA", "North America", 10, 2),
    SA("SA", "South America", 11,2),
    AU("AU", "Australia", 12, 5),
    ZZ("ZZ", "Undefined", 0);

    // Continent code, as used in https://en.wikipedia.org/wiki/List_of_sovereign_states_and_dependent_territories_by_continent_%28data_file%29
    private final String code;
    // English name
    private final String name;
    private final int riderunId;
    private final int parent;

    Continent(String continentCode, String name, int riderunId) {
        this(continentCode, name, riderunId, 0);
    }

    Continent(String continentCode, String name, int riderunId, int parent) {
        this.code = continentCode;
        this.name = name;
        this.riderunId = riderunId;
        this.parent = parent;
    }

    public String code2letter() {
        return code;
    }

    public String continentName() {
        return name;
    }

    /**
     * Returns a continent
     * @param continentCode
     * @return
     */
    public static Continent continentByCode(String continentCode) {
        try {
            return Continent.valueOf(continentCode);
        } catch (IllegalArgumentException iae) {
            return ZZ;
        }
    }

    private static volatile List<Continent> continentListCached;
    public static List<Continent> allContinents() {
        if (continentListCached == null) {
            continentListCached = Arrays.asList(Continent.values());
        }
        return continentListCached;
    }


    public static Continent none() {
        return ZZ;
    }

    @Override
    public String toString() {
        // DO NOT CHANGE THE IMPLEMENTATION.
        // The return code is tailored to the Continent Spinner..
        // TODO This needs l10n
        return name;
    }
}
