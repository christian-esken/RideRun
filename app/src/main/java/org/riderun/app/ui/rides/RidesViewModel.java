package org.riderun.app.ui.rides;

import org.riderun.app.model.Park;
import org.riderun.app.model.Ride;
import org.riderun.app.provider.ride.mock.RidesMockedProvider;
import org.riderun.app.provider.park.mock.ParkMockReader;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

/**
 * Business logic for the RideFragment.
 */
public class RidesViewModel extends ViewModel {
    private final MutableLiveData<RidesData> data = new MutableLiveData<>();
    List<Ride> rides;

    public RidesViewModel() {
        ParkMockReader parks = ParkMockReader.instance();
        Park park = parks.parks().get(0);
        rides = RidesMockedProvider.instance().rides();
        RidesData rdata = new RidesData(park, rides);
        data.setValue(rdata);
    }

    /**
     * Informs that fields in the park or rides has changed
     */
    public void notifyFieldModification() {
        //data.postValue(new RidesData(park, rides));
        RidesData existingValue = data.getValue();
        data.postValue(existingValue); // Re-POST the existing value (presuming fields have changed)
        //data.postValue(new RidesData(park, rides));
    }

    /**
     * Informs that fields in the park or rides has changed
     */
    public void setNewPark(Park park) {
        // TODO
        data.postValue(new RidesData(park, rides));
    }


    public LiveData<RidesData> ridesData() {
        return data;
    }
}