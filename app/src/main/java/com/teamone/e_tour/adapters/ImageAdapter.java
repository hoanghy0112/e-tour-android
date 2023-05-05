package com.teamone.e_tour.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.teamone.e_tour.R;
import com.teamone.e_tour.entities.Image;

import java.util.ArrayList;
import java.util.function.Consumer;

public class ImageAdapter extends PagerAdapter {
    private Context context;
    private ArrayList<Image> images = new ArrayList<>();

    public ImageAdapter(Context context) {
        this.context = context;
    }

    public void setImages(ArrayList<String> imageURIs) {
        this.images = new ArrayList<>();
        imageURIs.forEach(new Consumer<String>() {
            @Override
            public void accept(String s) {
                images.add(new Image(s));
            }
        });
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = LayoutInflater.from(container.getContext()).inflate(R.layout.route_image_item, container, false);
        ImageView imageView = view.findViewById(R.id.image);

        Image image = images.get(position);
        Glide.with(context).load(image.getImageUri()).into(imageView);

        container.addView(view);

        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return images.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }
}