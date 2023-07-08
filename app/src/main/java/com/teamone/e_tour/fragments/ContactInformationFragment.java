package com.teamone.e_tour.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.teamone.e_tour.R;
import com.teamone.e_tour.databinding.FragmentContactInformationBinding;
import com.teamone.e_tour.entities.UserProfile;
import com.teamone.e_tour.models.BookingDataManager;
import com.teamone.e_tour.models.UserProfileManager;

import java.util.Objects;

public class ContactInformationFragment extends Fragment {

    public ContactInformationFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentContactInformationBinding binding = FragmentContactInformationBinding.inflate(inflater, container, false);

        BookingDataManager bookingDataManager = BookingDataManager.getInstance();
        binding.fullName.setText(bookingDataManager.getTicketData().ticketInfo.getFullName());
        binding.phoneNumber.setText(bookingDataManager.getTicketData().ticketInfo.getPhoneNumber());
        binding.email.setText(bookingDataManager.getTicketData().ticketInfo.getEmail());

        binding.useYourInfo.setOnClickListener(v -> {
            UserProfile userProfile = UserProfileManager.getInstance(requireActivity()).getUserProfile();

            bookingDataManager.getTicketData().ticketInfo.setFullName(Objects.requireNonNull(userProfile.getFullName()));
            bookingDataManager.getTicketData().ticketInfo.setPhoneNumber(Objects.requireNonNull(userProfile.getPhoneNumber()));
            bookingDataManager.getTicketData().ticketInfo.setEmail(Objects.requireNonNull(userProfile.getEmail()));

            binding.fullName.setText(BookingDataManager.getInstance().getTicketData().ticketInfo.getFullName());
            binding.phoneNumber.setText(BookingDataManager.getInstance().getTicketData().ticketInfo.getPhoneNumber());
            binding.email.setText(BookingDataManager.getInstance().getTicketData().ticketInfo.getEmail());
        });

        binding.nextBtn.setOnClickListener(v -> {
            bookingDataManager.getTicketData().ticketInfo.setFullName(Objects.requireNonNull(binding.fullName.getText()).toString());
            bookingDataManager.getTicketData().ticketInfo.setPhoneNumber(Objects.requireNonNull(binding.phoneNumber.getText()).toString());
            bookingDataManager.getTicketData().ticketInfo.setEmail(Objects.requireNonNull(binding.email.getText()).toString());
            NavHostFragment.findNavController(ContactInformationFragment.this).popBackStack();
        });

        binding.topAppBar.setOnClickListener(v -> NavHostFragment.findNavController(ContactInformationFragment.this).popBackStack());

        return binding.getRoot();
    }
}