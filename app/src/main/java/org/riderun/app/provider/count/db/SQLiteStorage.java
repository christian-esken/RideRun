package org.riderun.app.provider.count.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import org.riderun.app.RideRunApplication;
import org.riderun.app.model.Count;
import org.riderun.app.model.CountEntry;

import java.time.Instant;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import androidx.annotation.Nullable;

public class SQLiteStorage extends SQLiteOpenHelper {
    private static final String TAG = "SQLiteStorage";

    private static volatile SQLiteStorage instance;
    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "riderun";

    // Standard column names
    public static final String C_ID = "id";      // Numeric ID
    public static final String C_UUID = "uuid";  // UUID in String format "6fc6cb12-9031-4499-9578-d39d218d6ceb"
    public static final String C_PROVIDER = "provider"; // Provider ID String ("countdb", "rcdb")


    // counts
    private static final String T_COUNTS = "counts";
    private static final String C_VISITED_AT = "visited";
    private static final String C_VISITED_TIMEZONE = "tz";
    private static final String C_POI = "poi";

    // metadata
    private static final String T_METADATA = "metadata";


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
        String createIdeas = "CREATE TABLE " +  T_COUNTS + " ("
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
                + C_VISITED_TIMEZONE + " STRING "
                + ")";
        String createMeta  = "CREATE TABLE " +  T_METADATA + " (k TEXT PRIMARY KEY, value TEXT)";

        db.execSQL(createIdeas);
        db.execSQL(createMeta);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }


    public Map<PoiKey, Count> getAll() {
        String sql = "SELECT "
                + C_PROVIDER
                + ","+ C_POI
                + "," + C_VISITED_AT
                + "," + C_VISITED_TIMEZONE
                + " FROM " + T_COUNTS;

        try (SQLiteDatabase db = getReadableDatabase();  Cursor cursor = db.rawQuery(sql, null);) {
            Map<PoiKey, Count> countMap = new HashMap<>();
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                String provider = cursor.getString(0);
                String poi = cursor.getString(1);
                int visitedAtEpcoh = cursor.getInt(2);
                String visitedTz = cursor.getString(3);
                PoiKey pk = new PoiKey(provider, poi);
                countMap.computeIfAbsent(pk, foo -> new Count()).addCount(Instant.ofEpochSecond(visitedAtEpcoh), visitedTz);
                cursor.moveToNext();
            }
            cursor.close();
            return countMap;
        }
    }

    /**
     * Writes  the counts fot the given POI. If counts for the POI existed, they will be replaced.
     * @param poiKey The POI
     * @param counts The (new) counts
     */
    public void replaceCount(PoiKey poiKey, Count counts) {
        try(SQLiteDatabase db = getWritableDatabase();) {
            // Remove old counts
            String sqlDeleteWhere = C_PROVIDER + " = '?' AND " + C_POI + " = '?' ";
            int deletedRows = db.delete(T_COUNTS, sqlDeleteWhere, new String[]{poiKey.provider, poiKey.poi});

            // Add new counts
            Iterator<CountEntry> iterator = counts.iterator();
            while (iterator.hasNext()) {
                CountEntry ce = iterator.next();

                ContentValues cv = new ContentValues();
                cv.put(C_PROVIDER, poiKey.provider);
                cv.put(C_POI, poiKey.poi);
                cv.put(C_VISITED_AT, ce.visitedAsEpochSeconds());
                cv.put(C_VISITED_TIMEZONE, ce.visitedTimezoneString());
                long rowId = db.insert(T_COUNTS, null, cv);
            }
        }
    }
}
