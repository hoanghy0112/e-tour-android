package com.teamone.e_tour.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.teamone.e_tour.R;
import com.teamone.e_tour.entities.Rating;
import com.teamone.e_tour.utils.Formatter;

import java.util.ArrayList;


public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {
    private Fragment context;
    private ArrayList<Rating> ratings = new ArrayList<>();

    public CommentAdapter(Fragment context) {
        this.context = context;
    }

    public void setRatings(ArrayList<Rating> ratings) {
        this.ratings = ratings;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comment, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Rating rating = ratings.get(position);

        holder.comment.setText(rating.getDescription());
//        holder.ratingPoint.setText(String.valueOf(rating.getStar()));
        holder.ratingPoint.setRating(rating.getStar());
        holder.userDisplayName.setText(rating.getUserId().getFullName());
        holder.createdAt.setText(Formatter.dateToDateWithoutHourString(rating.getCreatedAt()));
        Glide.with(context).load(rating.getUserId().getImage()).into(holder.userImage);
    }

    @Override
    public int getItemCount() {
        return ratings.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView userImage;
        TextView userDisplayName;
        RatingBar ratingPoint;
        TextView comment;
        TextView createdAt;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            userImage = itemView.findViewById(R.id.user_image);
            userDisplayName = itemView.findViewById(R.id.user_display_name);
            ratingPoint = itemView.findViewById(R.id.rating_point);
            comment = itemView.findViewById(R.id.comment);
            createdAt = itemView.findViewById(R.id.createdAt);
        }
    }
}
