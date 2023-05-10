package com.teamone.e_tour.fragments;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.teamone.e_tour.R;
import com.teamone.e_tour.adapters.RouteListAdapter;
import com.teamone.e_tour.databinding.FragmentSavedTabBinding;
import com.teamone.e_tour.dialogs.LoadingDialog;
import com.teamone.e_tour.entities.TouristRoute;
import com.teamone.e_tour.models.SavedRouteManager;

import java.util.ArrayList;

public class SavedTab extends Fragment {

    public SavedTab() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentSavedTabBinding binding = FragmentSavedTabBinding.inflate(inflater, container, false);

        RouteListAdapter adapter = new RouteListAdapter(this, R.layout.fragment_route_preview_card_large);
        LoadingDialog dialog = new LoadingDialog(getActivity());
        dialog.showLoading("Loading data");

        SavedRouteManager.getInstance((AppCompatActivity) getActivity()).getRoutes().observe(getViewLifecycleOwner(), new Observer<ArrayList<TouristRoute>>() {
            @Override
            public void onChanged(ArrayList<TouristRoute> touristRoutes) {
                if (touristRoutes == null) return;
                dialog.dismiss();
                adapter.setRouteList(touristRoutes);
            }
        });

        binding.savedRouteList.setAdapter(adapter);
        binding.savedRouteList.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));

        return binding.getRoot();
    }
}