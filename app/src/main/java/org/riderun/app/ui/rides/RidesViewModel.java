package org.riderun.app.ui.rides;

import org.riderun.app.model.Count;
import org.riderun.app.model.Park;
import org.riderun.app.model.Ride;
import org.riderun.app.provider.ProviderFactory;
import org.riderun.app.provider.count.CountProvider;
import org.riderun.app.provider.count.db.PoiKey;
import org.riderun.app.provider.count.db.RcdbCountProvider;
import org.riderun.app.provider.park.ParksProvider;
import org.riderun.app.provider.ride.RidesProvider;
import org.riderun.app.provider.ride.mock.RidesMockedProvider;
import org.riderun.app.provider.park.mock.ParkMockReader;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

/**
 * Business logic for the RideFragment.It mainly manages the data: It loads it from the providers,
 * maintains the internal data model (including LiveData) and writes changes back to the providers.
 */
public class RidesViewModel extends ViewModel {
    private final MutableLiveData<RidesData> data = new MutableLiveData<>();
    // The following fields are used to update
    CountProvider countProvider = ProviderFactory.countProvider();
    ParksProvider parks = ProviderFactory.parksProvider();
    RidesProvider ridesProvider = ProviderFactory.ridesProvider();
    List<Ride> rides;
    //Map<PoiKey, Count> counts;

    public RidesViewModel() {
        Park park = parks.all().get(0);
        rides = ridesProvider.rides();
        RidesData rdata = new RidesData(park, rides, reloadCounts());
        data.setValue(rdata);
    }


    /**
     * Informs that fields in the park or rides has changed
     */
    public void notifyFieldModification() {
        // Re-POST the existing value, presuming fields have changed
        RidesData existingValue = data.getValue();
        data.postValue(existingValue);
    }


    private Map<PoiKey, Count> reloadCounts() {
        Map<PoiKey, Count> counts = new HashMap<>();
        for (Ride ride : rides) {
            counts.putAll(countProvider.getByPoiKey(Integer.toString(ride.rcdbId())));
        }
        return counts;
    }

    /**
     * Informs that fields in the park or rides has changed
     */
    public void setNewPark(Park parkNew, List<Ride> ridesNew, Map<PoiKey, Count> countsNew) {
        data.postValue(new RidesData(parkNew, ridesNew, countsNew));
    }


    public LiveData<RidesData> ridesData() {
        return data;
    }

    public void  notifyCountChange(Ride ride, Count count) {
        // Persist the count, writing it to DB
        countProvider.replaceCount(Integer.toString(ride.rcdbId()), count);
        RidesData existingValue = data.getValue();
        // Post the changed data, loading count back from DB. This will be a bit inefficient,
        // as counts for ALL rides of this Model are reloaded. Usually this is only a dozend or less,
        // but if the user would be able to show all rides (10000 in the whole world), this would be
        // not so efficient. For now we don't do this premature optimization.
        data.postValue(new RidesData(existingValue.park, existingValue.rides, reloadCounts()));
    }
}