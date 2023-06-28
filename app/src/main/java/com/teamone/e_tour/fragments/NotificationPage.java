package com.teamone.e_tour.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.faltenreich.skeletonlayout.Skeleton;
import com.faltenreich.skeletonlayout.SkeletonLayoutUtils;
import com.teamone.e_tour.R;
import com.teamone.e_tour.activities.HomeActivity;
import com.teamone.e_tour.adapters.NotificationAdapter;
import com.teamone.e_tour.databinding.FragmentNotificationPageBinding;
import com.teamone.e_tour.entities.NotificationItem;
import com.teamone.e_tour.models.NotificationManager;

import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class NotificationPage extends Fragment {

    public NotificationPage() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ((HomeActivity) requireActivity()).setBackId(R.id.action_notificationPage_to_homeFragment);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentNotificationPageBinding binding = FragmentNotificationPageBinding.inflate(inflater, container, false);

        NotificationAdapter recentAdapter = new NotificationAdapter(this);
        NotificationAdapter oldAdapter = new NotificationAdapter(this);

        binding.recentNotificationList.setAdapter(recentAdapter);
        binding.recentNotificationList.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));

        binding.oldNotificationList.setAdapter(oldAdapter);
        binding.oldNotificationList.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));

        Skeleton recentSkeleton = SkeletonLayoutUtils.applySkeleton(binding.recentNotificationList, R.layout.notification_large_item);
        recentSkeleton.setMaskCornerRadius(60);
        recentSkeleton.showSkeleton();

        Skeleton oldSkeleton = SkeletonLayoutUtils.applySkeleton(binding.oldNotificationList, R.layout.notification_large_item);
        oldSkeleton.setMaskCornerRadius(60);
        oldSkeleton.showSkeleton();


        NotificationManager.getInstance(requireActivity()).getNotificationList().observe(getViewLifecycleOwner(), new Observer<ArrayList<NotificationItem>>() {
            @Override
            public void onChanged(ArrayList<NotificationItem> notificationItems) {
                if (notificationItems == null) return;

                ArrayList<NotificationItem> recentNotification = notificationItems.stream().filter(notificationItem -> notificationItem.createdAt != null && new Date().getTime() - notificationItem.createdAt.getTime() < 100400000).collect(Collectors.toCollection(ArrayList::new));
                ArrayList<NotificationItem> oldNotification = notificationItems.stream().filter(notificationItem -> notificationItem.createdAt == null || new Date().getTime() - notificationItem.createdAt.getTime() >= 100400000).collect(Collectors.toCollection(ArrayList::new));

                recentAdapter.setNotificationList(recentNotification);
                oldAdapter.setNotificationList(oldNotification);

                recentSkeleton.showOriginal();
                oldSkeleton.showOriginal();
            }
        });

        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(requireActivity(), R.id.home_wrapper).navigate(R.id.action_notificationPage_to_homeFragment);
            }
        });

        return binding.getRoot();
    }
}