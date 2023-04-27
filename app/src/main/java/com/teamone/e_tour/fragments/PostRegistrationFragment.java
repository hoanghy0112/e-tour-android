package com.teamone.e_tour.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.teamone.e_tour.R;
import com.teamone.e_tour.activities.HomeActivity;
import com.teamone.e_tour.activities.RegistrationActivity;
import com.teamone.e_tour.databinding.FragmentPostRegistrationBinding;

import java.util.Objects;

public class PostRegistrationFragment extends Fragment {

    public PostRegistrationFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentPostRegistrationBinding binding = FragmentPostRegistrationBinding.inflate(inflater, container, false);

        RegistrationActivity activity = (RegistrationActivity) getActivity();
        binding.setViewModel(Objects.requireNonNull(activity).getRegistrationViewModel());

        binding.goToHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), HomeActivity.class));
            }
        });

        return binding.getRoot();
    }
}