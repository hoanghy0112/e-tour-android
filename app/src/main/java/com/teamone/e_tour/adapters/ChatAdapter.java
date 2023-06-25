package com.teamone.e_tour.adapters;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.GravityInt;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.teamone.e_tour.R;
import com.teamone.e_tour.entities.ChatMessage;
import com.teamone.e_tour.entities.UserProfile;
import com.teamone.e_tour.utils.Formatter;

import java.util.ArrayList;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {
    Fragment context;
    ArrayList<ChatMessage> messages = new ArrayList<>();
    String userId;

    public ChatAdapter(Fragment context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_message_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ChatMessage message = messages.get(position);

        holder.content.setText(message.content);
        holder.createdAt.setText(Formatter.dateToDateOnlyHourString(message.createdAt));
        if (userId != null && userId.equals(message.uid))
            holder.message.setGravity(Gravity.END);
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public void setMessages(ArrayList<ChatMessage> messages) {
        this.messages = messages;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView content;
        TextView createdAt;
        LinearLayout message;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            content = itemView.findViewById(R.id.chat_message_content);
            createdAt = itemView.findViewById(R.id.chat_message_created_at);
            message = itemView.findViewById(R.id.chat_message);
        }
    }
}
