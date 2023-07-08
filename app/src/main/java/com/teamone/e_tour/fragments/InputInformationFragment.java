package com.teamone.e_tour.fragments;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.teamone.e_tour.R;
import com.teamone.e_tour.adapters.VisitorAdapter;
import com.teamone.e_tour.api.ticket.BookTicketApi;
import com.teamone.e_tour.databinding.FragmentInputInformationBinding;
import com.teamone.e_tour.dialogs.LoadingDialog;
import com.teamone.e_tour.models.BookingDataManager;

public class InputInformationFragment extends Fragment {
    boolean isSpecialRequirementVisible = true;

    public InputInformationFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        FragmentInputInformationBinding binding = FragmentInputInformationBinding.inflate(inflater, container, false);

        String fullName = BookingDataManager.getInstance().getTicketData().ticketInfo.getFullName();
        binding.contactInformation.setText(fullName != null && fullName.length() != 0 ? fullName : requireActivity().getString(R.string.please_enter_contact_information));

        binding.contactInformation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(requireActivity(), R.id.home_wrapper).navigate(R.id.action_bookTicketFragment_to_contactInformationFragment);
            }
        });

        binding.nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavHostFragment.findNavController(InputInformationFragment.this).navigate(R.id.action_inputInformationFragment_to_checkoutFragment);
            }
        });

        ((EditText) binding.specialRequirementEdit).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                BookingDataManager.getInstance().getTicketData().ticketInfo.setSpecialRequirement(String.valueOf(s));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        ((EditText) binding.pickupLocationEdit).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                BookingDataManager.getInstance().getTicketData().ticketInfo.setPickupLocation(String.valueOf(s));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        binding.makeYouAsVisitor.setOnClickListener(v -> {
            BookingDataManager.getInstance().addUserAsVisitor(requireActivity());
        });

        binding.addNewVisitor.setOnClickListener(v -> BookingDataManager.getInstance().addNewVisitor());

        VisitorAdapter adapter = new VisitorAdapter(getActivity());
        binding.visitorList.setAdapter(adapter);
        binding.visitorList.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });

        BookingDataManager.getInstance().getTicketLiveData().observe(getViewLifecycleOwner(), requestBody -> {
            adapter.setVisitors(requestBody.ticketInfo.getVisitors());
            if (requestBody.ticketInfo.getVisitors().size() > 0) {
                binding.makeYouAsVisitor.setVisibility(View.INVISIBLE);
                binding.makeYouAsVisitor.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0));
            } else {
                binding.makeYouAsVisitor.setVisibility(View.VISIBLE);
                binding.makeYouAsVisitor.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            }
            BookingDataManager.getInstance().setNumOfVisitor(requestBody.ticketInfo.getVisitors().size());
            binding.totalPrice.setText("VND " + String.valueOf(BookingDataManager.getInstance().getNumOfVisitor() * BookingDataManager.getInstance().getPrice()));
        });


        BookingDataManager.getInstance().getErrorMessage().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                if (s.length() == 0) return;
                LoadingDialog dialog = new LoadingDialog(requireActivity());
                dialog.showError(s).setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        BookingDataManager.getInstance().setErrorMessage("");
                    }
                });
            }
        });

        BookTicketFragment.viewFirstTab();

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}