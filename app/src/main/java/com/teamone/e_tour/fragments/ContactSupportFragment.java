package com.teamone.e_tour.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.teamone.e_tour.R;
import com.teamone.e_tour.adapters.ChatAdapter;
import com.teamone.e_tour.api.support.GetChatRoomOfRoute;
import com.teamone.e_tour.databinding.FragmentContactSupportBinding;
import com.teamone.e_tour.entities.ChatRoom;
import com.teamone.e_tour.entities.Image;
import com.teamone.e_tour.entities.UserProfile;
import com.teamone.e_tour.models.UserProfileManager;

public class ContactSupportFragment extends Fragment {
    String routeId;
    MutableLiveData<ChatRoom> chatRoom = new MutableLiveData<>();

    public ContactSupportFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            routeId = getArguments().getString("routeId");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentContactSupportBinding binding = FragmentContactSupportBinding.inflate(inflater, container, false);

        ChatAdapter adapter = new ChatAdapter(this);
        binding.chatList.setAdapter(adapter);
        binding.chatList.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));

        GetChatRoomOfRoute.getChatRoom(getActivity(), routeId, new GetChatRoomOfRoute.IGetChatRoomOfRouteCallback() {
            @Override
            public void onSuccess(ChatRoom chatRoom) {
                ContactSupportFragment.this.chatRoom.postValue(chatRoom);
            }
        });


        chatRoom.observe(getViewLifecycleOwner(), new Observer<ChatRoom>() {
            @Override
            public void onChanged(ChatRoom chatRoom) {
                if (chatRoom == null) return;
                Glide.with(getActivity()).load(new Image(chatRoom.staffId.image).getImageUri()).into(binding.staffAvatar);
                binding.staffName.setText(chatRoom.staffId.fullName);
                binding.staffId.setText(chatRoom.staffId.role);
                String userId = UserProfileManager.getInstance(getActivity()).getUserProfile().get_id();
                adapter.setMessages(chatRoom.chats);
            }
        });

        return binding.getRoot();
    }
}