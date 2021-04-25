package org.riderun.app.ui.parks;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.google.android.material.chip.Chip;

import org.riderun.app.R;
import org.riderun.app.model.City;
import org.riderun.app.model.GeoPrecision;
import org.riderun.app.model.Park;
import org.riderun.app.provider.park.ParksProvider;
import org.riderun.app.provider.park.mock.ParksMockProvider;
import org.riderun.app.storage.Order;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

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
        final Chip preselectionAll = root.findViewById(R.id.chip_parks_all);
        final Chip preselectionLocation = root.findViewById(R.id.chip_parks_location);
        final Chip preselectionNearby = root.findViewById(R.id.chip_parks_nearby);
        final Chip preselectionTour = root.findViewById(R.id.chip_parks_tour);

        final Spinner spinnerContinent = root.findViewById(R.id.dd_preselection_continent);
        final Spinner spinnerCountry = root.findViewById(R.id.dd_preselection_country);
        final Spinner spinnerCity = root.findViewById(R.id.dd_preselection_city);

        final TableLayout parksTable = root.findViewById(R.id.parks_table);
        final EditText parkNameFilter = root.findViewById(R.id.parks_name_filter);

        //textView.setText("---");

        parkNameFilter.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                parksViewModel.setParkNameFilter(editable.toString());
            }
        });

        parksViewModel.getLiveParksData().observe(getViewLifecycleOwner(), new Observer<ParksData>() {
            @Override
            public void onChanged(@Nullable ParksData parksData)
            {
                // Model changed => update GUI.
                List<Park> parksList = parksData.parks;

                // --- Preselection ---
                ParksFilterCriteria filterCriteria = parksData.filterCriteria;
                ParksPreselection preselection = filterCriteria.preselection;
                preselectionAll.setChecked(preselection == ParksPreselection.All);
                preselectionLocation.setChecked(preselection == ParksPreselection.Location);
                preselectionNearby.setChecked(preselection == ParksPreselection.Nearby);
                preselectionTour.setChecked(preselection == ParksPreselection.Tour);

                // Preselection specific filter criteria
                Set<City> cities = new TreeSet<>(City.orderByName(Order.ASC));
                Set<String> countries = new TreeSet<>();
                Set<String> continents = new TreeSet<>();
                for (Park park : parksList) {
                    City city = park.getCity();
                    cities.add(city);
                    countries.add(city.getCountry2letter());
                }
                continents.add("Africa");
                continents.add("America");
                continents.add("Asia");
                continents.add("Australia");
                continents.add("Europe");
                ArrayList<String> alContinent = new ArrayList<>(continents);
                ArrayAdapter<String> aaContinent = new ArrayAdapter<>(spinnerContinent.getContext(), android.R.layout.simple_list_item_1, alContinent);
                spinnerContinent.setAdapter(aaContinent);

                ArrayList<String> alCountries = new ArrayList<>(countries);
                ArrayAdapter<String> aaContries = new ArrayAdapter<>(spinnerCountry.getContext(), android.R.layout.simple_list_item_1, alCountries);
                spinnerCountry.setAdapter(aaContries);

                ArrayList<String> alCities = new ArrayList<>(cities.size());
                cities.forEach(city -> alCities.add(city.getName()));
                ArrayAdapter<String> aaCity = new ArrayAdapter<>(spinnerCity.getContext(), android.R.layout.simple_list_item_1, alCities);
                spinnerCity.setAdapter(aaCity);



                // Park table
                Context pctx = parksTable.getContext();
                parksTable.removeAllViews();
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