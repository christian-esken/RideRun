package org.riderun.app.ui.rides;

import org.riderun.app.model.Park;
import org.riderun.app.model.Ride;
import org.riderun.app.storage.MockedData;
import org.riderun.app.storage.mock.ParkMock;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class RidesViewModel extends ViewModel {

    private MutableLiveData<RidesData> data = new MutableLiveData<>();
    Park park;
    List<Ride> rides;

    public RidesViewModel() {
        ParkMock parks = ParkMock.instance();
        park = parks.parks().get(0);
        //park = MockedData.parks().get(0);
        rides = MockedData.rides();
        RidesData rdata = new RidesData(park, rides);
        data.setValue(rdata);
    }

    /**
     * Informs that fields in the park or rides has changed
     */
    public void notifyFieldModification() {
        data.postValue(new RidesData(park, rides));
    }

    /**
     * Informs that fields in the park or rides has changed
     */
    public void setNewPark(Park park) {
        this.park = park;
        data.postValue(new RidesData(park, rides));
    }


    public LiveData<RidesData> ridesData() {
        return data;
    }
}