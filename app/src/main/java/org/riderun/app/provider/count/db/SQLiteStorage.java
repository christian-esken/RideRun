package org.riderun.app.provider.count.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import org.jetbrains.annotations.NotNull;
import org.riderun.app.RideRunApplication;
import org.riderun.app.model.Count;
import org.riderun.app.model.CountEntry;
import org.riderun.app.model.SiteUserData;

import java.time.Instant;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import androidx.annotation.Nullable;

public class SQLiteStorage extends SQLiteOpenHelper {
    private static final String TAG = "SQLiteStorage";

    private static volatile SQLiteStorage instance;
    private static final int DB_VERSION = 2;
    private static final String DB_NAME = "riderun";

    // Standard column names
    public static final String C_ID = "id";      // Numeric ID
    //public static final String C_UUID = "uuid";  // UUID in String format "6fc6cb12-9031-4499-9578-d39d218d6ceb"
    public static final String C_PROVIDER = "provider"; // Provider ID String ("countdb", "rcdb")
    public static final String C_COMMENT = "comment";   // Free form user comment
    public static final String C_LAST_MODIFIED = "last_modified";   // Instant
    public static final String C_LIKED = "liked";   // boolean


    // counts
    private static final String T_COUNTS = "counts";
    private static final String C_VISITED_AT = "visited";
    private static final String C_VISITED_TIMEZONE = "tz";
    private static final String C_POI = "poi";

    // metadata
    private static final String T_METADATA = "metadata";

    // Sites User data
    private static final String T_SITES_UD = "sites_ud";
    // C_PROVIDER
    // C_POI
    // C_LAST_MODIFIED
    // C_LIKED

    // SQL templates
    public static final String SELECT_COUNTS_SQL = "SELECT "
            + C_PROVIDER
            + "," + C_POI
            + "," + C_VISITED_AT
            + "," + C_VISITED_TIMEZONE
            + "," + C_COMMENT
            + " FROM " + T_COUNTS;

    private static final String SELECT_SITES_UD = "SELECT "
            + C_PROVIDER
            + "," + C_POI
            + "," + C_LAST_MODIFIED
            + "," + C_LIKED
            + "," + C_COMMENT
            + " FROM " + T_SITES_UD;



    private SQLiteStorage(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        if (instance != null) {
            throw new IllegalStateException("SQLiteStorage already created. Call instance(Context ctx) instead.");
        }
        instance = this;
    }

    /**
     * Returns the static SQLiteStorage, creating it on the first call.
     * @return The static SQLiteStorage. @NotNull
     */
    public synchronized static SQLiteStorage build() {
        Context context = RideRunApplication.getAppContext();
        if (instance == null) {
            // Note: context is used for the paths to the DB. It should not matter which view
            // of the app calls the bulld() method first.
            instance = new SQLiteStorage(context);
            Log.i(TAG, "Created SQLiteStorage for context " + context);
        }
        Log.i(TAG, "Using SQLiteStorage in context " + context);
        return instance;
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String createCounts = "CREATE TABLE " +  T_COUNTS + " ("
                // The ID is supposed to not get exported or visible on a Web page. It is user
                // specific. And it is  a purely technical measure to have a unique key, that
                // can be updated. It may also change, e.g. on an "undo" operation the id will
                // change.
                + C_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                // The provider DB containing the POI ("rcdb", "unesco-whs"...)
                + C_PROVIDER + " TEXT, "
                // The id of the POI, as used by the provider. The ID may be numerical, an UUID, or any free from text
                + C_POI + " TEXT, "
                // We want to store the LocalDateTime of the visit (e.g. "Used the Ride at 2020-09-17 15:12 CEST".
                // A user travelling to the US should stell see this count as "2020-09-17 15:12 CEST". For storing the
                // datetime + timezone  we need two fields.
                // The date/time as Unix Time (seconds since Epoch). Note: SQLite does not have specific date/time types.
                + C_VISITED_AT + " INTEGER, "
                // The time zone of the visit. We need to store it, as users that go abroad may
                // be temporarily change te time zone. The counts should be shown with
                // the correct time
                + C_VISITED_TIMEZONE + " TEXT, "
                // A free comment, e.g. "Virtual tour" or "Without museum visit"
                + C_COMMENT + " TEXT "
                + ")";

        // Index for  getBy... Provider,Poi
        String indexCounts = "CREATE INDEX idx_provider_poi ON " + T_COUNTS + " (" + C_PROVIDER + "," + C_POI + ")";
        String createMeta  = "CREATE TABLE " +  T_METADATA + " (k TEXT PRIMARY KEY, value TEXT)";

        db.execSQL(createCounts);
        db.execSQL(indexCounts);
        db.execSQL(createMeta);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, final int oldVersion, final int newVersion) {
        int upgradeVersion = oldVersion;
        if (upgradeVersion == 1) {
            // upgrade to version 2
            String createSitesUserdata = "CREATE TABLE " +  T_SITES_UD + " ("
                    // The ID is supposed to not get exported or visible on a Web page. It is user
                    // specific. And it is  a purely technical measure to have a unique key, that
                    // can be updated. It may also change, e.g. on an "undo" operation the id will
                    // change.
                    + C_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    // The provider DB containing the POI ("rcdb", "unesco-whs"...)
                    + C_PROVIDER + " TEXT, "
                    // The id of the POI, as used by the provider. The ID may be numerical, an UUID, or any free from text
                    + C_POI + " TEXT, "
                    // Last modification (Instant)
                    + C_LAST_MODIFIED + " INTEGER, "
                    // Liked flag. For yes/no the value is 0/1.
                    + C_LIKED + " INTEGER, "
                    // A free comment, e.g. "A must see historic site". "Most beautiful theme park in Europe"
                    + C_COMMENT + " TEXT "
                    + ")";

            db.execSQL(createSitesUserdata);
            String indexSites = "CREATE INDEX idx_sites_ud ON " + T_SITES_UD + " (" + C_PROVIDER + "," + C_POI + ")";
            db.execSQL(indexSites);


            upgradeVersion = 2; // we are now at version 2
        }
    }

