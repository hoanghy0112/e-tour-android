package com.teamone.e_tour.fragments;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.teamone.e_tour.R;
import com.teamone.e_tour.api.ticket.ViewBookedTicketApi;
import com.teamone.e_tour.databinding.FragmentDetailIncomingTourBinding;
import com.teamone.e_tour.entities.Image;
import com.teamone.e_tour.models.BookedTicketManager;
import com.teamone.e_tour.utils.Formatter;

import java.util.ArrayList;
import java.util.Date;

public class DetailIncomingTour extends Fragment {
    private String ticketId;

    public DetailIncomingTour() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            ticketId = getArguments().getString("id");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentDetailIncomingTourBinding binding = FragmentDetailIncomingTourBinding.inflate(inflater, container, false);

        getActivity().findViewById(R.id.bottom_navigation).setVisibility(View.INVISIBLE);
        getActivity().findViewById(R.id.home_wrapper).setPadding(0, 0, 0, 0);


            BookedTicketManager.getInstance((AppCompatActivity) getActivity()).getBookedTickets().observe(getViewLifecycleOwner(), new Observer<ArrayList<ViewBookedTicketApi.ResponseData.Ticket>>() {
                @Override
                public void onChanged(ArrayList<ViewBookedTicketApi.ResponseData.Ticket> tickets) {
                    if (tickets == null) return;
                    if (ticketId == null) return;
                    ViewBookedTicketApi.ResponseData.Ticket ticket = tickets.stream().filter(ticket_ -> ticket_._id.equals(ticketId)).findFirst().get();

                    binding.tourName.setText(ticket.tourId.name);
                    binding.tourDescription.setText(ticket.tourId.description);
                    binding.routeName.setText(ticket.tourId.touristRoute.name);
                    binding.destinationRoute.setText(String.join(" - ", ticket.tourId.touristRoute.route));
                    binding.departureTime.setText(Formatter.dateToDayString(ticket.tourId.from));

                    if (ticket.tourId.image != null && !ticket.tourId.image.equals("")) {
                        Glide.with(getActivity()).load(new Image(ticket.tourId.image).getImageUri()).into(binding.tourImage);
                    }

                    binding.routeName.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Bundle bundle = new Bundle();
                            bundle.putString("id", ticket.tourId.touristRoute._id);
                            Navigation.findNavController(getActivity(), R.id.home_wrapper).navigate(R.id.detailTourFragment, bundle);
                        }
                    });

                    binding.visitorCard.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Bundle bundle = new Bundle();
                            bundle.putString("id", ticketId);
                            Navigation.findNavController(getActivity(), R.id.home_wrapper).navigate(R.id.visitorInformationDetail, bundle);
                        }
                    });
                    binding.contactCard.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                        }
                    });
                    binding.reportCard.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Bundle bundle = new Bundle();
                            bundle.putString("id", ticket.tourId._id);
                            Navigation.findNavController(getActivity(), R.id.home_wrapper).navigate(R.id.reportTour, bundle);
                        }
                    });

                    binding.discardTicket.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                        }
                    });
                }
            });

        return binding.getRoot();
    }
}