package com.teamone.e_tour.fragments.screens;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.teamone.e_tour.R;
import com.teamone.e_tour.databinding.FragmentRateTourBinding;

public class RateTour extends Fragment {

    public RateTour() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentRateTourBinding binding = FragmentRateTourBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }
}