    // --- READ FROM DB --------------------------------

    public Map<PoiKey, Count> getAll() {
        return getCountsFromDbQuery(SELECT_COUNTS_SQL);
    }

    public Map<PoiKey, Count> getByPoiKey(PoiKey poiKey) {
        String sql = SELECT_COUNTS_SQL
                + " WHERE " + C_PROVIDER + " = '" + poiKey.provider + "' AND "
                + C_POI + " = '" + poiKey.poi + "'";

        return getCountsFromDbQuery(sql);
    }

    @NotNull
    private Map<PoiKey, Count> getCountsFromDbQuery(String sql) {
        try (SQLiteDatabase db = getReadableDatabase(); Cursor cursor = db.rawQuery(sql, null)) {
            Map<PoiKey, Count> countMap = new HashMap<>();
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                String provider = cursor.getString(0);
                String poi = cursor.getString(1);
                int visitedAtEpoch = cursor.getInt(2);
                String visitedTz = cursor.getString(3);
                String comment = cursor.getString(4);
                PoiKey pk = new PoiKey(provider, poi);
                countMap.computeIfAbsent(pk, foo -> new Count()).addCount(Instant.ofEpochSecond(visitedAtEpoch), visitedTz, comment);
                cursor.moveToNext();
            }
            cursor.close();
            return countMap;
        }
    }


    public Map<PoiKey, SiteUserData> getSitesUserdata() {
        return getSitesUserdatFromDbQuery(SELECT_SITES_UD);
    }

    public Map<PoiKey, SiteUserData> getSitesUserdataByPoiKey(PoiKey poiKey) {
        String sql = SELECT_COUNTS_SQL
                + " WHERE " + C_PROVIDER + " = '" + poiKey.provider + "' AND "
                + C_POI + " = '" + poiKey.poi + "'";
        return getSitesUserdatFromDbQuery(SELECT_SITES_UD);
    }

    @NotNull
    private Map<PoiKey, SiteUserData> getSitesUserdatFromDbQuery(String sql) {
        try (SQLiteDatabase db = getReadableDatabase(); Cursor cursor = db.rawQuery(sql, null)) {
            Map<PoiKey, SiteUserData> resultMap = new HashMap<>();
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                String provider = cursor.getString(0);
                String poi = cursor.getString(1);
                int lastModified = cursor.getInt(2);
                int liked = cursor.getInt(3);
                String comment = cursor.getString(4);
                PoiKey pk = new PoiKey(provider, poi);
                resultMap.computeIfAbsent(pk, foo ->
                        new SiteUserData(
                                provider,
                                poi,
                                liked == 1,
                                comment,
                                Instant.ofEpochSecond(lastModified)));
                cursor.moveToNext();
            }
            cursor.close();
            return resultMap;
        }
    }

    // --- WRITE TO DB --------------------------------

    /**
     * Writes  the counts fot the given POI. If counts for the POI existed, they will be replaced.
     * @param poiKey The POI
     * @param counts The (new) counts
     */
    public void replaceCount(PoiKey poiKey, Count counts) {
        try(SQLiteDatabase db = getWritableDatabase()) {
            // Remove old counts
            String sqlDeleteWhere = C_PROVIDER + " = ? AND " + C_POI + " = ? ";
            db.delete(T_COUNTS, sqlDeleteWhere, new String[]{poiKey.provider, poiKey.poi});

            // Add new counts
            Iterator<CountEntry> iterator = counts.iterator();
            while (iterator.hasNext()) {
                CountEntry ce = iterator.next();

                ContentValues cv = new ContentValues();
                cv.put(C_PROVIDER, poiKey.provider);
                cv.put(C_POI, poiKey.poi);
                cv.put(C_VISITED_AT, ce.visitedAsEpochSeconds());
                cv.put(C_VISITED_TIMEZONE, ce.visitedTimezoneString());
                cv.put(C_COMMENT, ce.comment());
                db.insert(T_COUNTS, null, cv);
            }
        }
    }

    /**
     * Writes  the siteUserData fot the given POI. If siteUserData for the POI existed, they will be replaced.
     * @param poiKey The POI
     * @param siteUserData The (new) siteUserData
     */
    public void replaceSiteUserdata(PoiKey poiKey, SiteUserData siteUserData) {
        try(SQLiteDatabase db = getWritableDatabase()) {
            String sqlWhere = C_PROVIDER + " = ? AND " + C_POI + " = ? ";
            ContentValues cv = new ContentValues();
            cv.put(C_PROVIDER, poiKey.provider);
            cv.put(C_POI, poiKey.poi);
            cv.put(C_LAST_MODIFIED, siteUserData.getLastModified().getEpochSecond());
            cv.put(C_LIKED, siteUserData.getLiked());
            cv.put(C_COMMENT, siteUserData.getComment());

            String[] whereArgs = new String[2];
            whereArgs[0] = poiKey.provider;
            whereArgs[1] = poiKey.poi;

            db.update(T_SITES_UD, cv, sqlWhere, whereArgs);

        }
    }
}
