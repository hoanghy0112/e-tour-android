package com.teamone.e_tour.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.teamone.e_tour.R;
import com.teamone.e_tour.databinding.FragmentRoutePreviewCardSmallBinding;

public class TourPreviewCard extends Fragment {


    public TourPreviewCard() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentRoutePreviewCardSmallBinding binding = FragmentRoutePreviewCardSmallBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }
}