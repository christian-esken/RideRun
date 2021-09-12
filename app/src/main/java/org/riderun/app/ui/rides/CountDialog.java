package org.riderun.app.ui.rides;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;

import org.riderun.app.model.Count;
import org.riderun.app.model.CountEntry;
import org.riderun.app.model.Ride;
import org.riderun.app.provider.count.CountProvider;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

public class CountDialog extends AppCompatDialogFragment {
    private final Ride ride;
    private final Count count;
    private final CountProvider countProvider;
    private final RidesViewModel ridesViewModel;
    private final boolean editMode;

    public CountDialog(Ride ride, Count count, CountProvider countProvider, RidesViewModel ridesViewModel, boolean editMode) {
        super();
        this.ride = ride;
        this.count = count;
        this.countProvider = countProvider;
        this.ridesViewModel = ridesViewModel;
        this.editMode = editMode;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        String msg = ride.name() + " (" + ride.rcdbId() + ") ";
        //final  Count count = ride.getCount();
        boolean actionIsRemove = editMode && count != null && !count.isEmpty();
        if (editMode) {
            // Guarantered here: count != null
            CountEntry lastEntry = count.getLastEntry();
            Instant instant = lastEntry.instant();
            if (lastEntry.instant() != null && instant.isBefore(Instant.now().minus(1, ChronoUnit.DAYS))) {
                actionIsRemove = false;
                msg += " was counted more than 1 day ago. Deletion prohibited. Confirm with OK to add add the count instead.";
            } else {
                msg += " is already counted " + count.size() +" times. Confirm with OK to remove the latest count. " + lastEntry.formatAsDateTime();
            }
        } else {
            CountEntry lastEntry = count == null ? null : count.getLastEntry();
            if (lastEntry != null) {
                msg += " is already counted. Confirm with OK to add another count. " + lastEntry.formatAsDateTime();
            } else {
                msg += " is not counted yet. Confirm with OK to mark the count.";
            }
        }

        final boolean actionIsRemoveL = actionIsRemove;

        builder.setTitle("Count Ride").setMessage(msg);
        builder.setPositiveButton("OK", (dialogInterface, i) -> {
            if (actionIsRemoveL) {
                count.removeCount(count.getLastEntry());
                ridesViewModel.notifyCountChange(ride, count, countProvider);
            } else {
                // TODO Add comment field in GUI, for optional comments
                Count countToUpdate = count == null ? new Count() : count;
                countToUpdate.addCountNow(null);
                ridesViewModel.notifyCountChange(ride, countToUpdate, countProvider);
            }
        });
        builder.setNegativeButton("Cancel", (a,b) -> {} );

        return builder.create();
    }
}
