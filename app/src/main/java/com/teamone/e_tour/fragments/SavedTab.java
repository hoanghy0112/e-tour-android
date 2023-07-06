package com.teamone.e_tour.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.faltenreich.skeletonlayout.Skeleton;
import com.faltenreich.skeletonlayout.SkeletonLayoutUtils;
import com.teamone.e_tour.R;
import com.teamone.e_tour.adapters.RouteListAdapter;
import com.teamone.e_tour.adapters.VoucherAdapter;
import com.teamone.e_tour.api.voucher.SavedVoucher;
import com.teamone.e_tour.databinding.FragmentSavedTabBinding;
import com.teamone.e_tour.dialogs.LoadingDialog;
import com.teamone.e_tour.entities.TouristRoute;
import com.teamone.e_tour.entities.Voucher;
import com.teamone.e_tour.models.CredentialToken;
import com.teamone.e_tour.models.SavedRouteManager;
import com.teamone.e_tour.models.VoucherManager;

import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SavedTab extends Fragment {
    Skeleton savedVoucherListSkeleton;

    public SavedTab() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentSavedTabBinding binding = FragmentSavedTabBinding.inflate(inflater, container, false);

        requireActivity().findViewById(R.id.bottom_navigation).setVisibility(View.VISIBLE);

        RouteListAdapter adapter = new RouteListAdapter(this, R.layout.fragment_route_preview_card_large);
        LoadingDialog dialog = new LoadingDialog(requireActivity());
        dialog.showLoading("Loading data");

        VoucherAdapter voucherAdapter = new VoucherAdapter(this, R.layout.item_saved_voucher_preview);

        binding.savedRouteList.setAdapter(adapter);
        binding.savedRouteList.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));

        binding.savedVouchersList.setNestedScrollingEnabled(false);
        binding.savedVouchersList.setAdapter(voucherAdapter);
        binding.savedVouchersList.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));

        Skeleton savedVoucherListSkeleton = SkeletonLayoutUtils.applySkeleton(binding.savedVouchersList, R.layout.item_saved_voucher_preview);
        savedVoucherListSkeleton.setMaskCornerRadius(60);
        savedVoucherListSkeleton.showSkeleton();

        SavedRouteManager.getInstance((AppCompatActivity) getActivity()).getRoutes().observe(getViewLifecycleOwner(), new Observer<ArrayList<TouristRoute>>() {
            @Override
            public void onChanged(ArrayList<TouristRoute> touristRoutes) {
                if (touristRoutes == null) return;
                dialog.dismiss();
                adapter.setRouteList(touristRoutes);
            }
        });

        fetchSavedVoucher();
        binding.swiperefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchSavedVoucher();
            }
        });

        VoucherManager.getInstance().getSavedList().observe(getViewLifecycleOwner(), new Observer<ArrayList<Voucher>>() {
            @Override
            public void onChanged(ArrayList<Voucher> newVouchers) {
                voucherAdapter.setVouchers(newVouchers);
                binding.swiperefresh.setRefreshing(false);
                savedVoucherListSkeleton.showOriginal();
            }
        });


        return binding.getRoot();
    }

    void fetchSavedVoucher() {
        SavedVoucher.api.viewSavedVoucher("Bearer " + CredentialToken.getInstance(requireActivity()).getAccessToken()).enqueue(new Callback<SavedVoucher.ViewSavedVoucherResponse>() {
            @Override
            public void onResponse(@NonNull Call<SavedVoucher.ViewSavedVoucherResponse> call, @NonNull Response<SavedVoucher.ViewSavedVoucherResponse> response) {
                if (getActivity() == null) return;

                if (response.code() == 200) {
                    assert response.body() != null;
                    VoucherManager.getInstance().setSavedList(response.body().data);
                } else {
                    try {
                        assert response.errorBody() != null;
                        Toast.makeText(requireActivity(), response.code() + " - " + response.errorBody().string(), Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<SavedVoucher.ViewSavedVoucherResponse> call, @NonNull Throwable t) {
                Toast.makeText(requireActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}