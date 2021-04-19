package com.example.riderun.storage;

import com.example.riderun.model.City;
import com.example.riderun.model.Park;
import com.example.riderun.model.Ride;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class MockedData {
    private static final List<Ride> RIDES = new ArrayList<>(100);
    private static final List<Park> PARKS = new ArrayList<>(100);
    private static final List<City> CITIES = new ArrayList<>(100);

    static {
        City city = new City("Brühl", 25873, 50.833333, 6.9);
        CITIES.add(city);

        Park park = new Park("Phantasialand", 4872, city, 50.833333, 6.9);
        PARKS.add(park);

        LocalDate ld2020 = LocalDate.of(2020,9, 16);
        LocalDate ld2018 = LocalDate.of(2018,6, 5);

        RIDES.add(new Ride("Taron", park, 12723, ld2020));
        RIDES.add(new Ride("Raik", park, 13689, ld2020));
        RIDES.add(new Ride("F.L.Y.", park, 15201, null));
        RIDES.add(new Ride("Crazy Bats", park, 980, null));
        RIDES.add(new Ride("Colorado Adventure", park, 978, ld2018));
        RIDES.add(new Ride("Black Mamba", park, 3117, ld2018));
        // The Winja's have only one ID on RCDB, but can be counted separately on Coaster Count
        RIDES.add(new Ride("Winja's Fear", park, 1235, null));
        RIDES.add(new Ride("Winja's Force", park, 1235, ld2020));
    }


    public static List<Ride> rides() {
        return RIDES;
    }

    public static List<Park> parks() {
        return PARKS;
    }

    public static List<City> cities() {
        return CITIES;
    }

}
