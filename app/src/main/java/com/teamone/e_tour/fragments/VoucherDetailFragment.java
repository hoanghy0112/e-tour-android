package com.teamone.e_tour.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.teamone.e_tour.R;
import com.teamone.e_tour.databinding.FragmentVoucherDetailBinding;
import com.teamone.e_tour.entities.Image;

public class VoucherDetailFragment extends Fragment {

    public VoucherDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @SuppressLint("SetTextI18n")
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

        Bundle bundle = getArguments();

        assert bundle != null;
        binding.voucherName.setText(bundle.getString("name"));
        binding.voucherDescription.setText(bundle.getString("description"));
        binding.usingCondition.setText(bundle.getString("usingCondition"));
        binding.expiredDate.setText(bundle.getString("expiredAt"));
        binding.voucherValue.setText(getActivity().getString(R.string.discount) + " " + String.valueOf((int)(bundle.getFloat("value") * 100)) + "%");

        Glide.with(getActivity()).load(new Image(bundle.getString("image")).getImageUri()).into(binding.voucherImage);

        return binding.getRoot();
    }
}