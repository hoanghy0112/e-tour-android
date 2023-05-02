package com.teamone.e_tour.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.teamone.e_tour.R;
import com.teamone.e_tour.databinding.FragmentVisitorInformationBinding;

public class VisitorInformationFragment extends Fragment {

    public VisitorInformationFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentVisitorInformationBinding binding = FragmentVisitorInformationBinding.inflate(inflater, container, false);

        binding.nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavHostFragment.findNavController(VisitorInformationFragment.this).navigate(R.id.action_visitorInformationFragment2_to_bookTicketFragment);
            }
        });

        return binding.getRoot();
    }
}