package com.teamone.e_tour.fragments;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.teamone.e_tour.R;
import com.teamone.e_tour.api.ticket.BookTicketApi;
import com.teamone.e_tour.api.voucher.SavedVoucher;
import com.teamone.e_tour.constants.ApiEndpoint;
import com.teamone.e_tour.databinding.FragmentBookTicketBinding;
import com.teamone.e_tour.models.BookingDataManager;
import com.teamone.e_tour.models.CredentialToken;
import com.teamone.e_tour.models.VoucherManager;
import com.teamone.e_tour.utils.Formatter;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BookTicketFragment extends Fragment {

    static FragmentBookTicketBinding binding;
    static Activity context;

    public BookTicketFragment() {
        // Required empty public constructor
    }

    public static FragmentBookTicketBinding getBinding() {
        return binding;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SavedVoucher.api.viewSavedVoucher(CredentialToken.getInstance(requireActivity()).getBearerAccessToken()).enqueue(new Callback<SavedVoucher.ViewSavedVoucherResponse>() {
            @Override
            public void onResponse(@NonNull Call<SavedVoucher.ViewSavedVoucherResponse> call, @NonNull Response<SavedVoucher.ViewSavedVoucherResponse> response) {
                if (response.code() == 200) {
                    assert response.body() != null;
                    VoucherManager.getInstance().setSavedList(response.body().data);
                }
            }

            @Override
            public void onFailure(@NonNull Call<SavedVoucher.ViewSavedVoucherResponse> call, @NonNull Throwable t) {

            }
        });
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentBookTicketBinding.inflate(inflater, container, false);
        context = getActivity();

        BookTicketApi.RequestBody.TicketInfo ticketInfo = BookingDataManager.getInstance().getTicketData().ticketInfo;
        binding.departureTime.setText(Formatter.dateToDateWithoutHourString(BookingDataManager.getInstance().getDepartureDate()));
        binding.tourName.setText(BookingDataManager.getInstance().getTourName());
        binding.description.setText(BookingDataManager.getInstance().getDescription());
        binding.routeName.setText(BookingDataManager.getInstance().getRouteName());

        Glide.with(requireActivity()).load(BookingDataManager.getInstance().getImageUri()).into(binding.imageView);

        binding.backBtn.setOnClickListener(v -> Navigation.findNavController(requireActivity(), R.id.home_wrapper).popBackStack());

        return binding.getRoot();
    }

    public static void viewFirstTab() {
        Drawable active_icon = ContextCompat.getDrawable(context, R.drawable.number_circle);
        Drawable inactive_icon = ContextCompat.getDrawable(context, R.drawable.number_circle_unfocus);

        int colorActive = context.getResources().getColor(R.color.white, new ContextThemeWrapper(context, R.style.Theme_ETour).getTheme());
        int colorInActive = context.getResources().getColor(R.color.blue_white, new ContextThemeWrapper(context, R.style.Theme_ETour).getTheme());

        binding.num1.setBackground(active_icon);
        binding.num2.setBackground(inactive_icon);
        binding.num2.setBackground(inactive_icon);

        binding.text1.setTextColor(colorActive);
        binding.text2.setTextColor(colorInActive);
        binding.text3.setTextColor(colorInActive);

        binding.topAppBarTitle.setText(R.string.visitor_information);
    }

    public static void viewSecondTab() {
        Drawable active_icon = ContextCompat.getDrawable(context, R.drawable.number_circle);
        Drawable inactive_icon = ContextCompat.getDrawable(context, R.drawable.number_circle_unfocus);

        int colorActive = context.getResources().getColor(R.color.white, new ContextThemeWrapper(context, R.style.Theme_ETour).getTheme());
        int colorInActive = context.getResources().getColor(R.color.blue_white, new ContextThemeWrapper(context, R.style.Theme_ETour).getTheme());

        binding.num1.setBackground(inactive_icon);
        binding.num2.setBackground(active_icon);
        binding.num3.setBackground(inactive_icon);

        binding.text1.setTextColor(colorInActive);
        binding.text2.setTextColor(colorActive);
        binding.text3.setTextColor(colorInActive);

        binding.topAppBarTitle.setText(R.string.choose_payment_method_and_checkout);
    }

    public static void viewThirdTab() {
        Drawable active_icon = ContextCompat.getDrawable(context, R.drawable.number_circle);
        Drawable inactive_icon = ContextCompat.getDrawable(context, R.drawable.number_circle_unfocus);

        int colorActive = context.getResources().getColor(R.color.white, new ContextThemeWrapper(context, R.style.Theme_ETour).getTheme());
        int colorInActive = context.getResources().getColor(R.color.blue_white, new ContextThemeWrapper(context, R.style.Theme_ETour).getTheme());

        binding.num1.setBackground(inactive_icon);
        binding.num2.setBackground(inactive_icon);
        binding.num3.setBackground(active_icon);

        binding.text1.setTextColor(colorInActive);
        binding.text2.setTextColor(colorInActive);
        binding.text3.setTextColor(colorActive);

        binding.topAppBarTitle.setText(R.string.receipt_information);
    }
}