package com.teamone.e_tour.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.teamone.e_tour.R;
import com.teamone.e_tour.api.rate.CreateNewRateApi;
import com.teamone.e_tour.api.ticket.ViewBookedTicketApi;
import com.teamone.e_tour.databinding.FragmentRateTourBinding;
import com.teamone.e_tour.entities.Image;
import com.teamone.e_tour.entities.Rating;
import com.teamone.e_tour.models.BookedTicketManager;
import com.teamone.e_tour.models.RatingManager;
import com.teamone.e_tour.models.UserProfileManager;
import com.teamone.e_tour.utils.Formatter;

import java.util.ArrayList;

public class RateTour extends Fragment {
    private String id;

    public RateTour() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.id = getArguments().getString("id");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentRateTourBinding binding = FragmentRateTourBinding.inflate(inflater, container, false);


        BookedTicketManager.getInstance((AppCompatActivity) getActivity()).getBookedTickets().observe(getViewLifecycleOwner(), new Observer<ArrayList<ViewBookedTicketApi.ResponseData.Ticket>>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onChanged(ArrayList<ViewBookedTicketApi.ResponseData.Ticket> tickets) {
                if (id == null) return;
                ViewBookedTicketApi.ResponseData.Ticket ticket = tickets.stream().filter(ticket_ -> ticket_._id == id).findFirst().get();

                RatingManager.getInstance((AppCompatActivity) getActivity()).viewRating(ticket.tourId.touristRoute._id);

                RatingManager.getInstance((AppCompatActivity) getActivity()).getRating().observe(getViewLifecycleOwner(), new Observer<ArrayList<Rating>>() {
                    @Override
                    public void onChanged(ArrayList<Rating> ratings) {
                        if (ratings == null) return;

                        String uid = UserProfileManager.getInstance(getActivity()).getUserProfile().get_id();
                        if (!ratings.stream().filter(rating_ -> rating_.getUserId().get_id().equals(uid)).findFirst().isPresent()) return;
                        Rating rating = ratings.stream().filter(rating_ -> rating_.getUserId().get_id().equals(uid)).findFirst().get();
                        boolean isComment = ratings.stream().anyMatch(rating_ -> rating_.getUserId().get_id().equals(uid));

//                        if (isComment) {
//                            View view = LayoutInflater.from(getActivity()).inflate(R.layout.your_rating, container, false);
//                            if (view.getParent() != null) {
//                                ((ViewGroup) view.getParent()).removeView(view);
//                            }
//                            ((TextView)view.findViewById(R.id.comment)).setText(rating.getDescription());
//                            ((RatingBar)view.findViewById(R.id.rating_bar)).setRating(rating.getStar());
//                            binding.yourRating.addView(view);
//                        }
                    }
                });
            }
        });

        return binding.getRoot();
    }
}