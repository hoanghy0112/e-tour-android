package com.teamone.e_tour.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
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
import android.widget.TextView;

import com.faltenreich.skeletonlayout.Skeleton;
import com.faltenreich.skeletonlayout.SkeletonLayoutUtils;
import com.teamone.e_tour.R;
import com.teamone.e_tour.adapters.TourAdapter;
import com.teamone.e_tour.api.tour.ViewTourListOfRouteApi;
import com.teamone.e_tour.databinding.FragmentTourListBinding;
import com.teamone.e_tour.dialogs.LoadingDialog;
import com.teamone.e_tour.entities.Tour;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Date;

public class TourListFragment extends Fragment {
    ViewTourListOfRouteApi api;

    public TourListFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        api = new ViewTourListOfRouteApi(getActivity());

        assert getArguments() != null;
        String id = getArguments().getString("id");
        api.fetchData(id);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentTourListBinding binding = FragmentTourListBinding.inflate(inflater, container, false);

        requireActivity().findViewById(R.id.bottom_navigation).setVisibility(View.INVISIBLE);


        Window window = requireActivity().getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(requireActivity(), R.color.blue));

        binding.backBtn.setOnClickListener(new View.OnClickListener() {
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

        Skeleton skeleton = SkeletonLayoutUtils.applySkeleton(binding.tourList, R.layout.tour_preview_item, 4);
        skeleton.setMaskCornerRadius(50);
        skeleton.showSkeleton();

        ViewGroup parent = (ViewGroup) binding.tourList.getParent();
        int viewIndex = parent.indexOfChild(binding.tourList);

        ArrayList<Tour> tours = api.getTourList().getValue();
        if (tours != null) {
            skeleton.showOriginal();
            adapter.setTourList(tours);
        }

        api.getTourList().observe(getViewLifecycleOwner(), tours1 -> {
            if (tours1 == null) return;
            ArrayList<Tour> newTours = new ArrayList<>();
            tours1.forEach(tour -> {
                if (tour.getFrom().after(new Date())) {
                    newTours.add(tour);
                }
            });
            if (newTours.size() == 0) {
                View loadingView = LayoutInflater.from(requireActivity()).inflate(R.layout.not_found, parent, false);
                ((TextView) loadingView.findViewById(R.id.loading_text)).setText(requireActivity().getString(R.string.no_tour));
                parent.addView(loadingView, viewIndex + 1);
            } else {
                if (parent.getChildCount() > 1)
                    parent.removeViewAt(viewIndex + 1);
            }
            skeleton.showOriginal();
            adapter.setTourList(newTours);
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