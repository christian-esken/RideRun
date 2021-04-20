package org.riderun.app.ui.rides;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import org.riderun.app.R;
import org.riderun.app.model.Park;
import org.riderun.app.model.Ride;

import java.time.LocalDate;
import java.util.List;

public class RidesFragment extends Fragment {

    private RidesViewModel homeViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(RidesViewModel.class);
        View root = inflater.inflate(R.layout.fragment_rides, container, false);
        final TextView textView = root.findViewById(R.id.textView_location);
        final TextView counts = root.findViewById(R.id.textViewCounts);
        final TableLayout rideTable = root.findViewById(R.id.rideTable);
        rideTable.setBackgroundColor(Color.BLACK);

        homeViewModel.ridesData().observe(getViewLifecycleOwner(), new Observer<RidesData>() {
            @Override
            public void onChanged(@Nullable RidesData rd) {
                Park park = rd.park;
                textView.setText(park.getName() + " / " + park.getCity().getName());

                List<Ride> rides = rd.rides;
                if (rides.isEmpty()) {
                    counts.setText("-");
                } else {
                    long counted = rides.stream().filter(i -> i.getCountDate() != null).count();
                    counts.setText(counted + "/" + rides.size());
                }

                Context ctx = rideTable.getContext();
                TableRow th = new TableRow(ctx);
                th.setBackgroundColor(Color.LTGRAY);
                TextView thRide = new TextView(ctx);
                thRide.setText("Ride");
                TextView thCount = new TextView(ctx);
                thCount.setText("Count");
                th.addView(thRide);
                th.addView(thCount);
                rideTable.addView(th);

                for (Ride ride : rides) {
                    TableRow tr = new TableRow(ctx);
                    tr.setBackgroundColor(Color.WHITE);

                    LocalDate countDate = ride.getCountDate();
                    final View countView;
                    if (countDate == null) {
                        Button button = new Button(ctx);
                        button.setText("Count");
                        //button.setBackgroundColor(Color.YELLOW);
                        button.setTextColor(Color.BLUE);
                        countView = button;
                    } else {
                        // Clicking the button mans: Edit or add a repeated ride
                        TextView button = new Button(ctx);
                        button.setText(countDate.toString());
                        //button.setBackgroundColor(Color.LTGRAY);
                        countView = button;
                    }

                    TextView tv1 = new TextView(ctx);
                    tv1.setText(ride.name());
                    tr.addView(tv1);
                    tr.addView(countView);
                    rideTable.addView(tr);
                }

            }
        });
        return root;
    }
}