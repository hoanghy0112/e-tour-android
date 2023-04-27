package com.teamone.e_tour.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.teamone.e_tour.R;
import com.teamone.e_tour.databinding.ActivityRegistrationActivitiesBinding;
import com.teamone.e_tour.fragments.AccountRegistrationFragment;
import com.teamone.e_tour.fragments.BasicInforRegistrationFragment;
import com.teamone.e_tour.viewmodels.RegistrationViewModel;

public class RegistrationActivity extends AppCompatActivity {
    RegistrationViewModel registrationViewModel = new RegistrationViewModel(this);

    public RegistrationViewModel getRegistrationViewModel() {
        return registrationViewModel;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        registrationViewModel = new RegistrationViewModel(this);

        ActivityRegistrationActivitiesBinding binding = ActivityRegistrationActivitiesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }

    public void navigateToAccountRegistration() {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.registration_wrapper, new AccountRegistrationFragment(this, new AccountRegistrationFragment.INavigate() {
            @Override
            public void onNavigate() {
                navigateToBasicInfoRegistration();
            }
        }));
        fragmentTransaction.commitAllowingStateLoss();
    }

    public void navigateToBasicInfoRegistration() {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.registration_wrapper, new BasicInforRegistrationFragment());
        fragmentTransaction.commitAllowingStateLoss();
    }
}