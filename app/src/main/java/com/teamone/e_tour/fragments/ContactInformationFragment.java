package com.teamone.e_tour.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.teamone.e_tour.R;
import com.teamone.e_tour.databinding.FragmentContactInformationBinding;
import com.teamone.e_tour.models.BookingDataManager;

public class ContactInformationFragment extends Fragment {

    public ContactInformationFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentContactInformationBinding binding = FragmentContactInformationBinding.inflate(inflater, container, false);

        BookingDataManager bookingDataManager = BookingDataManager.getInstance();
        binding.fullName.setText(bookingDataManager.getTicketData().ticketInfo.getFullName());
        binding.phoneNumber.setText(bookingDataManager.getTicketData().ticketInfo.getPhoneNumber());
        binding.email.setText(bookingDataManager.getTicketData().ticketInfo.getEmail());

        binding.nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bookingDataManager.getTicketData().ticketInfo.setFullName(binding.fullName.getText().toString());
                bookingDataManager.getTicketData().ticketInfo.setPhoneNumber(binding.phoneNumber.getText().toString());
                bookingDataManager.getTicketData().ticketInfo.setEmail(binding.email.getText().toString());
                NavHostFragment.findNavController(ContactInformationFragment.this).navigate(R.id.action_contactInformationFragment_to_bookTicketFragment);
            }
        });

        binding.topAppBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavHostFragment.findNavController(ContactInformationFragment.this).popBackStack();
            }
        });

        return binding.getRoot();
    }
}