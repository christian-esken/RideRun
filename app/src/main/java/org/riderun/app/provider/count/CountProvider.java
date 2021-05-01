package org.riderun.app.provider.count;

import org.riderun.app.model.Count;
import org.riderun.app.provider.Provider;

import java.util.List;

public interface CountProvider extends Provider {
    List<Count> all();
}
