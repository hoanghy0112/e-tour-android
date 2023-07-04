package com.teamone.e_tour.fragments;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.textfield.TextInputEditText;
import com.teamone.e_tour.R;
import com.teamone.e_tour.api.rate.CreateNewRateApi;
import com.teamone.e_tour.api.ticket.ViewBookedTicketApi;
import com.teamone.e_tour.databinding.FragmentDetailIncomingTourBinding;
import com.teamone.e_tour.databinding.FragmentDetailVisitedTourBinding;
import com.teamone.e_tour.databinding.FragmentReportTourBinding;
import com.teamone.e_tour.databinding.ReportResultBinding;
import com.teamone.e_tour.entities.Image;
import com.teamone.e_tour.entities.Rating;
import com.teamone.e_tour.entities.UserProfile;
import com.teamone.e_tour.models.BookedTicketManager;
import com.teamone.e_tour.models.RatingManager;
import com.teamone.e_tour.models.UserProfileManager;
import com.teamone.e_tour.utils.Formatter;

import java.util.ArrayList;
import java.util.Date;

public class DetailVisitedTour extends Fragment {
    private String ticketId;
    View viewDialog;
    BottomSheetDialog bottomSheetDialog;

    public DetailVisitedTour() {
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
        FragmentDetailVisitedTourBinding binding = FragmentDetailVisitedTourBinding.inflate(inflater, container, false);

        requireActivity().findViewById(R.id.bottom_navigation).setVisibility(View.INVISIBLE);
        requireActivity().findViewById(R.id.home_wrapper).setPadding(0, 0, 0, 0);


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
                    Glide.with(requireActivity()).load(new Image(ticket.tourId.image).getImageUri()).into(binding.tourImage);
                }

                binding.routeName.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Bundle bundle = new Bundle();
                        bundle.putString("id", ticket.tourId.touristRoute._id);
                        Navigation.findNavController(requireActivity(), R.id.home_wrapper).navigate(R.id.detailTourFragment, bundle);
                    }
                });
                binding.rateCard.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        RatingManager.getInstance((AppCompatActivity) getActivity()).viewRating(ticket.tourId.touristRoute._id);

                        RatingManager.getInstance((AppCompatActivity) getActivity()).getRating().observe(getViewLifecycleOwner(), new Observer<ArrayList<Rating>>() {
                            @Override
                            public void onChanged(ArrayList<Rating> ratings) {
                                if (ratings == null) return;

                                String uid = UserProfileManager.getInstance(getActivity()).getUserProfile().get_id();
                                if (ratings.stream().noneMatch(rating_ -> rating_.getUserId().get_id().equals(uid))) {
                                    displayEditRate("", 0, ticket.tourId.touristRoute._id);
                                    return;
                                }
                                Rating rating = ratings.stream().filter(rating_ -> rating_.getUserId().get_id().equals(uid)).findFirst().get();
                                boolean isComment = ratings.stream().anyMatch(rating_ -> rating_.getUserId().get_id().equals(uid));

                                if (isComment) {
                                    displayRateDetail(rating.getDescription(), rating.getStar(), ticket.tourId.touristRoute._id);
                                }
                            }
                        });
                    }
                });
                binding.reportCard.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        Bundle bundle = new Bundle();
//                        bundle.putString("id", ticket.tourId._id);
//                        Navigation.findNavController(getActivity(), R.id.home_wrapper).navigate(R.id.reportTour, bundle);
                        displayReportBottomSheet(ticket.tourId._id);
                    }
                });
            }
        });
        bottomSheetDialog = new BottomSheetDialog(DetailVisitedTour.this.getActivity(), R.style.SheetDialog);

        return binding.getRoot();
    }

    private void displayRateDetail(String commentTextString, float star, String routeId) {
        viewDialog = getLayoutInflater().inflate(R.layout.fragment_rate_tour, null);
        ImageView avatar = viewDialog.findViewById(R.id.avatar);
        TextView displayName = viewDialog.findViewById(R.id.user_display_name);
        UserProfile userProfile = UserProfileManager.getInstance(getActivity()).getUserProfile();
        Glide.with(getActivity()).load(userProfile.getImage()).into(avatar);
        displayName.setText(userProfile.getFullName());
        TextView commentText = viewDialog.findViewById(R.id.comment_text);
        RatingBar ratingBar = viewDialog.findViewById(R.id.ratingBar);
        commentText.setText(commentTextString);
        ratingBar.setRating(star);
        Button editBtn = viewDialog.findViewById(R.id.edit_btn);
        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayEditRate(commentTextString, star, routeId);
            }
        });
        bottomSheetDialog.setContentView(viewDialog);
        if (!bottomSheetDialog.isShowing()) bottomSheetDialog.show();
    }

    private void displayEditRate(String commentTextString, float star, String routeId) {
        viewDialog = getLayoutInflater().inflate(R.layout.edit_rate_tour_bottom_sheet, null);
        ImageView avatar = viewDialog.findViewById(R.id.avatar);
        TextView displayName = viewDialog.findViewById(R.id.user_display_name);
        UserProfile userProfile = UserProfileManager.getInstance(getActivity()).getUserProfile();
        Glide.with(getActivity()).load(userProfile.getImage()).into(avatar);
        displayName.setText(userProfile.getFullName());
        RatingBar rating = viewDialog.findViewById(R.id.rating_bar);
        rating.setRating(star);
        TextInputEditText comment = viewDialog.findViewById(R.id.comment_text);
        comment.setText(commentTextString);
        Button sendBtn = viewDialog.findViewById(R.id.submit_btn);
        Button backBtn = viewDialog.findViewById(R.id.back_btn);
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RatingBar rating = viewDialog.findViewById(R.id.rating_bar);
                TextInputEditText comment = viewDialog.findViewById(R.id.comment_text);
                CreateNewRateApi api = new CreateNewRateApi(getActivity());
                api.send(rating.getRating(), comment.getText().toString(), routeId);
                displayRateDetail(comment.getText().toString(), rating.getRating(), routeId);
            }
        });
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayRateDetail(commentTextString, star, routeId);
            }
        });
        bottomSheetDialog.setContentView(viewDialog);
        if (!bottomSheetDialog.isShowing()) bottomSheetDialog.show();
    }

    void submitReport(String type) {
        ReportResultBinding binding = ReportResultBinding.inflate(getLayoutInflater(), null, false);

        bottomSheetDialog.setContentView(binding.getRoot());
        if (!bottomSheetDialog.isShowing()) bottomSheetDialog.show();
    }

    void displayReportBottomSheet(String tourId) {
        FragmentReportTourBinding binding = FragmentReportTourBinding.inflate(getLayoutInflater(), null, false);
        binding.scam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitReport("scam");
            }
        });
        binding.fakeTour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitReport("fake tour");
            }
        });
        binding.violatePolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitReport("violate policy");
            }
        });
        bottomSheetDialog.setContentView(binding.getRoot());
        if (!bottomSheetDialog.isShowing()) bottomSheetDialog.show();
    }
}