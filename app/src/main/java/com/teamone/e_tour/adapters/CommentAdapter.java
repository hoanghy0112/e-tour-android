package com.teamone.e_tour.adapters;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CommentAdapter {
    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView userImage;
        TextView userDisplayName;
        TextView ratingPoint;
        TextView comment;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
