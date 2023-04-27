package com.teamone.e_tour;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.teamone.e_tour.databinding.FragmentLoadingBinding;

public class LoadingFragment extends Fragment {

    public LoadingFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentLoadingBinding binding = FragmentLoadingBinding.inflate(inflater, container, false);
        return inflater.inflate(R.layout.fragment_loading, container, false);
    }
}