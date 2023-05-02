package com.teamone.e_tour.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.teamone.e_tour.R;
import com.teamone.e_tour.adapters.RecommendedRouteListAdapter;
import com.teamone.e_tour.databinding.ActivityHomeBinding;
import com.teamone.e_tour.databinding.FragmentHomeBinding;
import com.teamone.e_tour.dialogs.LoadingDialog;
import com.teamone.e_tour.entities.TouristRoute;
import com.teamone.e_tour.models.RecommendedRouteManager;

import java.util.ArrayList;

public class HomeFragment extends Fragment {
    private Context context = getActivity();
    private RecyclerView recommendList;

    public HomeFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        RecommendedRouteManager.getInstance(getActivity()).fetchData(10);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentHomeBinding binding = FragmentHomeBinding.inflate(inflater, container, false);

        recommendList = binding.getRoot().findViewById(R.id.recommend_list);

        RecommendedRouteListAdapter adapter = new RecommendedRouteListAdapter(new ArrayList<>(), HomeFragment.this);
        recommendList.setAdapter(adapter);
        recommendList.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));

        LoadingDialog dialog = new LoadingDialog(getActivity());
        dialog.showLoading("Fetching data");


        RecommendedRouteManager.getInstance(context).getRouteList().observe(getViewLifecycleOwner(), new Observer<ArrayList<TouristRoute>>() {
            @Override
            public void onChanged(ArrayList<TouristRoute> touristRoutes) {
                if (touristRoutes.size() <= 0) return;
                dialog.dismiss();
                adapter.setRouteList(touristRoutes);
            }
        });


        Window window = getActivity().getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(getActivity(), R.color.blue_dark));

        return binding.getRoot();
    }
}