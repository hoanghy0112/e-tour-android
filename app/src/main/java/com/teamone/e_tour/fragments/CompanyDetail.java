package com.teamone.e_tour.fragments;

import android.annotation.SuppressLint;
import android.content.res.ColorStateList;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.teamone.e_tour.R;
import com.teamone.e_tour.adapters.ImageAdapter;
import com.teamone.e_tour.adapters.RouteListAdapter;
import com.teamone.e_tour.api.company.CompanyApi;
import com.teamone.e_tour.databinding.FragmentCompanyDetailBinding;
import com.teamone.e_tour.entities.Company;
import com.teamone.e_tour.entities.Image;
import com.teamone.e_tour.entities.TouristRoute;
import com.teamone.e_tour.models.CredentialToken;
import com.teamone.e_tour.utils.SocketManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CompanyDetail extends Fragment {
    String id;
    View originalLayout;
    int viewIndex;

    MutableLiveData<Company> companyInfo = new MutableLiveData<>();
    MutableLiveData<ArrayList<TouristRoute>> companyRouteList = new MutableLiveData<>();

    public CompanyDetail() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            id = getArguments().getString("id");
        }
    }

    @SuppressLint({"UseCompatTextViewDrawableApis", "UseCompatLoadingForDrawables"})
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentCompanyDetailBinding binding = FragmentCompanyDetailBinding.inflate(inflater);

        ImageAdapter imageAdapter = new ImageAdapter(requireActivity());

        RouteListAdapter routeListAdapter = new RouteListAdapter(this, R.layout.search_route_regular_item);

        binding.routeImageList.setAdapter(imageAdapter);
        binding.routeImageIndicator.setViewPager(binding.routeImageList);
        imageAdapter.registerDataSetObserver(binding.routeImageIndicator.getDataSetObserver());

        binding.touristRouteList.setNestedScrollingEnabled(false);
        binding.touristRouteList.setAdapter(routeListAdapter);
        binding.touristRouteList.setLayoutManager(new LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false));

        fetchData();
        binding.swiperefresh.setOnRefreshListener(this::fetchData);

        originalLayout = binding.swiperefresh;
        ViewGroup parent = (ViewGroup) originalLayout.getParent();
        viewIndex = parent.indexOfChild(originalLayout);
        parent.removeAllViews();

        View loadingView = LayoutInflater.from(requireActivity()).inflate(R.layout.loading_fragment, parent, false);
        ((TextView) loadingView.findViewById(R.id.loading_text)).setText(R.string.loading_route);
        parent.addView(loadingView, viewIndex);

        companyInfo.observe(getViewLifecycleOwner(), company -> {
            if (company == null) return;
            binding.swiperefresh.setRefreshing(false);

            parent.removeAllViews();
            parent.addView(originalLayout, viewIndex);

            binding.companyName.setText(company.name);
            binding.companyNameBelow.setText(company.name);
            binding.description.setText(company.description);
            binding.companyEmail.setText(company.email);
            binding.contactEmail.setText(company.email);

            imageAdapter.setImages(company.previewImages);

            Glide.with(requireActivity()).load(new Image(company.image).getImageUri()).into(binding.companyImage);

            if (company.isFollowing) {
                binding.followBtn.setBackgroundColor(requireActivity().getColor(R.color.blue));
                binding.followBtn.setCompoundDrawableTintList(ColorStateList.valueOf(requireActivity().getColor(R.color.white)));
                binding.followBtn.setTextColor(requireActivity().getColor(R.color.white));
                binding.followBtn.setCompoundDrawablesRelativeWithIntrinsicBounds(requireActivity().getDrawable(R.drawable.bell_active), null, null, null);
                binding.followBtn.setText(requireActivity().getString(R.string.unfollow_company));
            } else {
                binding.followBtn.setBackgroundColor(requireActivity().getColor(R.color.blue_5_percent));
                binding.followBtn.setCompoundDrawableTintList(ColorStateList.valueOf(requireActivity().getColor(R.color.chat_blue)));
                binding.followBtn.setTextColor(requireActivity().getColor(R.color.chat_blue));
                binding.followBtn.setCompoundDrawablesRelativeWithIntrinsicBounds(requireActivity().getDrawable(R.drawable.bell), null, null, null);
                binding.followBtn.setText(requireActivity().getString(R.string.follow_company));
            }
        });

        companyRouteList.observe(getViewLifecycleOwner(), touristRoutes -> {
            if (touristRoutes == null) return;

            routeListAdapter.setRouteList(touristRoutes);
        });

        binding.followBtn.setOnClickListener(v -> {
            Company t = companyInfo.getValue();
            if (t == null) return;

            JSONObject object = new JSONObject();
            if (t.isFollowing) {
                try {
                    object.put("companyId", t._id);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
                SocketManager.getInstance(getActivity()).emit("unfollow-company", object);
            } else {
                try {
                    object.put("companyId", t._id);
                    object.put("notificationType", "all");
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
                SocketManager.getInstance(getActivity()).emit("follow-company", object);
            }

            t.isFollowing = !t.isFollowing;
            companyInfo.postValue(t);
        });

        return binding.getRoot();
    }

    void fetchData() {
        CompanyApi.api.getCompanyInfo(id, CredentialToken.getInstance(requireActivity()).getBearerAccessToken()).enqueue(new Callback<CompanyApi.GetCompanyInfoResponse>() {
            @Override
            public void onResponse(@NonNull Call<CompanyApi.GetCompanyInfoResponse> call, @NonNull Response<CompanyApi.GetCompanyInfoResponse> response) {
                if (response.code() == 200) {
                    assert response.body() != null;
                    companyInfo.postValue(response.body().data);
                } else {
                    if (getActivity() == null) return;
                    Toast.makeText(requireActivity(), "Error: " + response.code() + " - " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<CompanyApi.GetCompanyInfoResponse> call, @NonNull Throwable t) {
                if (getActivity() == null) return;
                Toast.makeText(requireActivity(), "App error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        CompanyApi.api.getTouristRouteOfCompany(id).enqueue(new Callback<CompanyApi.GetTouristRouteOfCompany>() {
            @Override
            public void onResponse(@NonNull Call<CompanyApi.GetTouristRouteOfCompany> call, @NonNull Response<CompanyApi.GetTouristRouteOfCompany> response) {
                if (response.code() == 200) {
                    assert response.body() != null;
                    companyRouteList.postValue(response.body().data);
                } else {
                    if (getActivity() == null) return;
                    Toast.makeText(requireActivity(), "Error: " + response.code() + " - " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<CompanyApi.GetTouristRouteOfCompany> call, @NonNull Throwable t) {
                if (getActivity() == null) return;
                Toast.makeText(requireActivity(), "App error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}