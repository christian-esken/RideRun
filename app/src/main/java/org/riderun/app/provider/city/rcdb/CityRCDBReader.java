package org.riderun.app.provider.city.rcdb;

import android.util.Log;

import org.riderun.app.R;
import org.riderun.app.RideRunApplication;
import org.riderun.app.model.City;
import org.riderun.app.model.Continent;
import org.riderun.app.model.Country;
import org.riderun.app.provider.park.rcdb.ParkRCDBReader;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class CityRCDBReader {
    private static final String TAG = "CityRCDBReader";

    private static CityRCDBReader instance = new CityRCDBReader();


    private ArrayList<City> CITIES = new ArrayList<>();
    private ArrayList<Country> COUNTRIES = new ArrayList<>();

    public static CityRCDBReader instance() {
        return instance;
    }

    // private. Use  ParkMock.instance() instead
    private CityRCDBReader() {
        // geoihelper is a hack for guessing if a given csv row is a city or country
        ParkRCDBReader geohelper = ParkRCDBReader.instance();

        InputStream is = RideRunApplication.getAppContext().getResources().openRawResource(R.raw.rcdb_locations_europe);
        BufferedReader br = new BufferedReader(new InputStreamReader(is, Charset.forName("utf-8")));
        ArrayList<CityReadHelper> citiesWork = new ArrayList<>();
        try {
            while (br.ready()) {
                String line = br.readLine();
                // Note: The mock csv files are specially crafted: We do not need a CSV Parser.
                // 62012;"Brest"
                String[] fields = line.split(";");

                int locationId = Integer.parseInt(fields[0]);
                String locationName = unescape(fields[1]);
                if (fields.length >= 3) {
                    String countryCode = unescape(fields[2]);
                    final Continent continent;
                    if (fields.length < 4) {
                        Log.w(TAG, "Imported country without continent. Assigning default: " + fields[2]);
                        continent = Continent.ZZ;
                    } else {
                        String continentCode = unescape(fields[3]);
                        continent = Continent.continentByCode(continentCode);
                    }

                    COUNTRIES.add(new Country(continent, locationName, locationId, countryCode));
                } else {
                    citiesWork.add(new CityReadHelper(locationName, locationId));
                }
            }

            for (CityReadHelper crh : citiesWork) {
                Integer countryId = geohelper.city2countryId(crh.locationId);
                if (countryId != null) {
                    // city is known, and has a country assigned
                    Country country = findCountry(countryId);
                    if (country != null) {
                        // We know that country ... yay!
                        CITIES.add(new City(
                            crh.locationName,
                            crh.locationId, // for now: our Id = rcdbId
                            crh.locationId, // for now: our Id = rcdbId
                            country.cc2Letter));
                    }
                }
            }

        } catch (Exception exc) {
            throw new RuntimeException("Error initializing ParkMock", exc);
        }
    }

    private Country findCountry(int countryId) {
        for (Country country : COUNTRIES) {
            if (country.locationId == countryId) {
                return country;
            }
        }
        return null;
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

    public List<Country> countries() {
        return COUNTRIES;
    }

    private class CityReadHelper {
        final String locationName;
        final int locationId;

        private CityReadHelper(String locationName, int locationId) {
            this.locationName = locationName;
            this.locationId = locationId;
        }
    }
}
