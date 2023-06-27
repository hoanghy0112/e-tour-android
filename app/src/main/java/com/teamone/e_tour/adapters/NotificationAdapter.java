package com.teamone.e_tour.adapters;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.card.MaterialCardView;
import com.teamone.e_tour.R;
import com.teamone.e_tour.constants.SocketMessage;
import com.teamone.e_tour.entities.Image;
import com.teamone.e_tour.entities.NotificationItem;
import com.teamone.e_tour.utils.SocketManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {
    Fragment context;
    private ArrayList<NotificationItem> notificationList = new ArrayList<>();

    public ArrayList<NotificationItem> getNotificationList() {
        return notificationList;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setNotificationList(ArrayList<NotificationItem> notificationList) {
        this.notificationList = notificationList;
        notifyDataSetChanged();
    }

    public NotificationAdapter(Fragment context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notification_large_item, parent, false);
        if (viewType == 0)
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notification_large_item, parent, false);
        else if (viewType == 1)
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notification_small_item, parent, false);

        return new ViewHolder(view);
    }

    void readNotification(String id) {
        JSONObject object = new JSONObject();
        JSONArray idList = new JSONArray();
        idList.put(id);
        try {
            object.put("notificationIDs", idList);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        SocketManager.getInstance(context.requireActivity()).emit("read-notification", object);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        NotificationItem notificationItem = notificationList.get(position);

        holder.title.setText(notificationItem.title);
        holder.content.setText(notificationItem.content);
        if (notificationItem.image != null && !notificationItem.image.equals("")) {
            Glide.with(context).load(new Image(notificationItem.image).getImageUri()).into(holder.image);
        } else {
            holder.image.getLayoutParams().height = 20;
            holder.image.requestLayout();
        }

        if (notificationItem.isRead != null) {
            if (!notificationItem.isRead) {
                holder.card.setBackgroundTintList(context.requireActivity().getColorStateList(R.color.blue_5_percent));
            }
        }

        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                readNotification(notificationItem._id);
                holder.card.setBackgroundTintList(context.requireActivity().getColorStateList(R.color.white));
            }
        });

        if (notificationItem.link != null) {
            Pattern pattern = Pattern.compile("(?<type>\\w+)-(?<mainId>\\w+)/(?<subId>\\w+)");
            Matcher matcher = pattern.matcher(notificationItem.link);

            if (matcher.find()) {
                String type = matcher.group("type");
                String mainId = matcher.group("mainId");
                String subId = matcher.group("subId");

                assert type != null;
                if (type.equals("route")) {
                    Bundle bundle = new Bundle();
                    bundle.putString("id", mainId);
                    holder.card.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            readNotification(notificationItem._id);
                            holder.card.setBackgroundTintList(context.requireActivity().getColorStateList(R.color.white));
                            Navigation.findNavController(context.requireActivity(), R.id.home_wrapper).navigate(R.id.detailTourFragment, bundle);
                        }
                    });
                } else if (type.equals("ticket")) {

                }
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        NotificationItem notificationItem = notificationList.get(position);

        if (notificationItem.link == null) return 0;

        Pattern pattern = Pattern.compile("(?<type>\\w+)-(?<mainId>\\w+)/(?<subId>\\w+)");
        Matcher matcher = pattern.matcher(notificationItem.link);

        if (matcher.find()) {
            String type = matcher.group("type");
            String mainId = matcher.group("mainId");
            String subId = matcher.group("subId");

            assert type != null;
            if (type.equals("route")) {
                return 0;
            } else if (type.equals("ticket")) {
                return 1;
            }
        }

        return 0;
    }

    @Override
    public int getItemCount() {
        return notificationList.size();
    }


    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView content;
        ImageView image;
        MaterialCardView card;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.notification_title);
            content = itemView.findViewById(R.id.notification_content);
            image = itemView.findViewById(R.id.notification_image);
            card = itemView.findViewById(R.id.notification_item);
        }
    }
}
