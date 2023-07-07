package com.teamone.e_tour.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
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
import com.teamone.e_tour.adapters.ChatRoomAdapter;
import com.teamone.e_tour.databinding.FragmentAllSupportBinding;
import com.teamone.e_tour.models.ChatRoomManager;

import java.util.ArrayList;

public class AllSupport extends Fragment {
    public AllSupport() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentAllSupportBinding binding = FragmentAllSupportBinding.inflate(inflater);

        ChatRoomAdapter adapter = new ChatRoomAdapter(requireActivity());

        binding.chatRoomList.setNestedScrollingEnabled(false);
        binding.chatRoomList.setAdapter(adapter);
        binding.chatRoomList.setLayoutManager(new LinearLayoutManager(requireActivity(), RecyclerView.VERTICAL, false));

        Skeleton skeleton = SkeletonLayoutUtils.applySkeleton(binding.chatRoomList, R.layout.chat_room_item, 4);
        skeleton.setMaskCornerRadius(60);
        skeleton.showSkeleton();

        ChatRoomManager.getInstance().getChatRooms().observe(getViewLifecycleOwner(), new Observer<ArrayList<ChatRoomManager.ResponseData.ChatRoom>>() {
            @Override
            public void onChanged(ArrayList<ChatRoomManager.ResponseData.ChatRoom> chatRooms) {
                if (chatRooms == null) return;

                adapter.setChatRooms(chatRooms);
                skeleton.showOriginal();
                binding.swiperefresh.setRefreshing(false);
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

    public void fetchData() {
        ChatRoomManager.getInstance().fetchData();
    }
}