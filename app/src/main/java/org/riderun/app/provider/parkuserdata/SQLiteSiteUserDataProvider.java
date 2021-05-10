package org.riderun.app.provider.parkuserdata;

import org.riderun.app.model.Count;
import org.riderun.app.model.SiteUserData;
import org.riderun.app.provider.ProviderType;
import org.riderun.app.provider.count.CountProvider;
import org.riderun.app.provider.count.db.PoiKey;
import org.riderun.app.provider.count.db.SQLiteStorage;

import java.util.Map;

/**
 * A provider storing user data on sites in a SQLite DB.
 */
public class SQLiteSiteUserDataProvider implements ParksUserDataProvider {
    private static SQLiteSiteUserDataProvider instance;

    private final SQLiteStorage sqLiteStorage;

    private SQLiteSiteUserDataProvider() {
        this.sqLiteStorage = SQLiteStorage.build();
    }

    public static synchronized SQLiteSiteUserDataProvider instance() {
        if (instance == null) {
            instance = new SQLiteSiteUserDataProvider();
        }
        return instance;
    }


    @Override
    public String id() {
        // rr = RideRun ,  PUD=Park User Notes , rcdb=keys from Rollercoaster database
        return "rrPUNrcdb";
    }

    @Override
    public String name() {
        return "RideRun Park User Notes";
    }

    @Override
    public ProviderType type() {
        return ProviderType.UserNotes;
    }


    @Override
    public SiteUserData byRcdbId(int key) {
        PoiKey poiKey = new PoiKey(id(), key);
        // TODO continue here. The getSitesUserdataByPoiKey() mehtod should return exactly one result. Is this sure?!?
        return sqLiteStorage.getSitesUserdataByPoiKey(poiKey).get(key);
    }
}
