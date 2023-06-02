package com.teamone.e_tour.fragments;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.teamone.e_tour.R;
import com.teamone.e_tour.adapters.TicketVisitorAdapter;
import com.teamone.e_tour.api.ticket.UpdateTicketApi;
import com.teamone.e_tour.api.ticket.ViewBookedTicketApi;
import com.teamone.e_tour.databinding.FragmentVisitorInformationDetailBinding;
import com.teamone.e_tour.models.BookedTicketManager;

import java.util.ArrayList;
import java.util.NoSuchElementException;

public class VisitorInformationDetail extends Fragment {

    private String ticketId;
    private ViewBookedTicketApi.ResponseData.Ticket ticket;


    public VisitorInformationDetail() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            ticketId = getArguments().getString("id");
        }
        UpdateTicketApi.getInstance(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        FragmentVisitorInformationDetailBinding binding = FragmentVisitorInformationDetailBinding.inflate(inflater, container, false);

        BookedTicketManager.getInstance((AppCompatActivity) getActivity()).getBookedTickets().observe(getViewLifecycleOwner(), new Observer<ArrayList<ViewBookedTicketApi.ResponseData.Ticket>>() {
            @Override
            public void onChanged(ArrayList<ViewBookedTicketApi.ResponseData.Ticket> tickets) {
                if (tickets == null) return;
                if (ticketId == null) return;
                if (ticket != null) return;

                try {
                    ticket = tickets.stream().filter(ticket_ -> ticket_._id.equals(ticketId)).findFirst().get();

                    TicketVisitorAdapter adapter = new TicketVisitorAdapter(VisitorInformationDetail.this);
                    adapter.setVisitors(ticket.visitors);

                    binding.pickupLocation.getEditText().setText(ticket.pickupLocation);
                    binding.specialRequirements.getEditText().setText(ticket.specialRequirement);
                    binding.visitorList.setAdapter(adapter);
                    binding.visitorList.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
                    binding.visitorList.setNestedScrollingEnabled(false);
                } catch (NoSuchElementException e) {

                }

            }
        });

        binding.pickupLocation.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                ticket.pickupLocation = String.valueOf(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        binding.specialRequirements.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                ticket.specialRequirement = String.valueOf(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        binding.saveChangesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateTicketApi.getInstance(getActivity()).send(new UpdateTicketApi.RequestBody(new UpdateTicketApi.RequestBody.TicketInfo(ticket.tourId._id, ticket.fullName, ticket.email, ticket.phoneNumber, ticket.visitors, ticket.pickupLocation, ticket.specialRequirement), ticketId));
            }
        });

        UpdateTicketApi.getInstance(getActivity()).getResponseData().observe(getViewLifecycleOwner(), new Observer<UpdateTicketApi.ResponseData>() {
            @Override
            public void onChanged(UpdateTicketApi.ResponseData responseData) {
                if (responseData != null)
                    if (responseData.status == 200) {
                        Toast.makeText(getActivity(), "Success", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getActivity(), "Fail", Toast.LENGTH_SHORT).show();
                    }
            }
        });

        return binding.getRoot();
    }
}