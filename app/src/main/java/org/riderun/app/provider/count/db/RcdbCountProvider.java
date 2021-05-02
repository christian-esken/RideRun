package org.riderun.app.provider.count.db;

import org.riderun.app.model.Count;
import org.riderun.app.provider.ProviderType;
import org.riderun.app.provider.count.CountProvider;

import java.util.Map;

public class RcdbCountProvider implements CountProvider {
    private final SQLiteStorage sqLiteStorage;

    @Override
    public String id() {
        // rr = RideRun ,  #=count , rcdb=keys from Rollercoaster database
        return "rr#rcdb";
    }

    @Override
    public String name() {
        return "RideRun counts based on the RCDB";
    }

    @Override
    public ProviderType type() {
        return ProviderType.Count;
    }

    public RcdbCountProvider() {
        this.sqLiteStorage = SQLiteStorage.build();
    }

    @Override
    public Map<PoiKey, Count> getAll() {
        return sqLiteStorage.getAll();
    }

    @Override
    public Map<PoiKey, Count> getByPoiKey(String key) {
        return sqLiteStorage.getByPoiKey(new PoiKey(id(), key));
    }

    @Override
    public void replaceCount(String key, Count counts) {
        sqLiteStorage.replaceCount(new PoiKey(id(), key), counts);
    }
}
