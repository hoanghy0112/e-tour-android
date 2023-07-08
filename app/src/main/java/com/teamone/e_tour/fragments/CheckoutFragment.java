package com.teamone.e_tour.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.teamone.e_tour.R;
import com.teamone.e_tour.adapters.VoucherAdapter;
import com.teamone.e_tour.api.ticket.BookTicketApi;
import com.teamone.e_tour.databinding.FragmentCheckoutBinding;
import com.teamone.e_tour.entities.Ticket;
import com.teamone.e_tour.entities.Voucher;
import com.teamone.e_tour.models.BookingDataManager;
import com.teamone.e_tour.models.VoucherManager;

import java.util.ArrayList;

public class CheckoutFragment extends Fragment {

    public CheckoutFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        BookTicketFragment.viewSecondTab();
        FragmentCheckoutBinding binding = FragmentCheckoutBinding.inflate(inflater, container, false);

        binding.totalPrice.setText("VND " + BookingDataManager.getInstance().getTotalPrice());

        binding.usingCashCard.setOnClickListener(v -> {
            binding.radioGroup.check(binding.usingCash.getId());
        });

        binding.usingCreditCardCard.setOnClickListener(v -> {
            binding.radioGroup.check(binding.usingCard.getId());
        });

        binding.voucherNum.setText(requireActivity().getString(R.string.you_have_1_voucher, 0));

        VoucherManager.getInstance().getSavedList().observe(getViewLifecycleOwner(), vouchers -> {
            if (vouchers == null) {
                binding.voucherNum.setText(requireActivity().getString(R.string.you_have_1_voucher, 0));
            } else {
                binding.voucherNum.setText(requireActivity().getString(R.string.you_have_1_voucher, vouchers.size()));
            }
        });

        binding.voucherName.setText("No voucher are currently being used");
        binding.voucherName.setTextColor(requireActivity().getColor(R.color.title_gray));
        binding.voucherNameDescription.setText("");
        VoucherManager.getInstance().getSelectVoucher().observe(getViewLifecycleOwner(), voucher -> {
            if (voucher == null) {
                binding.voucherName.setText("No voucher are currently being used");
                binding.voucherName.setTextColor(requireActivity().getColor(R.color.title_gray));
                binding.voucherNameDescription.setText("");
            } else {
                binding.totalPrice.setText("VND " + BookingDataManager.getInstance().getTotalPrice());

                binding.voucherName.setText(voucher.getName());
                binding.voucherName.setTextColor(requireActivity().getColor(R.color.black));
                binding.voucherNameDescription.setText(voucher.getDescription());
            }
        });

        binding.changeBtn.setOnClickListener(v -> {
            Navigation.findNavController(requireActivity(), R.id.home_wrapper).navigate(R.id.action_bookTicketFragment_to_chooseVoucher);
        });

        binding.checkoutBtn.setOnClickListener(v -> {
            BookingDataManager.getInstance().setErrorMessage("");
            BookingDataManager.getInstance().setBookedTicket((Ticket) null);
            BookTicketApi.getInstance(getActivity()).send(BookingDataManager.getInstance().getTicketData());
            NavHostFragment.findNavController(CheckoutFragment.this).navigate(R.id.action_checkoutFragment_to_receiptFragment);
        });

        return binding.getRoot();
    }
}