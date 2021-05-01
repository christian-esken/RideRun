package org.riderun.app.provider.continent;

import org.riderun.app.model.Continent;

import java.util.Arrays;
import java.util.List;

import static org.riderun.app.model.Continent.Africa;
import static org.riderun.app.model.Continent.America;
import static org.riderun.app.model.Continent.Asia;
import static org.riderun.app.model.Continent.Oceania;
import static org.riderun.app.model.Continent.Europe;
import static org.riderun.app.model.Continent.NorthAmerica;
import static org.riderun.app.model.Continent.SouthAmerica;

public class ContinentProvider {
    private final static List<Continent> CONTINENTS;
    private final static List<Continent> SUBCONTINENTS;

    static {
        CONTINENTS = Arrays.asList(Africa, America, Asia, Oceania, Europe);
        SUBCONTINENTS = Arrays.asList(Africa, Asia, Oceania, Europe, NorthAmerica, SouthAmerica);
    }

    /**
     * Returns the 5 main continents
     * @return the 5 main continents
     */
    static List<Continent> continents() {
        return CONTINENTS;
    }

    /**
     * Returns all continents, with America splitted into NorthAmerica and SouthAmerica
     * @return the continents
     */
    static List<Continent> subcontinents() {
        return SUBCONTINENTS;
    }
}
