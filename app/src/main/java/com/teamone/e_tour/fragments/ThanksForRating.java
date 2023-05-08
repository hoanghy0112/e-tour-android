package com.teamone.e_tour.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.teamone.e_tour.R;
import com.teamone.e_tour.databinding.FragmentThanksForRatingBinding;

public class ThanksForRating extends Fragment {

    public ThanksForRating() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentThanksForRatingBinding binding = FragmentThanksForRatingBinding.inflate(inflater, container, false);

        binding.backToHomeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(getActivity(), R.id.home_wrapper).navigate(R.id.action_thanksForRating_to_homeFragment);
            }
        });

        return binding.getRoot();
    }
}