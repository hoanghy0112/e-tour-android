package com.teamone.e_tour.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.teamone.e_tour.R;
import com.teamone.e_tour.databinding.FragmentInputInformationBinding;
import com.teamone.e_tour.models.BookingDataManager;

public class InputInformationFragment extends Fragment {

    public InputInformationFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        FragmentInputInformationBinding binding = FragmentInputInformationBinding.inflate(inflater, container, false);

        String fullName =BookingDataManager.getInstance().getTicketData().ticketInfo.getFullName();
        binding.contactInformation.setText(fullName.length() != 0 ? fullName : getActivity().getResources().getString(R.string.please_enter_contact_information));

        binding.visitorBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(getActivity(), R.id.home_wrapper).navigate(R.id.action_bookTicketFragment_to_visitorInformationFragment2);
            }
        });

        binding.contactInformation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(getActivity(), R.id.home_wrapper).navigate(R.id.action_bookTicketFragment_to_contactInformationFragment);
            }
        });

        binding.nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavHostFragment.findNavController(InputInformationFragment.this).navigate(R.id.action_inputInformationFragment_to_checkoutFragment);
            }
        });

        BookTicketFragment.viewFirstTab();

        return binding.getRoot();
    }
}