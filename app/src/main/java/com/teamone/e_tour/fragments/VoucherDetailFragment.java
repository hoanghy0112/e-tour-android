package com.teamone.e_tour.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.teamone.e_tour.R;
import com.teamone.e_tour.databinding.FragmentVoucherDetailBinding;

public class VoucherDetailFragment extends Fragment {

    public VoucherDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentVoucherDetailBinding binding = FragmentVoucherDetailBinding.inflate(inflater, container, false);

        getActivity().findViewById(R.id.bottom_navigation).setVisibility(View.INVISIBLE);
        getActivity().findViewById(R.id.home_wrapper).setPadding(0, 0, 0, 0);

        binding.topAppBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavHostFragment.findNavController(VoucherDetailFragment.this).popBackStack();
            }
        });

        return binding.getRoot();
    }
}