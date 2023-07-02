package com.teamone.e_tour.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.teamone.e_tour.R;
import com.teamone.e_tour.activities.AuthenticationActivity;
import com.teamone.e_tour.databinding.FragmentAccountTabBinding;
import com.teamone.e_tour.entities.UserProfile;
import com.teamone.e_tour.models.CredentialToken;

public class AccountTab extends Fragment {

    public AccountTab() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentAccountTabBinding binding = FragmentAccountTabBinding.inflate(inflater, container, false);

        CredentialToken.getInstance(getActivity()).getUserProfile().observe(getViewLifecycleOwner(), new Observer<UserProfile>() {
            @Override
            public void onChanged(UserProfile userProfile) {
                if (userProfile == null) return;
                binding.userDisplayName.setText(userProfile.getFullName());
                binding.email.setText(userProfile.getEmail());
                binding.userDisplayName.setText(userProfile.getFullName());
                if (!userProfile.getImage().equals(""))
                    Glide.with(requireActivity()).load(userProfile.getImage()).into(binding.userImage);
            }
        });

        binding.contactSupportBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        binding.logOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CredentialToken.getInstance(getActivity()).setCredential("", "", "");
                startActivity(new Intent(getActivity(), AuthenticationActivity.class));
                requireActivity().finish();
            }
        });

        return binding.getRoot();
    }
}