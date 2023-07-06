package com.teamone.e_tour.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.teamone.e_tour.R;
import com.teamone.e_tour.api.paymentCard.PaymentCardApi;
import com.teamone.e_tour.databinding.FragmentPaymentCardDetailBinding;
import com.teamone.e_tour.dialogs.LoadingDialog;
import com.teamone.e_tour.entities.PaymentCard;
import com.teamone.e_tour.models.CredentialToken;
import com.teamone.e_tour.models.PaymentCardManager;
import com.teamone.e_tour.utils.Formatter;

import java.io.IOException;
import java.util.Objects;
import java.util.Optional;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PaymentCardDetail extends Fragment {
    String id;

    public PaymentCardDetail() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            id = getArguments().getString("id");
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentPaymentCardDetailBinding binding = FragmentPaymentCardDetailBinding.inflate(inflater);

        PaymentCardManager.getInstance().getCardList().observe(getViewLifecycleOwner(), paymentCards -> {
            if (paymentCards == null) return;
            Optional<PaymentCard> paymentCardOptional = paymentCards.stream().filter(card -> card._id.equals(id)).findFirst();
            if (!paymentCardOptional.isPresent()) return;

            PaymentCard paymentCard = paymentCardOptional.get();

            binding.cardNumber.setText(paymentCard.cardNumber);
            binding.expiredDate.setText(Formatter.dateToCardEXP(paymentCard.expiredDate));
            binding.cardName.setText(paymentCard.name);

            binding.backBtn.setOnClickListener(v -> {
                Navigation.findNavController(v).popBackStack();
            });

            LoadingDialog dialog = new LoadingDialog(requireContext());

            binding.updateBtn.setOnClickListener(v -> {
                Bundle bundle = new Bundle();
                bundle.putString("id", paymentCard._id);
                Navigation.findNavController(v).navigate(R.id.action_paymentCardDetail_to_addPaymentCard, bundle);
            });

            binding.makeDefaultBtn.setOnClickListener(v -> {
                dialog.showLoading("Loading...");

                PaymentCardApi.api.makeDefault(CredentialToken.getInstance(getContext()).getBearerAccessToken(), new PaymentCardApi.MakeDefaultBody(paymentCard._id)).enqueue(new Callback<PaymentCardApi.UpdatedResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<PaymentCardApi.UpdatedResponse> call, @NonNull Response<PaymentCardApi.UpdatedResponse> response) {
                        dialog.dismiss();
                        if (getActivity() == null) return;

                        if (response.code() == 200) {
                            Toast.makeText(requireActivity(), "Successfully make as default", Toast.LENGTH_SHORT).show();
                            Navigation.findNavController(v).navigate(R.id.viewAllCardFragment);
                        } else {
                            assert response.errorBody() != null;
                            try {
                                Toast.makeText(requireActivity(), "Error: " + response.code() + " - " + response.errorBody().string(), Toast.LENGTH_SHORT).show();
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<PaymentCardApi.UpdatedResponse> call, @NonNull Throwable t) {
                        dialog.dismiss();
                        if (getActivity() == null) return;

                        Toast.makeText(requireActivity(), "App error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            });

            binding.removeButton.setOnClickListener(v -> {
                dialog.showLoading("Loading...");

                PaymentCardApi.api.deleteCard(CredentialToken.getInstance(getContext()).getBearerAccessToken(), paymentCard._id).enqueue(new Callback<PaymentCardApi.UpdatedResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<PaymentCardApi.UpdatedResponse> call, @NonNull Response<PaymentCardApi.UpdatedResponse> response) {
                        dialog.dismiss();
                        if (getActivity() == null) return;

                        if (response.code() == 200) {
                            Toast.makeText(requireActivity(), "Successfully remove card", Toast.LENGTH_SHORT).show();
                            Navigation.findNavController(v).navigate(R.id.viewAllCardFragment);
                        } else {
                            assert response.errorBody() != null;
                            try {
                                Toast.makeText(requireActivity(), "Error: " + response.code() + " - " + response.errorBody().string(), Toast.LENGTH_SHORT).show();
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<PaymentCardApi.UpdatedResponse> call, @NonNull Throwable t) {
                        dialog.dismiss();
                        if (getActivity() == null) return;

                        Toast.makeText(requireActivity(), "App error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            });
        });

        return binding.getRoot();
    }
}