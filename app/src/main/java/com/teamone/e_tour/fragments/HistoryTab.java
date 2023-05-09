package com.teamone.e_tour.fragments;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.teamone.e_tour.R;
import com.teamone.e_tour.adapters.BookedTicketAdapter;
import com.teamone.e_tour.api.ticket.ViewBookedTicketApi;
import com.teamone.e_tour.databinding.FragmentHistoryTabBinding;
import com.teamone.e_tour.dialogs.LoadingDialog;
import com.teamone.e_tour.models.BookedTicketManager;

import java.util.ArrayList;

public class HistoryTab extends Fragment {

    public HistoryTab() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        BookedTicketManager.getInstance((AppCompatActivity) getActivity()).viewBookedTickets();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentHistoryTabBinding binding = FragmentHistoryTabBinding.inflate(inflater, container, false);

        BookedTicketAdapter adapter = new BookedTicketAdapter(this);
        LoadingDialog dialog = new LoadingDialog(getActivity());
        dialog.showLoading("Loading data");
        BookedTicketManager.getInstance((AppCompatActivity) getActivity()).getBookedTickets().observe(getViewLifecycleOwner(), new Observer<ArrayList<ViewBookedTicketApi.ResponseData.Ticket>>() {
            @Override
            public void onChanged(ArrayList<ViewBookedTicketApi.ResponseData.Ticket> tickets) {
                if (tickets == null) return;
                dialog.dismiss();
                adapter.setTickets(tickets);
            }
        });

        binding.bookedTicketList.setAdapter(adapter);
        binding.bookedTicketList.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));

        return binding.getRoot();
    }
}