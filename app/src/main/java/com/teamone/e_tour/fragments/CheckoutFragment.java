package com.teamone.e_tour.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.teamone.e_tour.R;
import com.teamone.e_tour.api.ticket.BookTicketApi;
import com.teamone.e_tour.databinding.FragmentCheckoutBinding;
import com.teamone.e_tour.models.BookingDataManager;

public class CheckoutFragment extends Fragment {

    public CheckoutFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        BookTicketFragment.viewSecondTab();
        FragmentCheckoutBinding binding = FragmentCheckoutBinding.inflate(inflater, container, false);

        binding.checkoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BookTicketApi.getInstance(getActivity()).send(BookingDataManager.getInstance().getTicketData());
                NavHostFragment.findNavController(CheckoutFragment.this).navigate(R.id.action_checkoutFragment_to_receiptFragment);
            }
        });

        return binding.getRoot();
    }
}