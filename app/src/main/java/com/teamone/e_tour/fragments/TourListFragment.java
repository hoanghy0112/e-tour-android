package com.teamone.e_tour.fragments;

import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.teamone.e_tour.R;
import com.teamone.e_tour.adapters.TourAdapter;
import com.teamone.e_tour.api.tour.ViewTourListOfRouteApi;
import com.teamone.e_tour.databinding.FragmentTourListBinding;
import com.teamone.e_tour.dialogs.LoadingDialog;
import com.teamone.e_tour.entities.Tour;

import java.util.ArrayList;

public class TourListFragment extends Fragment {
    ViewTourListOfRouteApi api;

    public TourListFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        api = new ViewTourListOfRouteApi(getActivity());

        String id = getArguments().getString("id");
        api.fetchData(id);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentTourListBinding binding = FragmentTourListBinding.inflate(inflater, container, false);

        getActivity().findViewById(R.id.bottom_navigation).setVisibility(View.INVISIBLE);


        Window window = getActivity().getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(getActivity() , R.color.blue));

        binding.topAppBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavHostFragment.findNavController(TourListFragment.this).popBackStack();
            }
        });

        String routeName = getArguments().getString("name");

        TourAdapter adapter = new TourAdapter(TourListFragment.this);
        adapter.setRouteName(routeName);
        binding.tourList.setAdapter(adapter);
        binding.tourList.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));

        LoadingDialog dialog = new LoadingDialog(getActivity());
        dialog.showLoading(getString(R.string.fetching_tour_list_of_route));

        ArrayList<Tour> tours = api.getTourList().getValue();
        if (tours.size() != 0) {
            dialog.dismiss();
            adapter.setTourList(tours);
        }

        api.getTourList().observe(getViewLifecycleOwner(), new Observer<ArrayList<Tour>>() {
            @Override
            public void onChanged(ArrayList<Tour> tours) {
                if (tours.size() == 0) return;
                dialog.dismiss();
                adapter.setTourList(tours);
            }
        });

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        api.finish();
    }
}