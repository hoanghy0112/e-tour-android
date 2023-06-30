package com.teamone.e_tour.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.transition.ChangeBounds;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.teamone.e_tour.R;
import com.teamone.e_tour.activities.AuthenticationActivity;
import com.teamone.e_tour.activities.HomeActivity;
import com.teamone.e_tour.activities.SignUpWithGoogle;
import com.teamone.e_tour.api.account.registration.SignUpWithGoogleAPI;
import com.teamone.e_tour.databinding.FragmentInputInformationSignUpWithGoogleBinding;
import com.teamone.e_tour.models.CredentialToken;
import com.teamone.e_tour.utils.SocketManager;

import java.io.IOException;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InputInformationSignUpWithGoogle extends Fragment {
    String accessToken;
    String email;

    public InputInformationSignUpWithGoogle() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            accessToken = getArguments().getString("accessToken");
            email = getArguments().getString("email");
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentInputInformationSignUpWithGoogleBinding binding = FragmentInputInformationSignUpWithGoogleBinding.inflate(inflater);

        setSharedElementEnterTransition(new ChangeBounds());

        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).popBackStack();
            }
        });

//        Toast.makeText(requireActivity(), accessToken + "; " + email, Toast.LENGTH_SHORT).show();
        binding.email.setText(email);

        binding.signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fullName = Objects.requireNonNull(binding.fullName.getText()).toString();
                String identity = Objects.requireNonNull(binding.citizenId.getText()).toString();
                Boolean isForeigner = binding.isForeigner.isChecked();
                String email = Objects.requireNonNull(binding.email.getText()).toString();
                String phoneNumber = Objects.requireNonNull(binding.phoneNumber.getText()).toString();
                String address = Objects.requireNonNull(binding.address.getText()).toString();

                SignUpWithGoogleAPI.api.registerWithGoogle(new SignUpWithGoogleAPI.RegistrationForm(fullName, identity, isForeigner, email, phoneNumber, address, accessToken)).enqueue(new Callback<SignUpWithGoogleAPI.RegistrationWithPasswordSuccess>() {
                    @Override
                    public void onResponse(@NonNull Call<SignUpWithGoogleAPI.RegistrationWithPasswordSuccess> call, @NonNull Response<SignUpWithGoogleAPI.RegistrationWithPasswordSuccess> response) {
                        try {
                            if (response.code() == 200) {
                                assert response.body() != null;
                                CredentialToken.getInstance(requireActivity()).setCredential(response.body().getUserId(), response.body().getAccessToken(), response.body().getRefreshToken());
                                SocketManager.reload(requireActivity());
                                startActivity(new Intent(requireActivity(), HomeActivity.class));
                                requireActivity().finish();
                            } else {
                                assert response.errorBody() != null;
                                Toast.makeText(requireActivity(), "Success: " + response.code() + " - " + response.errorBody().string().toString(), Toast.LENGTH_SHORT).show();
                            }
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<SignUpWithGoogleAPI.RegistrationWithPasswordSuccess> call, @NonNull Throwable t) {
                        Toast.makeText(requireActivity(), "Error", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });

        return binding.getRoot();
    }
}