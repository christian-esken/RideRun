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
import org.riderun.app.model.Park;
import org.riderun.app.storage.ParksMockStorage;
import org.riderun.app.storage.ParksStorage;

import java.util.List;

public class ParksFragment extends Fragment {

    ParksStorage parksStorage;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        parksStorage = new ParksMockStorage();
        ParksViewModel parksViewModel = new ViewModelProvider(this).get(ParksViewModel.class);
        View root = inflater.inflate(R.layout.fragment_parks, container, false);
        final TextView textView = root.findViewById(R.id.text_parks);
        List<Park> parks = parksStorage.byName("fun", 10);

        final String firstParkName;
        if (parks.isEmpty()) {
            firstParkName = "No matching park .. clear filters";
        } else {
            Park park = parks.get(0);
            firstParkName = park.getName();
        }

        textView.setText(firstParkName);


        parksViewModel.getParksData().observe(getViewLifecycleOwner(), new Observer<ParksData>() {
            @Override
            public void onChanged(@Nullable ParksData parksData)
            {
                List<Park> parksList = parksData.parks;
                String firstParkInList = parksList.isEmpty() ? "---" : parksList.get(0).getName();
                textView.setText(firstParkInList);
            }
        });
        return root;
    }
}