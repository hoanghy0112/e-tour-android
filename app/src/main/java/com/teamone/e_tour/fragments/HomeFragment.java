package com.teamone.e_tour.fragments;

import android.content.Context;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextPaint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.teamone.e_tour.R;
import com.teamone.e_tour.adapters.RouteListAdapter;
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

        getActivity().findViewById(R.id.bottom_navigation).setVisibility(View.VISIBLE);
        getActivity().findViewById(R.id.home_wrapper).setPadding(0, 0, 0, 180);

        recommendList = binding.getRoot().findViewById(R.id.recommend_list);

        RouteListAdapter adapter = new RouteListAdapter(HomeFragment.this, R.layout.fragment_route_preview_card_large);
        recommendList.setAdapter(adapter);
        recommendList.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));

        TextPaint paint = binding.textView6.getPaint();
        float width = paint.measureText(getString(R.string.for_you));
        Shader textShader = new LinearGradient(0, 0, width, binding.textView6.getTextSize(),
                new int[]{Color.parseColor("#177DB9"), Color.parseColor("#E3C7B3")},
                null, Shader.TileMode.CLAMP);
        binding.textView6.getPaint().setShader(textShader);

        binding.forYouBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(getActivity(), R.id.home_wrapper).navigate(R.id.action_homeFragment_to_forYou);
            }
        });

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