package com.teamone.e_tour.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.teamone.e_tour.R;
import com.teamone.e_tour.activities.AuthenticationActivity;
import com.teamone.e_tour.activities.RegistrationActivity;
import com.teamone.e_tour.api.account.authentication.SignInWithPasswordApiError;
import com.teamone.e_tour.api.account.registration.RegistrationAPI;
import com.teamone.e_tour.databinding.FragmentBasicInforRegistrationBinding;
import com.teamone.e_tour.dialogs.RegistrationDialog;
import com.teamone.e_tour.models.CredentialToken;
import com.teamone.e_tour.viewmodels.RegistrationViewModel;

import java.io.IOException;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BasicInforRegistrationFragment extends Fragment {
    private Context context = getActivity();
    private FragmentBasicInforRegistrationBinding binding;
    private RegistrationViewModel viewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentBasicInforRegistrationBinding.inflate(inflater, container, false);

        RegistrationActivity activity = (RegistrationActivity) getActivity();
        viewModel = Objects.requireNonNull(activity).getRegistrationViewModel();
        binding.setViewModel(viewModel);

        binding.signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRegistration();
            }
        });

        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavHostFragment.findNavController(BasicInforRegistrationFragment.this).navigate(R.id.action_basicInforRegistrationFragment_to_accountRegistrationFragment);
            }
        });

        return binding.getRoot();
    }

    public void onRegistration() {
        RegistrationDialog dialog = new RegistrationDialog(getActivity());
        dialog.showLoading();

        RegistrationAPI.api.registerWithPassword(new RegistrationAPI.RegistrationForm(
                viewModel.fullname.get(),
                viewModel.id.get(),
                viewModel.isForeigner.get(),
                viewModel.email.get(),
                viewModel.username.get(),
                viewModel.password.get(),
                viewModel.phoneNumber.get(),
                viewModel.address.get()
        )).enqueue(new Callback<RegistrationAPI.RegistrationWithPasswordSuccess>() {
            @Override
            public void onResponse(Call<RegistrationAPI.RegistrationWithPasswordSuccess> call, Response<RegistrationAPI.RegistrationWithPasswordSuccess> response) {
                if (response.code() == 200 && response.body() != null) {
                    RegistrationAPI.RegistrationWithPasswordSuccess result = response.body();
                    CredentialToken.getInstance(context).setCredential(result.getUserId(), result.getAccessToken(), result.getRefreshToken());
                    NavHostFragment.findNavController(BasicInforRegistrationFragment.this).navigate(R.id.action_basicInforRegistrationFragment_to_postRegistrationFragment);
                    dialog.dismiss();
                } else {
                    Gson gson = new GsonBuilder().create();
                    try {
                        assert response.errorBody() != null;
                        SignInWithPasswordApiError error = gson.fromJson(response.errorBody().string(), SignInWithPasswordApiError.class);
                        dialog.showError(error.getMessage());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }

            }

            @Override
            public void onFailure(Call<RegistrationAPI.RegistrationWithPasswordSuccess> call, Throwable t) {

            }
        });
    }
}