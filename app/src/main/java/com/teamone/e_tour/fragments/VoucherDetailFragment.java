package com.teamone.e_tour.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.teamone.e_tour.R;
import com.teamone.e_tour.api.voucher.SavedVoucher;
import com.teamone.e_tour.databinding.FragmentVoucherDetailBinding;
import com.teamone.e_tour.entities.Image;
import com.teamone.e_tour.models.CredentialToken;
import com.teamone.e_tour.utils.Formatter;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VoucherDetailFragment extends Fragment {

    public VoucherDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentVoucherDetailBinding binding = FragmentVoucherDetailBinding.inflate(inflater, container, false);

        requireActivity().findViewById(R.id.bottom_navigation).setVisibility(View.INVISIBLE);
        requireActivity().findViewById(R.id.home_wrapper).setPadding(0, 0, 0, 0);

        binding.topAppBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavHostFragment.findNavController(VoucherDetailFragment.this).popBackStack();
            }
        });

        Bundle bundle = getArguments();

        assert bundle != null;
        binding.voucherName.setText(bundle.getString("name"));
        binding.voucherDescription.setText(bundle.getString("description"));
        binding.usingCondition.setText(bundle.getString("usingCondition"));
        binding.expiredDate.setText(bundle.getString("expiredAt"));
        float value = bundle.getFloat("value", 0);
        float min = bundle.getFloat("min", 0);
        float max = bundle.getFloat("min", 0);
        String type = bundle.getString("type");

        if (type.equals("money"))
            binding.voucherValue.setText(requireActivity().getString(R.string.discount_money_message, Formatter.toCurrency((long) value), Formatter.toCurrency((long) min)));
        else {
            String s = (int)(value * 100) + "%";
            if (type.equals("percent"))
                binding.voucherValue.setText(requireActivity().getString(R.string.discount_percent_message, s, Formatter.toCurrency((long) min), Formatter.toCurrency((long) max)));
            else
                binding.voucherValue.setText(requireActivity().getString(R.string.discount_money_message, Formatter.toCurrency((long) value), Formatter.toCurrency((long) min)));
        }

        Glide.with(requireActivity()).load(new Image(bundle.getString("image")).getImageUri()).into(binding.voucherImage);

        binding.companyName.setText(bundle.getString("companyName"));
        binding.companyDescription.setText(bundle.getString("companyEmail"));
        Glide.with(requireActivity()).load(new Image(bundle.getString("companyImage")).getImageUri()).into(binding.companyImage);

        binding.companyCard.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.companyDetail);
        });

        binding.saveVoucherBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SavedVoucher.api.addToSavedList(new SavedVoucher.AddToSavedListBody(bundle.getString("id")), CredentialToken.getInstance(requireActivity()).getBearerAccessToken()).enqueue(new Callback<SavedVoucher.AddToSavedListResponse>() {
                    @Override
                    public void onResponse(Call<SavedVoucher.AddToSavedListResponse> call, Response<SavedVoucher.AddToSavedListResponse> response) {
                        if (getActivity() == null) return;

                        if (response.code() == 200) {
                            Toast.makeText(requireActivity(), "Successfully save this voucher", Toast.LENGTH_SHORT).show();
                        } else {
                            try {
                                assert response.errorBody() != null;
                                Toast.makeText(requireActivity(), "Error " + response.code() + " - " + response.errorBody().string(), Toast.LENGTH_SHORT).show();
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<SavedVoucher.AddToSavedListResponse> call, Throwable t) {
                        if (getActivity() == null) return;

                        Toast.makeText(requireActivity(), "App error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        return binding.getRoot();
    }
}