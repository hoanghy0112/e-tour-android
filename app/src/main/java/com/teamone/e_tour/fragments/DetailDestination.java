package com.teamone.e_tour.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.text.method.MultiTapKeyListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.faltenreich.skeletonlayout.Skeleton;
import com.faltenreich.skeletonlayout.SkeletonLayoutUtils;
import com.teamone.e_tour.R;
import com.teamone.e_tour.adapters.RouteListAdapter;
import com.teamone.e_tour.api.route.TouristRouteApi;
import com.teamone.e_tour.databinding.FragmentDetailDestinationBinding;
import com.teamone.e_tour.entities.TouristRoute;

import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailDestination extends Fragment {
    String destination;
    MutableLiveData<ArrayList<TouristRoute>> routes = new MutableLiveData<>();

    public DetailDestination() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            destination = getArguments().getString("destination");
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        FragmentDetailDestinationBinding binding = FragmentDetailDestinationBinding.inflate(inflater);

        RouteListAdapter adapter = new RouteListAdapter(this, R.layout.fragment_route_card_destination_preview);
        binding.routeList.setNestedScrollingEnabled(false);
        binding.routeList.setAdapter(adapter);
        binding.routeList.setLayoutManager(new LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false));

        Skeleton skeleton = SkeletonLayoutUtils.applySkeleton(binding.routeList, R.layout.fragment_route_card_destination_preview, 8);
        skeleton.setMaskCornerRadius(60);
        skeleton.showSkeleton();

        binding.destination.setText(destination);

        routes.observe(getViewLifecycleOwner(), new Observer<ArrayList<TouristRoute>>() {
            @Override
            public void onChanged(ArrayList<TouristRoute> touristRoutes) {
                if (touristRoutes == null) return;
                skeleton.showOriginal();
                binding.swiperefresh.setRefreshing(false);

                adapter.setRouteList(touristRoutes);
            }
        });

        fetchData();
        binding.swiperefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchData();
            }
        });

        return binding.getRoot();
    }

    void fetchData() {
        TouristRouteApi.api.filterRouteDestination(destination).enqueue(new Callback<TouristRouteApi.FilterRouteDestinationResponse>() {
            @Override
            public void onResponse(Call<TouristRouteApi.FilterRouteDestinationResponse> call, Response<TouristRouteApi.FilterRouteDestinationResponse> response) {
                if (getActivity() == null) return;

                if (response.code() == 200) {
                    assert response.body() != null;
                    routes.postValue(response.body().data);
                } else {
                    try {
                        assert response.errorBody() != null;
                        Toast.makeText(requireActivity(), "Call API error: " + response.code() + " - " + response.errorBody().string(), Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }

            @Override
            public void onFailure(Call<TouristRouteApi.FilterRouteDestinationResponse> call, Throwable t) {
                if (getActivity() == null) return;

                Toast.makeText(requireActivity(), "App error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}