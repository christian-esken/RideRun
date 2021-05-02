package org.riderun.app.provider.city.rcdb;

import org.riderun.app.R;
import org.riderun.app.RideRunApplication;
import org.riderun.app.model.City;
import org.riderun.app.provider.park.rcdb.ParkRCDBReader;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class CityRCDBReader {
    private static CityRCDBReader instance = new CityRCDBReader();


    private ArrayList<City> CITIES = new ArrayList<>();

    public static CityRCDBReader instance() {
        return instance;
    }

    // private. Use  ParkMock.instance() instead
    private CityRCDBReader() {
        // geoihelper is a hack for guessing if a given csv row is a city or country
        ParkRCDBReader geohelper = ParkRCDBReader.instance();

        InputStream is = RideRunApplication.getAppContext().getResources().openRawResource(R.raw.rcdb_locations_europe);
        BufferedReader br = new BufferedReader(new InputStreamReader(is, Charset.forName("utf-8")));
        try {
            while (br.ready()) {
                String line = br.readLine();
                // Note: The mock csv files are specially crafted: We do not need a CSV Parser.
                // 62012;"Brest"
                String[] fields = line.split(";");

                int locationId = Integer.parseInt(fields[0]);
                String locationName = unescape(fields[1]);
                if (geohelper.parkInCityExists(locationId)) {
                    Integer countrId = geohelper.city2country(locationId);
                    CITIES.add(new City(
                            locationName, // parkName
                            locationId, // for now: our Id = rcdbId
                            locationId, // for now: our Id = rcdbId
                            countrId.toString() // TOOD For now: Use int country code instead of 2-letter CC
                            ));
                }
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

    public List<City> cities() {
        return CITIES;
    }

}
