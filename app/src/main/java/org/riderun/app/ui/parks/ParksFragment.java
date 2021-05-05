package org.riderun.app.ui.parks;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.google.android.material.chip.Chip;

import org.riderun.app.R;
import org.riderun.app.model.City;
import org.riderun.app.model.Continent;
import org.riderun.app.model.Country;
import org.riderun.app.model.GeoPrecision;
import org.riderun.app.model.Park;
import org.riderun.app.provider.ProviderFactory;
import org.riderun.app.provider.city.CityProvider;
import org.riderun.app.provider.country.CountryProvider;
import org.riderun.app.provider.park.ParksProvider;
import org.riderun.app.storage.Order;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
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

    static final String SPINNER_DEFAULT_ALL = "All";
    ParksProvider parksProvider;
    CityProvider cityProvider;
    CountryProvider countryProvider;

    // Note onCreateView looks up the UI elements, install listeners and starts observing the
    // view model.
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        parksProvider = ProviderFactory.parksProvider();
        cityProvider = ProviderFactory.cityProvider();
        countryProvider = ProviderFactory.countryProvider();

        ParksViewModel parksViewModel = new ViewModelProvider(this.getActivity()).get(ParksViewModel.class);
        View root = inflater.inflate(R.layout.fragment_parks, container, false);
        final Chip preselectionAll = root.findViewById(R.id.chip_parks_favorites);
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

        spinnerContinent.setOnItemSelectedListener(new GeoSpinnerOnItemSelectedListener() {
            void set(Object newValue) { parksViewModel.setLocationContinent((String)newValue); }
        });

        spinnerCountry.setOnItemSelectedListener(new GeoSpinnerOnItemSelectedListener() {
            void set(Object newValue) { parksViewModel.setLocationCountry((String)newValue); }
        });

        spinnerCity.setOnItemSelectedListener(new GeoSpinnerOnItemSelectedListener() {
            void set(Object newValue) {
                if (newValue == null || newValue instanceof String) {
                    // SHOULD be the "ALL" selection
                    parksViewModel.setLocationCityId(null);
                } else {
                    parksViewModel.setLocationCityId(((City)newValue).getCityId());
                }
            }
        });

        parksViewModel.getLiveParksData().observe(getViewLifecycleOwner(), new Observer<ParksData>() {
            @Override
            public void onChanged(@Nullable ParksData parksData)
            {
                // Model changed => update GUI.

                // --- Preselection buttons ---
                ParksFilterCriteria filterCriteria = parksData.filterCriteria;
                ParksPreselection preselection = filterCriteria.preselection;
                preselectionAll.setChecked(preselection == ParksPreselection.All);
                preselectionLocation.setChecked(preselection == ParksPreselection.Location);
                preselectionNearby.setChecked(preselection == ParksPreselection.Nearby);
                preselectionTour.setChecked(preselection == ParksPreselection.Tour);

                // Preselection specific filter criteria

                // Preselection specific filter criteria: Location (continent, country, city)
                List<Park> allParks = parksProvider.all();
                SortedSet<City> cities = new TreeSet<>(City.orderByName(Order.ASC));
                SortedSet<Country> countries = new TreeSet<>(Country.orderByCountryName(Order.ASC));

                Country countryFromModel = null;
                City cityFromModel = null;

                GeoLevel geoLevel = null;
                {   // Opening a block solely to limit variable scope
                    // Create the pre-Selected city list from location filter
                    Integer locationCityId = filterCriteria.locationCityId;
                    Map<Integer, City> cityIdMap = new HashMap<>();
                    if (locationCityId != null) {
                        cityFromModel = cityProvider.byCityId(locationCityId, false);
                        if (cityFromModel != null) {
                            cityIdMap.put(cityFromModel.getCityId(), cityFromModel);
                            countryFromModel = countryProvider.countryBy2letterCC(cityFromModel.getCountry2letter());
                            geoLevel = GeoLevel.City;
                        }
                    }
                    if (geoLevel == null) {
                        // No city given. lets check if we have a country filter
                        String cc = filterCriteria.locationCountryCode2letter;
                        if (cc != null && !cc.equals(SPINNER_DEFAULT_ALL)) {
                            countryFromModel = countryProvider.countryBy2letterCC(cc);
                            List<City> citiesMatchingCC = cityProvider.byCountryCode(cc);
                            citiesMatchingCC.forEach(city -> cityIdMap.put(city.getCityId(), city));
                            if (!citiesMatchingCC.isEmpty()) {
                                geoLevel = GeoLevel.Country;
                            }
                        }
                    }
                    // TODO Continent matching
                    if (geoLevel == null) {
                        cityProvider.cities().forEach(city -> cityIdMap.put(city.getCityId(), city));
                        geoLevel = GeoLevel.World;
                    }

                    for (City city : cityIdMap.values()) {
                        cities.add(city);
                        Country country = countryProvider.countryBy2letterCC(city.getCountry2letter());
                        if (country != null) {
                            countries.add(country);
                        }
                    }
                }
                // TODO Continents should be derived from the country, but we do not have a
                //      CountryProvider yet.
                List<Continent> continents = Continent.allContinents();

                updateSpinnerAdapter(spinnerContinent, continents, filterCriteria.locationContinent);
                // Selecting by country name is not 100% correct. If there are 2 identically named entries,
                // a direct selection by ID will be required. For now we don't have this. It may be
                // required to fix this later, expecially for other spinners (e.g. city spinner).
                //String countrySelection = countryFromModel == null ? null : countryFromModel.countryName;
                updateSpinnerAdapter(spinnerCountry, countries, countryFromModel);
                updateSpinnerAdapter(spinnerCity, cities, cityFromModel);

                // Park table
                Context pctx = parksTable.getContext();
                parksTable.removeAllViews();

                List<Park> parksList = parksData.parks;

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
                        City city = cityProvider.byCityId(park.getCityId(), true);
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

    /**
     * Updates a Spinner with new entries from the given Set. The Spinner selection is set to
     * the entry from entries that matches the selection. Formally entry.equals(selection).
     * <br>
     *     Note: As this method replaces the ArrayAdapter of the Spinner, update methods may
     *      get triggered.
     *
     * @param spinner
     * @param entries
     * @param selection
     */
    private void updateSpinnerAdapter(Spinner spinner, Collection<?> entries, Object selection) {
        ArrayList al = new ArrayList(entries.size()+1);
        al.add(SPINNER_DEFAULT_ALL);
        entries.forEach(entry -> al.add(entry));
        ArrayAdapter<String> aaContinent = new ArrayAdapter<>(spinner.getContext(), android.R.layout.simple_list_item_1, al);
        spinner.setAdapter(aaContinent);
        setSpinnerSelection(spinner, al, selection, 0);
    }

    /**
     * Set selection on the spinner element that matches the given String. Hint: The spinnerArray
     * should be the same ArrayList that is used for the Adapter.
     * @param spinner
     * @param spinnerArray
     * @param match
     */
    private void setSpinnerSelection(Spinner spinner, ArrayList<String> spinnerArray, Object match, int fallbackId) {
        int i = 0;
        for (Object s : spinnerArray) {
            if (s.equals(match)) {
                spinner.setSelection(i);
                return;
            }
            i++;
        }
        spinner.setSelection(fallbackId);
    }

    private enum GeoLevel {
        World(1),Continent(3), Country(5), City(7);

        private final int level;

        GeoLevel(int level) {
            this.level = level;
        }

        boolean precisionIsAtLeast(GeoLevel minimumLevel) {
            return this.level >= minimumLevel.level;
        }
    }

    /**
     * An abstract OnItemSelectedListener implementation that unifies onItemSelected() and
     * onNothingSelected() behaviour. Both call {@link #set(Object)}, either with null or the
     * item at spinner position #pos.
     */
    private abstract class GeoSpinnerOnItemSelectedListener implements AdapterView.OnItemSelectedListener {

        @Override
        public final void onItemSelected(AdapterView adapterView, View view, int pos, long id) {
            Object item = adapterView.getItemAtPosition(pos);
            final String newValue;  // The new spinner value (only for "all" check). Must match the spinner value (as of toString()).
            final Object valueForSet; // The value that goes back to the model
            if (item instanceof String) {
                newValue = (String)item;
                valueForSet = newValue;
            } else if (item instanceof City) {
                City item1 = (City) item;
                valueForSet = (City)item1;
                newValue = item1.getName();
            } else if (item instanceof Country) {
                Country item1 = (Country) item;
                valueForSet = item1.cc2Letter;
                newValue = item1.countryName;
            } else {
                newValue = item.toString();
                valueForSet = newValue;
            }
            boolean all = SPINNER_DEFAULT_ALL.equals(newValue);
            set(all ? null : valueForSet);
        }

        @Override
        public final void onNothingSelected(AdapterView<?> adapterView) {
            set(null);
        }

        abstract void set(Object newValue);
    }
}