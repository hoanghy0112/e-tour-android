package com.teamone.e_tour.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.FragmentNavigator;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.teamone.e_tour.R;
import com.teamone.e_tour.databinding.FragmentFirstSignupGoogleGreetingBinding;

public class FirstSignupGoogleGreeting extends Fragment {
    String accessToken;
    String email;

    public FirstSignupGoogleGreeting() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (requireActivity().getIntent().getExtras() != null) {
            accessToken = requireActivity().getIntent().getExtras().getString("accessToken");
            email = requireActivity().getIntent().getExtras().getString("email");
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentFirstSignupGoogleGreetingBinding binding = FragmentFirstSignupGoogleGreetingBinding.inflate(inflater);

        binding.signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("accessToken", accessToken);
                bundle.putString("email", email);
                FragmentNavigator.Extras extras = new FragmentNavigator.Extras.Builder()
                        .addSharedElement(binding.signUpBtn, "sign_up_btn")
                        .addSharedElement(binding.greetingContainer, "greeting")
                        .build();
                Navigation.findNavController(v).navigate(R.id.action_firstSignupGoogleGreeting_to_inputInformationSignUpWithGoogle, bundle, null, extras);
                binding.signUpBtn.setOnClickListener(null);
            }
        });

        return binding.getRoot();
    }
}