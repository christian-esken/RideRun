package org.riderun.app.provider.park.mock;

import org.riderun.app.R;
import org.riderun.app.RideRunApplication;
import org.riderun.app.model.City;
import org.riderun.app.model.Park;
import org.riderun.app.provider.city.mock.CityMock;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class ParkMockReader {
    private static ParkMockReader instance = new ParkMockReader();


    private ArrayList<Park> PARKS = new ArrayList<>();

    public static ParkMockReader instance() {
        return instance;
    }

    // private. Use  ParkMock.instance() instead
    private ParkMockReader() {
        CityMock cityMock = CityMock.instance();
        InputStream is = RideRunApplication.getAppContext().getResources().openRawResource(R.raw.park_mock);
        BufferedReader br = new BufferedReader(new InputStreamReader(is, Charset.forName("utf-8")));
        try {
            while (br.ready()) {
                String line = br.readLine();
                // Note: The mock csv files are specially crafted: We do not need a CSV Parser.
                String[] fields = line.split(";");

                int cityId = Integer.parseInt(fields[2]);
                City city = cityMock.byCityId(cityId);

                PARKS.add(new Park(
                        fields[0],
                        Integer.parseInt(fields[1]),
                        city
                ));
            }
        } catch (Exception exc) {
            throw new RuntimeException("Error initializing ParkMock", exc);
        }
    }

    public List<Park> parks() {
        return PARKS;
    }

    // Test method: Return the first "count" Parks
    public List<Park> parks(int count) {
        return PARKS.subList(0, count);
    }

}
