package org.riderun.app.provider.park.rcdb;

import org.riderun.app.R;
import org.riderun.app.RideRunApplication;
import org.riderun.app.model.City;
import org.riderun.app.model.Park;
import org.riderun.app.provider.city.CityProvider;
import org.riderun.app.provider.city.mock.CityMockProvider;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ParkRCDBReader {
    private static ParkRCDBReader instance = new ParkRCDBReader();


    private ArrayList<Park> PARKS = new ArrayList<>();
    private Set<Integer> CITY_IDS = new HashSet<>();
    private Set<Integer> COUNTRY_IDS = new HashSet<>();
    private Map<Integer, Integer> CITY2COUNTRY = new HashMap<>();

    public static ParkRCDBReader instance() {
        return instance;
    }

    // private. Use  ParkMock.instance() instead
    private ParkRCDBReader() {
        InputStream is = RideRunApplication.getAppContext().getResources().openRawResource(R.raw.rcdb_parks_europe);
        BufferedReader br = new BufferedReader(new InputStreamReader(is, Charset.forName("utf-8")));
        try {
            while (br.ready()) {
                String line = br.readLine();
                // Note: The mock csv files are specially crafted: We do not need a CSV Parser.
                // 4796;"Alton Towers";"26178,26175,26161,26828";"In Betrieb";"";"1980-04-04";10
                String[] fields = line.split(";");

                int rcdbParkId = Integer.parseInt(fields[0]);
                String parkName = unescape(fields[1]);
                String locationIds = unescape(fields[2]);
                String operatingStatus = unescape(fields[3]);
                String openedOp = unescape(fields[4]);
                String openedDate = unescape(fields[5]);

                // We presume that the first ID in all the locations is the cityId, and the last one
                // is the countryId
                String[] locations = locationIds.split(",");
                int cityId = locations.length > 0 ? Integer.parseInt(locations[0]) : -1;
                int countryId = locations.length > 1 ? Integer.parseInt(locations[locations.length-1]) : -1;
                if (cityId != -1) {
                    CITY_IDS.add(cityId);
                    if (countryId != -1) {
                        COUNTRY_IDS.add(countryId);
                        CITY2COUNTRY.put(cityId, countryId);
                    }
                }
                PARKS.add(new Park(
                        parkName, // parkName
                        rcdbParkId,
                        cityId
                ));
            }
        } catch (Exception exc) {
            throw new RuntimeException("Error initializing ParkMock", exc);
        }
    }

    private String unescape(String field) {
        field = field.trim();
        int endTrim = field.endsWith("\"") ? 1 : 0;
        if (field.startsWith("\"")) {
            field =field.substring(1, field.length() - endTrim);
        }
        return field;
    }

    public List<Park> parks() {
        return PARKS;
    }

    // Test method: Return the first "count" Parks
    public List<Park> parks(int count) {
        return PARKS.subList(0, count);
    }

    // Remove this method. Possible fix is a clean data import of the actual location hierarchy
    // in CityRCDBReader. Then we don't need to guess any longer via the parks data.
    public boolean parkInCityExists(int cityId) {
        return CITY_IDS.contains(cityId);
    }

    // Remove this method. Possible fix is a clean data import of the actual location hierarchy
    // in CityRCDBReader. Then we don't need to guess any longer via the parks data.
    public boolean parkInCountryExists(int cityId) {
        return COUNTRY_IDS.contains(cityId);
    }

    /**
     * Returns the rcdb country id for the given rcdb city id
     * @param cityid
     * @return
     */
    public Integer city2countryId(Integer cityid) {
        if (cityid == null) {
            return null;
        }
        return CITY2COUNTRY.get(cityid);
    }

}
