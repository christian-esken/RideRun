package org.riderun.app.provider.city.mock;

import org.riderun.app.R;
import org.riderun.app.RideRunApplication;
import org.riderun.app.model.City;
import org.riderun.app.provider.city.CityProvider;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class CityMockProvider implements CityProvider {
    private static final CityMockProvider instance = new CityMockProvider();
    private static final City UNKNOWN_CITY = new City("Unknown city", 0 , 0, "--");

    private final ArrayList<City> CITIES = new ArrayList<>();

    public static CityMockProvider instance() {
        return instance;
    }

    // private. Use  CityMock.instance() instead
    private CityMockProvider() {
        InputStream is = RideRunApplication.getAppContext().getResources().openRawResource(R.raw.city_mock);
        BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
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

    @Override
    public  List<City> cities() {
        return CITIES;
    }

    @Override
    public City byCityId(int cityId, boolean assignUnknownCityIfNotFound) {
        for (City city : CITIES) {
            if (city.getCityId() == cityId) {
                return city;
            }
        }
        return assignUnknownCityIfNotFound ? UNKNOWN_CITY : null;
    }

    @Override
    public List<City> byCountryCode(String cc2letter) {
        List<City> cities = new ArrayList<>(CITIES.size() / 4);
        for (City city : CITIES) {
            if (cc2letter.equals(city.getCountry2letter())) {
                cities.add(city);
            }
        }
        return cities;
    }

    @Override
    public List<City> byCityName(String cityName) {
        List<City> cities = new ArrayList<>(CITIES.size() / 4);
        for (City city : CITIES) {
            if (cityName.equals(city.getName())) {
                cities.add(city);
            }
        }
        return cities;
    }

}
