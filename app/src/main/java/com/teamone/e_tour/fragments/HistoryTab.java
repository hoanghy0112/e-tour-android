package com.teamone.e_tour.fragments;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
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
import java.util.Date;

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

        requireActivity().findViewById(R.id.bottom_navigation).setVisibility(View.VISIBLE);
        requireActivity().findViewById(R.id.home_wrapper).setPadding(0, 0, 0, 180);

        BookedTicketAdapter incomingTourAdapter = new BookedTicketAdapter(this);
        BookedTicketAdapter visitedTourAdapter = new BookedTicketAdapter(this);
        LoadingDialog dialog = new LoadingDialog(requireActivity());
        dialog.showLoading("Loading data");
        BookedTicketManager.getInstance((AppCompatActivity) getActivity()).getBookedTickets().observe(getViewLifecycleOwner(), new Observer<ArrayList<ViewBookedTicketApi.ResponseData.Ticket>>() {
            @Override
            public void onChanged(ArrayList<ViewBookedTicketApi.ResponseData.Ticket> tickets) {
                if (tickets == null) return;
                binding.swiperefresh.setRefreshing(false);
                dialog.dismiss();
                ArrayList<ViewBookedTicketApi.ResponseData.Ticket> incomingTourList = new ArrayList<>();
                ArrayList<ViewBookedTicketApi.ResponseData.Ticket> visitedTourList = new ArrayList<>();
                tickets.forEach(ticket -> {
                    if (ticket.tourId.from.before(new Date()))
                        visitedTourList.add(ticket);
                    else
                        incomingTourList.add(ticket);
                });
                incomingTourAdapter.setTickets(incomingTourList);
                visitedTourAdapter.setTickets(visitedTourList);
            }
        });

        binding.incomingTourList.setAdapter(incomingTourAdapter);
        binding.incomingTourList.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        binding.incomingTourList.setNestedScrollingEnabled(false);
        binding.visitedTourList.setAdapter(visitedTourAdapter);
        binding.visitedTourList.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        binding.visitedTourList.setNestedScrollingEnabled(false);

        binding.swiperefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                BookedTicketManager.getInstance((AppCompatActivity) getActivity()).viewBookedTickets();
            }
        });

        return binding.getRoot();
    }
}