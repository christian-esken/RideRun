package org.riderun.app.ui.rides;

import org.riderun.app.model.Park;
import org.riderun.app.model.Ride;
import org.riderun.app.storage.MockedData;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class RidesViewModel extends ViewModel {

    private MutableLiveData<RidesData> data = new MutableLiveData<>();
    Park park;
    List<Ride> rides;

    public RidesViewModel() {
        park = MockedData.parks().get(0);
        rides = MockedData.rides();
        RidesData rdata = new RidesData(park, rides);
        data.setValue(rdata);
    }

    public LiveData<RidesData> ridesData() {
        return data;
    }
}