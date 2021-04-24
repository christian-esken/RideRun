package org.riderun.app.ui.parks;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
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
import org.riderun.app.provider.park.mock.ParksMockProvider;
import org.riderun.app.provider.park.ParksProvider;

import java.util.List;

/**
 * The ParksFragment shows a list of parks, and allows to chose a park to be displayed in the
 * {@link org.riderun.app.ui.rides.RidesFragment}. The list shows the most relevant parks
 * according to the users selection criteria. Selection criteria can be distance, park name,
 * park type (water park, has real-coaster, has alpinecoaster).
 */
public class ParksFragment extends Fragment {

    ParksProvider parksProvider;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        parksProvider = ParksMockProvider.instance();
        ParksViewModel parksViewModel = new ViewModelProvider(this).get(ParksViewModel.class);
        View root = inflater.inflate(R.layout.fragment_parks, container, false);
        final TextView textView = root.findViewById(R.id.text_parks);
        final TableLayout parksTable = root.findViewById(R.id.parks_table);
        final EditText parkNameFilter = root.findViewById(R.id.parks_name_filter);

        //textView.setText("---");

        parkNameFilter.setOnKeyListener((view,arg2,event)  -> {
            parksViewModel.setParkNameFilter(parkNameFilter.getText().toString());
            return true; // handled
        });

        parksViewModel.getLiveParksData().observe(getViewLifecycleOwner(), new Observer<ParksData>() {
            @Override
            public void onChanged(@Nullable ParksData parksData)
            {
                // Model changed => update GUI.
                List<Park> parksList = parksData.parks;

                //List<Park> parks = parksStorage.byName("fun", 10);

                final String firstParkName;
                parksTable.removeAllViews();
                Context pctx = parksTable.getContext();

                if (parksList.isEmpty()) {
                    TextView tv = new TextView(pctx);
                    tv.setText("No matching park .. clear filters"); // TODO translation
                    parksTable.addView(tv);
                } else {
                    for (Park park : parksList) {
                        TableRow tr = new TableRow(pctx);
                        Context ctx = tr.getContext();
                        TextView parkName = new TextView(ctx);
                        parkName.setText(park.getName());
                        TextView cityName = new TextView(ctx);
                        City city = park.getCity();
                        cityName.setText(city.getName() + " (" + city.getCountry2letter() + ")");

                        TextView geoView = new TextView(ctx);
                        String geoString = park.getGeoCoordinate().toStringShort(GeoPrecision.Park);
                        geoView.setText(geoString);

                        tr.addView(parkName);
                        tr.addView(cityName);
                        tr.addView(geoView);

                        parksTable.addView(tr);
                    }
                }
            }
        });

        return root;
    }
}