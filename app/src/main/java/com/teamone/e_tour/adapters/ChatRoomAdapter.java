package com.teamone.e_tour.adapters;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.card.MaterialCardView;
import com.teamone.e_tour.R;
import com.teamone.e_tour.entities.Image;
import com.teamone.e_tour.models.ChatRoomManager;
import com.teamone.e_tour.models.CredentialToken;
import com.teamone.e_tour.utils.Formatter;

import java.util.ArrayList;

public class ChatRoomAdapter extends RecyclerView.Adapter<ChatRoomAdapter.ViewHolder> {
    Context context;
    ArrayList<ChatRoomManager.ResponseData.ChatRoom> chatRooms = new ArrayList<>();

    public void setChatRooms(ArrayList<ChatRoomManager.ResponseData.ChatRoom> chatRooms) {
        this.chatRooms = chatRooms;
        notifyDataSetChanged();
    }

    public ChatRoomAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_room_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ChatRoomManager.ResponseData.ChatRoom chatRoom = chatRooms.get(position);

        String userId = CredentialToken.getInstance(context).getId();

        holder.routeName.setText(chatRoom.route.getName());
        holder.staffName.setText(chatRoom.staff.fullName);
        if (chatRoom.lastChat != null) {
            holder.lastMessageText.setText(context.getString(chatRoom.lastChat.uid.equals(userId) ? R.string.user_message : R.string.staff_message, chatRoom.lastChat.content));
            holder.lastMessageTime.setText(Formatter.dateToHourString(chatRoom.lastChat.createdAt));
        } else {
            holder.lastMessageText.setText(context.getString(R.string.empty_chat));
            holder.lastMessageTime.setText("");
        }
        Glide.with(context).load(new Image(chatRoom.route.getImages().get(0)).getImageUri()).into(holder.image);

        holder.card.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putString("routeId", chatRoom.routeId);
            Navigation.findNavController(v).navigate(R.id.contactSupportFragment, bundle);
        });
    }

    @Override
    public int getItemCount() {
        return chatRooms.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView routeName;
        TextView staffName;
        TextView lastMessageText;
        TextView lastMessageTime;
        MaterialCardView card;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.route_image);
            routeName = itemView.findViewById(R.id.route_name);
            staffName = itemView.findViewById(R.id.staff_name);
            lastMessageText = itemView.findViewById(R.id.last_message_text);
            lastMessageTime = itemView.findViewById(R.id.last_message_time);
            card = itemView.findViewById(R.id.card);
        }
    }
}
