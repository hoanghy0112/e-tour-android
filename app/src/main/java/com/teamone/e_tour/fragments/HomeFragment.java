package com.teamone.e_tour.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.FragmentNavigator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextPaint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.faltenreich.skeletonlayout.Skeleton;
import com.faltenreich.skeletonlayout.SkeletonLayout;
import com.faltenreich.skeletonlayout.SkeletonLayoutUtils;
import com.teamone.e_tour.R;
import com.teamone.e_tour.activities.HomeActivity;
import com.teamone.e_tour.adapters.RouteListAdapter;
import com.teamone.e_tour.adapters.VoucherAdapter;
import com.teamone.e_tour.databinding.FragmentHomeBinding;
import com.teamone.e_tour.dialogs.LoadingDialog;
import com.teamone.e_tour.entities.TouristRoute;
import com.teamone.e_tour.entities.UserProfile;
import com.teamone.e_tour.entities.Voucher;
import com.teamone.e_tour.models.HotVoucherManager;
import com.teamone.e_tour.models.PopularRouteManager;
import com.teamone.e_tour.models.RecommendedRouteManager;
import com.teamone.e_tour.models.UserProfileManager;

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

        ((HomeActivity) requireActivity()).setBackId(-1);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentHomeBinding binding = FragmentHomeBinding.inflate(inflater, container, false);

        requireActivity().findViewById(R.id.bottom_navigation).setVisibility(View.VISIBLE);
        requireActivity().findViewById(R.id.home_wrapper).setPadding(0, 0, 0, 180);

        recommendList = binding.getRoot().findViewById(R.id.recommend_list);

        RouteListAdapter recommendAdapter = new RouteListAdapter(HomeFragment.this, R.layout.fragment_route_preview_card_large);
        recommendList.setAdapter(recommendAdapter);
        recommendList.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));

        Skeleton recommendListSkeleton = SkeletonLayoutUtils.applySkeleton(binding.recommendList, R.layout.fragment_route_preview_card_large);
        recommendListSkeleton.setMaskCornerRadius(60);
        recommendListSkeleton.showSkeleton();

        RouteListAdapter popularAdapter = new RouteListAdapter(HomeFragment.this, R.layout.fragment_route_preview_card_large_horizontal);
        binding.popularList.setAdapter(popularAdapter);
        binding.popularList.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));

        Skeleton popularListSkeleton = SkeletonLayoutUtils.applySkeleton(binding.popularList, R.layout.fragment_route_preview_card_large_horizontal);
        popularListSkeleton.setMaskCornerRadius(60);
        popularListSkeleton.showSkeleton();

        VoucherAdapter voucherAdapter = new VoucherAdapter(HomeFragment.this);
        binding.hotVoucherList.setAdapter(voucherAdapter);
        binding.hotVoucherList.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));

        Skeleton voucherListSkeleton = SkeletonLayoutUtils.applySkeleton(binding.hotVoucherList, R.layout.item_voucher_preview);
        voucherListSkeleton.setMaskCornerRadius(40);
        voucherListSkeleton.showSkeleton();

        UserProfileManager.getInstance(requireActivity()).getLiveUserProfile().observe(getViewLifecycleOwner(), new Observer<UserProfile>() {
            @Override
            public void onChanged(UserProfile userProfile) {
                String[] names = userProfile.getFullName().split(" ");
                String name = names[names.length - 1];
                binding.greeting.setText("Hi " + name + ",");
            }
        });

        String[] names = UserProfileManager.getInstance(requireActivity()).getUserProfile().getFullName().split(" ");
        String name = names[names.length - 1];
        binding.greeting.setText("Hi " + names[0] + ",");

        binding.notifcationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(requireActivity(), R.id.home_wrapper).navigate(R.id.action_homeFragment_to_notificationPage);
            }
        });

        binding.editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                try {
                    FragmentNavigator.Extras extras = new FragmentNavigator.Extras.Builder().addSharedElement(binding.editText, "search_view").build();
                    Navigation.findNavController(requireActivity(), R.id.home_wrapper).navigate(R.id.action_homeFragment_to_inputSearch, null, null, extras);
                    binding.editText.setOnFocusChangeListener(null);
                } catch (IllegalArgumentException e) {
                    //
                }
            }
        });

        binding.forYouBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(requireActivity(), R.id.home_wrapper).navigate(R.id.action_homeFragment_to_forYou);
            }
        });

        binding.popularBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(requireActivity(), R.id.home_wrapper).navigate(R.id.action_homeFragment_to_popular);
            }
        });


        RecommendedRouteManager.getInstance(context).getRouteList().observe(getViewLifecycleOwner(), new Observer<ArrayList<TouristRoute>>() {
            @Override
            public void onChanged(ArrayList<TouristRoute> touristRoutes) {
                if (touristRoutes.size() <= 0) return;
                recommendListSkeleton.showOriginal();
                recommendAdapter.setRouteList(touristRoutes);
            }
        });

        PopularRouteManager.getInstance(context).getRouteList().observe(getViewLifecycleOwner(), new Observer<ArrayList<TouristRoute>>() {
            @Override
            public void onChanged(ArrayList<TouristRoute> touristRoutes) {
                if (touristRoutes.size() <= 0) return;
                popularListSkeleton.showOriginal();
                popularAdapter.setRouteList(touristRoutes);
            }
        });

        HotVoucherManager.getInstance(context).getVouchers().observe(getViewLifecycleOwner(), new Observer<ArrayList<Voucher>>() {
            @Override
            public void onChanged(ArrayList<Voucher> vouchers) {
                if (vouchers.size() <= 0) return;
                voucherListSkeleton.showOriginal();
                voucherAdapter.setVouchers(vouchers);
            }
        });


        return binding.getRoot();
    }


}