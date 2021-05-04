package org.riderun.app.provider.city.rcdb;

import org.riderun.app.model.City;
import org.riderun.app.model.Continent;
import org.riderun.app.model.Country;
import org.riderun.app.provider.city.CityProvider;
import org.riderun.app.provider.country.CountryProvider;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;

public class CityRCDBProvider implements CityProvider, CountryProvider {
    private static final City UNKNOWN_CITY = new City("Unknown city", 0 , 0, "--");

    private final static CityRCDBProvider instance = new CityRCDBProvider();
    private final CityRCDBReader cityRCDBReader;

    private CityRCDBProvider() {
        this.cityRCDBReader = CityRCDBReader.instance();
    }

    public static CityRCDBProvider instance() {
        return instance;
    }

    @Override
    public List<City> cities() {
        return cityRCDBReader.cities();
    }

    public City byCityId(int cityId, boolean assignUnknownCityIfNotFound) {
        List<City> CITIES = cityRCDBReader.cities();
        for (City city : CITIES) {
            if (city.getCityId() == cityId) {
                return city;
            }
        }
        return assignUnknownCityIfNotFound ? UNKNOWN_CITY : null;
    }

    @Override
    public List<City> byCountryCode(String cc2letter) {
        List<City> CITIES = cityRCDBReader.cities();
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
        List<City> CITIES = cityRCDBReader.cities();
        List<City> cities = new ArrayList<>(CITIES.size() / 4);
        for (City city : CITIES) {
            if (cityName.equals(city.getName())) {
                cities.add(city);
            }
        }
        return cities;
    }

    @Override
    public List<Country> allCountries() {
        return cityRCDBReader.countries();
    }

    @Override
    public Country countryBy2letterCC(String cc) {
        List<Country> countries = cityRCDBReader.countries();
        for (Country country : countries) {
            if (country.cc2Letter.equals(cc)) {
                return country;
            }
        }

        return null;
    }

    @Override
    public List<Continent> allContinents() {
        return Continent.allContinents();
    }
}
