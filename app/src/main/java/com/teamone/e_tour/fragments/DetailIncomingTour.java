package com.teamone.e_tour.fragments;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.teamone.e_tour.R;
import com.teamone.e_tour.api.ticket.TicketAPI;
import com.teamone.e_tour.api.ticket.ViewBookedTicketApi;
import com.teamone.e_tour.databinding.FragmentDetailIncomingTourBinding;
import com.teamone.e_tour.databinding.FragmentReportTourBinding;
import com.teamone.e_tour.databinding.ReportResultBinding;
import com.teamone.e_tour.entities.ApiResponse;
import com.teamone.e_tour.entities.Image;
import com.teamone.e_tour.models.BookedTicketManager;
import com.teamone.e_tour.utils.Formatter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailIncomingTour extends Fragment {
    private String ticketId;
    BottomSheetDialog bottomSheetDialog;

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        FragmentDetailIncomingTourBinding binding = FragmentDetailIncomingTourBinding.inflate(inflater, container, false);
        bottomSheetDialog = new BottomSheetDialog(DetailIncomingTour.this.getActivity(), R.style.SheetDialog);

        getActivity().findViewById(R.id.bottom_navigation).setVisibility(View.INVISIBLE);
        getActivity().findViewById(R.id.home_wrapper).setPadding(0, 0, 0, 0);

        binding.topAppBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavHostFragment.findNavController(DetailIncomingTour.this).popBackStack();
            }
        });

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
                        Bundle bundle = new Bundle();
                        bundle.putString("id", ticketId);
                        bundle.putString("routeId", ticket.tourId.touristRoute._id);
                        Navigation.findNavController(getActivity(), R.id.home_wrapper).navigate(R.id.contactSupportFragment, bundle);
                    }
                });
                binding.reportCard.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        displayReportBottomSheet(ticket.tourId._id);
                    }
                });

                binding.discardTicket.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        TicketAPI.getApi(getActivity()).discardTicket(ticketId).enqueue(new Callback<ApiResponse>() {
                            @Override
                            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                                if (response.code() == 200) {
                                    Toast.makeText(getActivity(), "Successfully discard ticket", Toast.LENGTH_SHORT).show();
                                    Navigation.findNavController(getActivity(), R.id.home_wrapper).navigate(R.id.homeFragment);
                                } else {
                                    Toast.makeText(getActivity(), "Fail to discard ticket", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<ApiResponse> call, Throwable t) {
                            }
                        });
                    }
                });
            }
        });

        return binding.getRoot();
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