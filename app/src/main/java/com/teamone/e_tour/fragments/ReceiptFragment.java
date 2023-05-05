package com.teamone.e_tour.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.teamone.e_tour.R;
import com.teamone.e_tour.api.ticket.BookTicketApi;
import com.teamone.e_tour.databinding.FragmentReceiptBinding;
import com.teamone.e_tour.dialogs.LoadingDialog;
import com.teamone.e_tour.entities.Ticket;
import com.teamone.e_tour.models.BookingDataManager;

public class ReceiptFragment extends Fragment {

    public ReceiptFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentReceiptBinding binding = FragmentReceiptBinding.inflate(inflater, container, false);
        BookTicketFragment.viewThirdTab();

        LoadingDialog dialog = new LoadingDialog(getActivity());
        dialog.showLoading("We are preparing for your ticket");

        binding.backToHomeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(getActivity(), R.id.home_wrapper).navigate(R.id.action_bookTicketFragment_to_homeFragment);
            }
        });

        BookTicketApi.getInstance(getActivity()).getData().observe(getViewLifecycleOwner(), new Observer<Ticket>() {
            @Override
            public void onChanged(Ticket ticket) {
                if (ticket == null) return;

                BookingDataManager.getInstance().setBookedTicket(ticket);

                dialog.dismiss();

                binding.totalPrice.setText(String.valueOf(ticket.getPrice()));
                binding.ticketId.setText(ticket.get_id());
                binding.customerName.setText(ticket.getFullName());
                binding.phoneNumber.setText(ticket.getPhoneNumber());
                binding.bookingDate.setText(ticket.getCreatedAt().toString());
            }
        });

        BookingDataManager.getInstance().getErrorMessage().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                if (s.length() == 0) return;
                dialog.dismiss();
                NavHostFragment.findNavController(ReceiptFragment.this).popBackStack();
                NavHostFragment.findNavController(ReceiptFragment.this).popBackStack();
            }
        });

        return binding.getRoot();
    }
}