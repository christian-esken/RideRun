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
import java.util.List;

public class ParkRCDBReader {
    private static ParkRCDBReader instance = new ParkRCDBReader();


    private ArrayList<Park> PARKS = new ArrayList<>();

    public static ParkRCDBReader instance() {
        return instance;
    }

    // private. Use  ParkMock.instance() instead
    private ParkRCDBReader() {
        CityProvider cityProvider = CityMockProvider.instance();
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

                City city = cityProvider.byCityId(cityId, true);

                PARKS.add(new Park(
                        parkName, // parkName
                        rcdbParkId,
                        city
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

}
