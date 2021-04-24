package org.riderun.app.provider.city.mock;

import org.riderun.app.RideRunApplication;
import org.riderun.app.model.City;
import org.riderun.app.R;
import org.riderun.app.provider.city.CityProvider;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class CityMock implements CityProvider {
    private static CityMock instance = new CityMock();
    private static City UNKNOWN_CITY = new City("Unknown city", 0 , 0, "--");

    private ArrayList<City> CITIES = new ArrayList<>();

    public static CityMock instance() {
        return instance;
    }

    // private. Use  CityMock.instance() instead
    private CityMock() {
        InputStream is = RideRunApplication.getAppContext().getResources().openRawResource(R.raw.city_mock);
        BufferedReader br = new BufferedReader(new InputStreamReader(is, Charset.forName("utf-8")));
        try {
            while (br.ready()) {
                String line = br.readLine();
                // Note: The mock csv files are specially crafted: We do not need a CSV Parser.
                String[] fields = line.split(";");

                CITIES.add(new City(
                        fields[0],
                        Integer.parseInt(fields[1]),
                        Integer.parseInt(fields[2]),
                        fields[3],
                        Double.parseDouble(fields[4]),
                        Double.parseDouble(fields[5])
                        ));
            }
        } catch (Exception exc) {
            throw new RuntimeException("Error initializing CityMock", exc);
        }
    }

    public  List<City> cities() {
        return CITIES;
    }

    public City byCityId(int cityId) {
        for (City city : CITIES) {
            if (city.getCityId() == cityId) {
                return city;
            }
        }
        return UNKNOWN_CITY;
    }

}
