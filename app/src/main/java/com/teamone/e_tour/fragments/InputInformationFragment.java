package com.teamone.e_tour.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.teamone.e_tour.R;
import com.teamone.e_tour.adapters.VisitorAdapter;
import com.teamone.e_tour.api.ticket.BookTicketApi;
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
        binding.contactInformation.setText(fullName != null && fullName.length() != 0 ? fullName : getActivity().getResources().getString(R.string.please_enter_contact_information));

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

        binding.addNewVisitor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BookingDataManager.getInstance().addNewVisitor();
            }
        });

        VisitorAdapter adapter = new VisitorAdapter(getActivity());
        binding.visitorList.setAdapter(adapter);
        binding.visitorList.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));

        BookingDataManager.getInstance().getTicketLiveData().observe(getViewLifecycleOwner(), new Observer<BookTicketApi.RequestBody>() {
            @Override
            public void onChanged(BookTicketApi.RequestBody requestBody) {
                adapter.setVisitors(requestBody.ticketInfo.getVisitors());
            }
        });

        BookTicketFragment.viewFirstTab();

        return binding.getRoot();
    }
}