package org.riderun.app.ui.parks;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import org.riderun.app.R;
import org.riderun.app.model.City;
import org.riderun.app.model.GeoPrecision;
import org.riderun.app.model.Park;
import org.riderun.app.storage.ParksMockStorage;
import org.riderun.app.storage.ParksStorage;

import java.util.List;

/**
 * The ParksFragment shows a list of parks, and allows to chose a park to be displayed in the
 * {@link org.riderun.app.ui.rides.RidesFragment}. The list shows the most relevant parks
 * according to the users selection criteria. Selection criteria can be distance, park name,
 * park type (water park, has real-coaster, has alpinecoaster).
 */
public class ParksFragment extends Fragment {

    ParksStorage parksStorage;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        parksStorage = ParksMockStorage.instance();
        ParksViewModel parksViewModel = new ViewModelProvider(this).get(ParksViewModel.class);
        View root = inflater.inflate(R.layout.fragment_parks, container, false);
        final TextView textView = root.findViewById(R.id.text_parks);

        textView.setText("---");

        parksViewModel.getParksData().observe(getViewLifecycleOwner(), new Observer<ParksData>() {
            @Override
            public void onChanged(@Nullable ParksData parksData)
            {
                // Model changed => update GUI.
                List<Park> parksList = parksData.parks;

                //List<Park> parks = parksStorage.byName("fun", 10);

                final String firstParkName;
                if (parksList.isEmpty()) {
                    firstParkName = "No matching park .. clear filters";
                } else {
                    Park park = parksList.get(0);
                    String geoCoord = park.getGeoCoordinate().toStringShort(GeoPrecision.Park);
                    City city = park.getCity();
                    firstParkName = park.getName() + " / " + city.getName()
                            + " (" + city.getCountry2letter() + ")"
                            + (geoCoord.isEmpty() ? "" : (" " + geoCoord)
                    );
                }
                textView.setText(firstParkName);
            }
        });

        return root;
    }
}