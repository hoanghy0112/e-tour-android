package com.teamone.e_tour.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.teamone.e_tour.R;
import com.teamone.e_tour.adapters.VoucherAdapter;
import com.teamone.e_tour.api.ticket.BookTicketApi;
import com.teamone.e_tour.databinding.FragmentChooseVoucherBinding;
import com.teamone.e_tour.models.BookedTicketManager;
import com.teamone.e_tour.models.BookingDataManager;
import com.teamone.e_tour.models.VoucherManager;

import java.util.ArrayList;

public class ChooseVoucher extends Fragment {

    public ChooseVoucher() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentChooseVoucherBinding binding = FragmentChooseVoucherBinding.inflate(inflater);

        VoucherAdapter adapter = new VoucherAdapter(this, R.layout.item_saved_voucher_preview, voucher -> {
            VoucherManager.getInstance().setSelectVoucher(voucher);
            BookTicketApi.RequestBody ticketData = BookingDataManager.getInstance().getTicketData();
            ticketData.voucherIds = new ArrayList<>();
            ticketData.voucherIds.add(voucher.get_id());
            BookingDataManager.getInstance().setTicketData(ticketData);
            Navigation.findNavController(requireActivity(), R.id.home_wrapper).popBackStack();
        });

        binding.voucherList.setAdapter(adapter);
        binding.voucherList.setLayoutManager(new LinearLayoutManager(requireActivity(), RecyclerView.VERTICAL, false));

        binding.backBtn.setOnClickListener(v -> {
            Navigation.findNavController(requireActivity(), R.id.home_wrapper).popBackStack();
        });

        VoucherManager.getInstance().getSavedList().observe(getViewLifecycleOwner(), adapter::setVouchers);

        return binding.getRoot();
    }
}