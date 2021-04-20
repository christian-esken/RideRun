package org.riderun.app.ui.rides;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import org.riderun.app.model.Ride;

import java.time.LocalDate;
import java.time.LocalDateTime;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

public class CountDialog extends AppCompatDialogFragment {

    private final Ride ride;
    private final RidesViewModel ridesViewModel;

    public CountDialog(Ride ride, RidesViewModel ridesViewModel) {
        super();
        this.ride = ride;
        this.ridesViewModel = ridesViewModel;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        String msg = ride.name() + " (" + ride.rcdbId() + ") ";
        LocalDate countDate = ride.getCountDate();
        if (countDate == null) {
            msg += " is not counted yet. Confirm with OK to mark the count.";
         } else {
            msg += " is already counted. Confirm with OK to remove the count.";
        }

        builder.setTitle("Count Ride").setMessage(msg);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                LocalDate countDate = ride.getCountDate();
                if (countDate == null) {
                    ride.setCountDate(LocalDateTime.now());
                } else {
                    ride.setCountDate(null);
                }
                ridesViewModel.notifyFieldModification();
            }
        });
        builder.setNegativeButton("Cancel", (a,b) -> {} );

        return builder.create();
    }
}
