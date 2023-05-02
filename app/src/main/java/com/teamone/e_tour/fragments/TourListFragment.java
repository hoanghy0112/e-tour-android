package com.teamone.e_tour.fragments;

import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.teamone.e_tour.R;
import com.teamone.e_tour.adapters.TourAdapter;
import com.teamone.e_tour.api.tour.ViewTourListOfRouteApi;
import com.teamone.e_tour.databinding.FragmentTourListBinding;
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

        String id = getArguments().getString("id");
        String routeName = getArguments().getString("name");
        api = new ViewTourListOfRouteApi(getActivity());
        api.fetchData(id);

        TourAdapter adapter = new TourAdapter(TourListFragment.this);
        adapter.setRouteName(routeName);
        binding.tourList.setAdapter(adapter);
        binding.tourList.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));

        api.getTourList().observe(getViewLifecycleOwner(), new Observer<ArrayList<Tour>>() {
            @Override
            public void onChanged(ArrayList<Tour> tours) {
                adapter.setTourList(tours);
            }
        });

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        api.finish();
    }
}