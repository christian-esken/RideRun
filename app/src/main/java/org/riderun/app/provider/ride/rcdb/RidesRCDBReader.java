package org.riderun.app.provider.ride.rcdb;

import org.riderun.app.R;
import org.riderun.app.RideRunApplication;
import org.riderun.app.model.Ride;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class RidesRCDBReader {
    private static RidesRCDBReader instance = new RidesRCDBReader();

    private ArrayList<Ride> RIDES = new ArrayList<>();

    public static RidesRCDBReader instance() {
        return instance;
    }

    // private. Use  ParkMock.instance() instead
    private RidesRCDBReader() {
        InputStream is = RideRunApplication.getAppContext().getResources().openRawResource(R.raw.rcdb_rides_europe);
        BufferedReader br = new BufferedReader(new InputStreamReader(is, Charset.forName("utf-8")));
        try {
            while (br.ready()) {
                String line = br.readLine();
                // Note: The mock csv files are specially crafted: We do not need a CSV Parser.
                // 11062;"Smiler";4796;"Alton Towers";1;"Stahl";6;"Sitzend";93;"In Betrieb";"";"2013-05-31"
                String[] fields = line.split(";");

                int rcdbRideId = Integer.parseInt(fields[0]);
                String rideName = unescape(fields[1]);
                int rcdbParkId = Integer.parseInt(fields[2]);
                // parkName is only added for information purposes. Actually the park is imported via a different script
                String parkName = unescape(fields[3]);
                int typeId = Integer.parseInt(fields[4]);    // 1 = steel
                String typeName = unescape(fields[5]);       // "Stahl"
                int aufbauId = Integer.parseInt(fields[6]);  // 6 = seated
                String aufbauName = unescape(fields[7]);     // "Sitzend"
                int statusId = Integer.parseInt(fields[8]);  // 93 = operating
                String statusName = unescape(fields[9]);     // "In Betrieb"
                String openedOp = unescape(fields[10]);      // opened operator: "" , <= , ...
                String openedDate = unescape(fields[11]);    // opened date


                RIDES.add(new Ride(
                        rideName,
                        rcdbRideId,
                        rcdbParkId,
                        typeId,
                        typeName,
                        aufbauId,
                        aufbauName,
                        statusId,
                        statusName,
                        openedOp,
                        openedDate
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

    public List<Ride> allRides() {
        return RIDES;
    }
}
