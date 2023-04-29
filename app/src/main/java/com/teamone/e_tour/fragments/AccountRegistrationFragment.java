package com.teamone.e_tour.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.teamone.e_tour.R;
import com.teamone.e_tour.activities.RegistrationActivity;
import com.teamone.e_tour.databinding.FragmentAccountRegistrationBinding;

import java.util.Objects;


public class AccountRegistrationFragment extends Fragment {
    private Context context;

    public AccountRegistrationFragment() {
    }

    private FragmentAccountRegistrationBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAccountRegistrationBinding.inflate(inflater, container, false);
        RegistrationActivity activity = (RegistrationActivity) getActivity();
        binding.setViewModel(Objects.requireNonNull(activity).getRegistrationViewModel());
        binding.accountRegistrationNextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavHostFragment.findNavController(AccountRegistrationFragment.this).navigate(R.id.action_accountRegistrationFragment_to_basicInforRegistrationFragment);
            }
        });

        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });

        return binding.getRoot();
    }
}