package org.riderun.app.ui.rides;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import org.riderun.app.R;
import org.riderun.app.model.Count;
import org.riderun.app.model.CountEntry;
import org.riderun.app.model.Park;
import org.riderun.app.model.SiteUserData;
import org.riderun.app.model.Ride;
import org.riderun.app.provider.ProviderBundle;
import org.riderun.app.provider.ProviderFactory;
import org.riderun.app.provider.city.CityProvider;
import org.riderun.app.provider.count.CountProvider;
import org.riderun.app.provider.parkuserdata.ParksUserDataProvider;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

/**
 * This Fragment shows several Rides, typically all Rides of a single Park.
 * The rides table shows for each ride if it was counted and allows to count it.
 */
public class RidesFragment extends Fragment {
    private RidesViewModel ridesViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        final ProviderBundle providerBundle = ProviderFactory.get(ProviderFactory.Bundle.RCDB);
        ridesViewModel = new ViewModelProvider(this.getActivity()).get(RidesViewModel.class);
        View root = inflater.inflate(R.layout.fragment_rides, container, false);
        final TextView textView = root.findViewById(R.id.textView_location);
        final TextView countsView = root.findViewById(R.id.textViewCounts);
        final TableLayout rideTable = root.findViewById(R.id.rideTable);

        final ImageButton likeButton = root.findViewById(R.id.park_like_button);

        rideTable.setBackgroundColor(Color.BLACK);

        ridesViewModel.ridesData().observe(getViewLifecycleOwner(), new Observer<RidesData>() {
            @Override
            public void onChanged(@Nullable RidesData rd) {
                Park park = rd.park;
                CityProvider cityProvider = providerBundle.cityProvider();
                ParksUserDataProvider parksUserDataProvider = providerBundle.siteUserDataProvider();
                textView.setText(park.getName() + " / " + cityProvider.byCityId(park.getCityId(), true));

                SiteUserData siteUserData = parksUserDataProvider.byRcdbId(park.getRcdbId());
                likeButton.setActivated(siteUserData.getLiked());

                likeButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        boolean activate = !likeButton.isActivated();
                        SiteUserData siteUserData = parksUserDataProvider.byRcdbId(park.getRcdbId());
                        siteUserData.setLiked(activate);
                        likeButton.setActivated(activate);
                    }
                });

                List<Ride> rides = rd.rides;
                if (rides.isEmpty()) {
                    countsView.setText("-");
                } else {
                    long counted = rides.stream().filter(ride -> {
                        Count count = rd.countByKey(ride.rcdbId());
                        return count != null && !count.isEmpty();
                    }).count();
                    countsView.setText(counted + "/" + rides.size());
                }

                Context ctx = rideTable.getContext();
                rideTable.removeAllViews();

                addHeaderRow(ctx, rideTable);

                CountProvider countProvider = providerBundle.countProvider();
                for (Ride ride : rides) {
                    TableRow tr = new TableRow(ctx);
                    tr.setBackgroundColor(Color.WHITE);

                    Count count = rd.countByKey(ride.rcdbId());
                    CountEntry ceFirst = count == null ? null : count.getFirstEntry();
                    final Button countButton;
                    Button repeatButton = null;
                    if (ceFirst == null) {
                        Button button = new Button(ctx);
                        button.setText("Count");
                        //button.setBackgroundColor(Color.YELLOW);
                        button.setTextColor(Color.BLUE);
                        countButton = button;
                    } else {
                        // Clicking the button means: Edit or add a repeated ride
                        Button button = new Button(ctx);
                        button.setText(ceFirst.formatAsDateTime());
                        //button.setBackgroundColor(Color.LTGRAY);
                        countButton = button;

                        repeatButton = new Button(ctx);
                        repeatButton.setText("+1");
                        repeatButton.setTextColor(Color.BLUE);
                    }

                    countButton.setOnClickListener(view -> openDialog(view, count, countProvider, ride, false));
                    if (repeatButton != null) {
                        repeatButton.setOnClickListener(view -> openDialog(view, count, countProvider, ride, true));
                    }

                    TextView tv1 = new TextView(ctx);
                    tv1.setText(ride.name());
                    tr.addView(tv1);
                    tr.addView(countButton);
                    if (repeatButton != null) {
                        tr.addView(repeatButton);
                    }
                    rideTable.addView(tr);
                }

                addHeaderRow(ctx, rideTable);


            }
        });
        return root;
    }

    private void addHeaderRow(Context ctx, TableLayout rideTable) {
        TableRow th = new TableRow(ctx);
        th.setBackgroundColor(Color.LTGRAY);
        TextView thRide = new TextView(ctx);
        thRide.setText("Ride");
        TextView thCount = new TextView(ctx);
        thCount.setText("Count");
        th.addView(thRide);
        th.addView(thCount);
        rideTable.addView(th);
    }

    private void openDialog(View view, Count count, CountProvider countProvider, Ride ride, boolean addRepeatMode) {
        CountDialog dialog = new CountDialog(ride, count, countProvider, ridesViewModel, addRepeatMode);
        dialog.show(this.getParentFragmentManager(), "info");
    }
}