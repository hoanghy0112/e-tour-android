package com.teamone.e_tour.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.teamone.e_tour.R;
import com.teamone.e_tour.databinding.FragmentVisitorInformationBinding;
import com.teamone.e_tour.entities.Ticket;
import com.teamone.e_tour.models.BookingDataManager;

public class VisitorInformationFragment extends Fragment {

    public VisitorInformationFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentVisitorInformationBinding binding = FragmentVisitorInformationBinding.inflate(inflater, container, false);

        assert getArguments() != null;
        int index = getArguments().getInt("index");
        Ticket.Visitor visitor = BookingDataManager.getInstance().getTicketData().ticketInfo.getVisitors().get(index);

        binding.name.setText(visitor.getName());
        binding.age.setText(String.valueOf(visitor.getAge()));
        binding.phoneNumber.setText(visitor.getPhoneNumber());
        binding.address.setText(visitor.getAddress());


        binding.nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int index = getArguments().getInt("index");

                String name = binding.name.getText().toString();
                int age = Integer.parseInt(binding.age.getText().toString());
                String address = binding.address.getText().toString();
                String phoneNumber = binding.phoneNumber.getText().toString();
                BookingDataManager.getInstance().changeVisitorInfo(index, new Ticket.Visitor(name, age, address, phoneNumber));

//                NavHostFragment.findNavController(VisitorInformationFragment.this).navigate(R.id.action_visitorInformationFragment2_to_bookTicketFragment);
                NavHostFragment.findNavController(VisitorInformationFragment.this).popBackStack();
            }
        });

        binding.removeVisitorBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int index = getArguments().getInt("index");
                BookingDataManager.getInstance().removeVisitor(index);

                NavHostFragment.findNavController(VisitorInformationFragment.this).popBackStack();
            }
        });

        binding.topAppBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavHostFragment.findNavController(VisitorInformationFragment.this).popBackStack();
            }
        });

        return binding.getRoot();
    }
}