package com.teamone.e_tour.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.teamone.e_tour.R;
import com.teamone.e_tour.api.paymentCard.PaymentCardApi;
import com.teamone.e_tour.databinding.FragmentAddPaymentCardBinding;
import com.teamone.e_tour.dialogs.LoadingDialog;
import com.teamone.e_tour.entities.PaymentCard;
import com.teamone.e_tour.models.CredentialToken;
import com.teamone.e_tour.models.PaymentCardManager;
import com.teamone.e_tour.utils.Formatter;

import java.io.IOException;
import java.text.ParseException;
import java.util.Objects;
import java.util.Optional;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddPaymentCard extends Fragment {
    String id;
    int previousCardNumberLength = 0;
    int previousExpLength = 0;
    LoadingDialog dialog;


    public AddPaymentCard() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            id = getArguments().getString("id");
        }
        dialog = new LoadingDialog(requireContext());
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentAddPaymentCardBinding binding = FragmentAddPaymentCardBinding.inflate(inflater);

        binding.backBtn.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.viewAllCardFragment);
        });

        if (id == null) {
            binding.textView11.setText(requireActivity().getString(R.string.add_new_card));
            binding.addBtn.setText(requireActivity().getString(R.string.add_new_card));

            binding.cardNumber.setText("");
            binding.expiredDate.setText("");
            binding.cvv.setText("");
            binding.cardName.setText("");

            binding.addBtn.setOnClickListener(v -> {
                try {
                    dialog.showLoading("Loading...");
                    PaymentCardApi.api.addNewCard(CredentialToken.getInstance(requireActivity()).getBearerAccessToken(), new PaymentCardApi.UpdateCardInfoBody(
                            Objects.requireNonNull(binding.cardNumber.getText()).toString(),
                            Objects.requireNonNull(binding.cardName.getText()).toString(),
                            Formatter.fromCardExp(Objects.requireNonNull(binding.expiredDate.getText()).toString()),
                            Objects.requireNonNull(binding.cvv.getText()).toString(),
                            "visa"
                    )).enqueue(new Callback<PaymentCardApi.UpdatedResponse>() {
                        @Override
                        public void onResponse(@NonNull Call<PaymentCardApi.UpdatedResponse> call, @NonNull Response<PaymentCardApi.UpdatedResponse> response) {
                            dialog.dismiss();
                            if (getActivity() == null) return;

                            if (response.code() == 200) {
                                Toast.makeText(requireActivity(), "Successfully add new card", Toast.LENGTH_SHORT).show();
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
                } catch (ParseException e) {
                    Toast.makeText(requireActivity(), "Cannot parse", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            binding.textView11.setText(requireActivity().getString(R.string.update_card));
            binding.addBtn.setText(requireActivity().getString(R.string.update_card));

            PaymentCardManager.getInstance().getCardList().observe(getViewLifecycleOwner(), paymentCards -> {
                if (paymentCards == null) return;
                Optional<PaymentCard> paymentCardOptional = paymentCards.stream().filter(card -> card._id.equals(id)).findFirst();
                if (!paymentCardOptional.isPresent()) return;

                PaymentCard paymentCard = paymentCardOptional.get();

                binding.cardNumber.setText(paymentCard.cardNumber);
                binding.expiredDate.setText(Formatter.dateToCardEXP(paymentCard.expiredDate));
                binding.cardName.setText(paymentCard.name);
                binding.cvv.setText(paymentCard.cvv);

                binding.addBtn.setOnClickListener(v -> {
                    dialog.showLoading("Loading...");
                    try {
                        PaymentCardApi.api.updateCardInfo(CredentialToken.getInstance(requireActivity()).getBearerAccessToken(), paymentCard._id, new PaymentCardApi.UpdateCardInfoBody(
                                Objects.requireNonNull(binding.cardNumber.getText()).toString(),
                                Objects.requireNonNull(binding.cardName.getText()).toString(),
                                Formatter.fromCardExp(Objects.requireNonNull(binding.expiredDate.getText()).toString()),
                                Objects.requireNonNull(binding.cvv.getText()).toString(),
                                paymentCard.type
                        )).enqueue(new Callback<PaymentCardApi.UpdatedResponse>() {
                            @Override
                            public void onResponse(@NonNull Call<PaymentCardApi.UpdatedResponse> call, @NonNull Response<PaymentCardApi.UpdatedResponse> response) {
                                dialog.dismiss();
                                if (getActivity() == null) return;

                                if (response.code() == 200) {
                                    Toast.makeText(requireActivity(), "Successfully update card information", Toast.LENGTH_SHORT).show();
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
                    } catch (ParseException e) {
                        Toast.makeText(requireActivity(), "Cannot parse", Toast.LENGTH_SHORT).show();
                    }
                });
            });
        }

        binding.cardNumber.addTextChangedListener(new TextWatcher() {
            @SuppressLint("SetTextI18n")
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                String text = String.valueOf(s);
                if (previousCardNumberLength > text.length()) {
                    if (text.length() == 5 || text.length() == 10 || text.length() == 15) {
                        binding.cardNumber.setText(Objects.requireNonNull(binding.cardNumber.getText()).toString().substring(0, text.length() - 1));
                    }
                } else if (text.length() == 4 || text.length() == 9 || text.length() == 14) {
                    binding.cardNumber.setText(Objects.requireNonNull(binding.cardNumber.getText()) + " ");
                }
                previousCardNumberLength = text.length();
                binding.cardNumber.setSelection(Objects.requireNonNull(binding.cardNumber.getText()).length());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        binding.expiredDate.addTextChangedListener(new TextWatcher() {
            @SuppressLint("SetTextI18n")
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String text = String.valueOf(s);
                if (previousExpLength > text.length()) {
                    if (text.length() == 3) {
                        binding.expiredDate.setText(Objects.requireNonNull(binding.expiredDate.getText()).toString().substring(0, text.length() - 1));
                    }
                } else if (text.length() == 2) {
                    if (Integer.parseInt(text) > 12)
                        binding.expiredDate.setText(Objects.requireNonNull(binding.expiredDate.getText()).toString().substring(0, 1));
                    else
                        binding.expiredDate.setText(Objects.requireNonNull(binding.expiredDate.getText()) + "/");
                } else if (text.length() == 1) {
                    if (Integer.parseInt(text) > 1) binding.expiredDate.setText("");
                }
                previousExpLength = text.length();
                binding.expiredDate.setSelection(Objects.requireNonNull(binding.expiredDate.getText()).length());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        return binding.getRoot();
    }

    void submit() {

    }
}