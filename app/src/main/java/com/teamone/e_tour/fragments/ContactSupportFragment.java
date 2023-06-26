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
import com.teamone.e_tour.api.support.GetMessageList;
import com.teamone.e_tour.api.support.SendChatMessage;
import com.teamone.e_tour.databinding.FragmentContactSupportBinding;
import com.teamone.e_tour.entities.ChatMessage;
import com.teamone.e_tour.entities.ChatRoom;
import com.teamone.e_tour.entities.Image;
import com.teamone.e_tour.entities.UserProfile;
import com.teamone.e_tour.models.UserProfileManager;

import java.util.ArrayList;

public class ContactSupportFragment extends Fragment {
    String routeId;
    MutableLiveData<ChatRoom> chatRoom = new MutableLiveData<>();
    MutableLiveData<ArrayList<ChatMessage>> messageList = new MutableLiveData<>(new ArrayList<>());

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
                GetMessageList.getMessageList(getActivity(), chatRoom._id, new GetMessageList.IGetMessageListCallback() {
                    @Override
                    public void onGetMessageList(ArrayList<ChatMessage> messages) {
//                        adapter.setMessages(messages);
                        messageList.postValue(messages);
                    }

                    @Override
                    public void onNewMessage(ChatMessage message) {
//                        adapter.addNewMessage(message);
                        ArrayList<ChatMessage> oldMessages = messageList.getValue();
                        oldMessages.add(message);
                        messageList.postValue(oldMessages);
                    }
                });

                Glide.with(getActivity()).load(new Image(chatRoom.staffId.image).getImageUri()).into(binding.staffAvatar);
                binding.staffName.setText(chatRoom.staffId.fullName);
                binding.staffId.setText(chatRoom.staffId.role);
                String userId = UserProfileManager.getInstance(getActivity()).getUserProfile().get_id();
                adapter.setUserId(userId);
                adapter.setMessages(chatRoom.chats);
            }
        });

        messageList.observe(getViewLifecycleOwner(), new Observer<ArrayList<ChatMessage>>() {
            @Override
            public void onChanged(ArrayList<ChatMessage> chatMessages) {
                adapter.setMessages(chatMessages);
                binding.chatList.setAdapter(adapter);
                binding.chatList.scrollToPosition(Math.max(chatMessages.size() - 1, 0));
            }
        });

        binding.sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = binding.chatContent.getText().toString();

                SendChatMessage.send(getActivity(), chatRoom.getValue()._id, content);

                binding.chatContent.setText("");
            }
        });

        return binding.getRoot();
    }
}