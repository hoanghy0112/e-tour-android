package com.teamone.e_tour.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.faltenreich.skeletonlayout.Skeleton;
import com.faltenreich.skeletonlayout.SkeletonLayoutUtils;
import com.teamone.e_tour.R;
import com.teamone.e_tour.adapters.PaymentCardAdapter;
import com.teamone.e_tour.api.paymentCard.PaymentCardApi;
import com.teamone.e_tour.databinding.FragmentViewAllCardBinding;
import com.teamone.e_tour.entities.PaymentCard;
import com.teamone.e_tour.models.CredentialToken;
import com.teamone.e_tour.models.PaymentCardManager;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ViewAllCardFragment extends Fragment {

    public ViewAllCardFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentViewAllCardBinding binding = FragmentViewAllCardBinding.inflate(inflater);

        PaymentCardAdapter adapter = new PaymentCardAdapter(this);

        binding.cardList.setNestedScrollingEnabled(false);
        binding.cardList.setAdapter(adapter);
        binding.cardList.setLayoutManager(new LinearLayoutManager(requireActivity(), RecyclerView.VERTICAL, false));

        Skeleton skeleton = SkeletonLayoutUtils.applySkeleton(binding.cardList, R.layout.item_card_preview);
        skeleton.setMaskCornerRadius(60);
        skeleton.showSkeleton();

        binding.cvvAuthRadio.setOnClickListener(v -> binding.radioGroup.check(binding.cvvAuthRadio.getId()));

        binding.accountAuthRadio.setOnClickListener(v -> binding.radioGroup.check(binding.accountAuthRadio.getId()));

        binding.addBtn.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.action_viewAllCardFragment_to_addPaymentCard);
        });

        PaymentCardManager.getInstance().getCardList().observe(getViewLifecycleOwner(), paymentCards -> {
            if (paymentCards == null) return;
            binding.swiperefresh.setRefreshing(false);
            skeleton.showOriginal();
            adapter.setCards(paymentCards);
        });

        fetchData();
        binding.swiperefresh.setOnRefreshListener(this::fetchData);

        binding.backBtn.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.action_viewAllCardFragment_to_accountTab2));

        return binding.getRoot();
    }

    void fetchData() {
        PaymentCardApi.api.viewAllCards(CredentialToken.getInstance(requireActivity()).getBearerAccessToken()).enqueue(new Callback<PaymentCardApi.ViewAllCardsResponse>() {
            @Override
            public void onResponse(@NonNull Call<PaymentCardApi.ViewAllCardsResponse> call, @NonNull Response<PaymentCardApi.ViewAllCardsResponse> response) {
                if (response.code() == 200) {
                    if (getActivity() == null) return;
                    assert response.body() != null;
                    PaymentCardManager.getInstance().setCardList(response.body().data);
                } else {
                    if (getActivity() == null) return;
                    Toast.makeText(requireActivity(), response.code() + " - " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<PaymentCardApi.ViewAllCardsResponse> call, @NonNull Throwable t) {
                if (getActivity() == null) return;
                Toast.makeText(requireActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }
}