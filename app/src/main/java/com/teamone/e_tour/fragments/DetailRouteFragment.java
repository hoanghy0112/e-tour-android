package com.teamone.e_tour.fragments;

import android.annotation.SuppressLint;
import android.content.res.ColorStateList;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.faltenreich.skeletonlayout.Skeleton;
import com.faltenreich.skeletonlayout.SkeletonLayoutUtils;
import com.teamone.e_tour.R;
import com.teamone.e_tour.activities.HomeActivity;
import com.teamone.e_tour.adapters.CommentAdapter;
import com.teamone.e_tour.adapters.DestinationAdapter;
import com.teamone.e_tour.adapters.ImageAdapter;
import com.teamone.e_tour.adapters.RouteListAdapter;
import com.teamone.e_tour.api.company.CompanyApi;
import com.teamone.e_tour.api.route.TouristRouteApi;
import com.teamone.e_tour.api.route.ViewDetailRouteApi;
import com.teamone.e_tour.constants.SocketMessage;
import com.teamone.e_tour.databinding.FragmentDetailRouteBinding;
import com.teamone.e_tour.dialogs.LoadingDialog;
import com.teamone.e_tour.entities.Rating;
import com.teamone.e_tour.entities.TouristRoute;
import com.teamone.e_tour.models.BookingDataManager;
import com.teamone.e_tour.models.CredentialToken;
import com.teamone.e_tour.models.DetailRouteManager;
import com.teamone.e_tour.models.RatingManager;
import com.teamone.e_tour.utils.Formatter;
import com.teamone.e_tour.utils.SocketManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;

import me.relex.circleindicator.CircleIndicator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class DetailRouteFragment extends Fragment {
    DetailRouteManager detailRouteManager;
    RatingManager ratingManager;
    String routeId;
    MutableLiveData<TouristRoute> route = new MutableLiveData<>();
    MutableLiveData<ArrayList<TouristRoute>> companyRoute = new MutableLiveData<>();
    MutableLiveData<ArrayList<TouristRoute>> recommendRoute = new MutableLiveData<>();
    View originalLayout;
    int viewIndex;

    public DetailRouteFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        assert getArguments() != null;
        routeId = getArguments().getString("id");
        detailRouteManager = new DetailRouteManager((AppCompatActivity) getActivity());
        ratingManager = new RatingManager((AppCompatActivity) getActivity());
        detailRouteManager.viewRoute(routeId);
        ratingManager.viewRating(routeId);

        ((HomeActivity) requireActivity()).setBackId(R.id.action_detailTourFragment_to_homeFragment);
    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentDetailRouteBinding binding = FragmentDetailRouteBinding.inflate(inflater, container, false);

        requireActivity().findViewById(R.id.bottom_navigation).setVisibility(View.INVISIBLE);
        requireActivity().findViewById(R.id.home_wrapper).setPadding(0, 0, 0, 0);

        Window w = requireActivity().getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        RouteListAdapter companyRouteAdapter = new RouteListAdapter(this, R.layout.fragment_route_preview_card_large);
        RouteListAdapter recommendRouteAdapter = new RouteListAdapter(this, R.layout.search_route_regular_item);

        ViewPager routeImageList = binding.routeImageList;
        CircleIndicator routeImageIndicator = binding.routeImageIndicator;

        ImageAdapter imageAdapter = new ImageAdapter(getActivity());
        routeImageList.setAdapter(imageAdapter);

        routeImageIndicator.setViewPager(routeImageList);
        imageAdapter.registerDataSetObserver(routeImageIndicator.getDataSetObserver());

        DestinationAdapter destinationAdapter = new DestinationAdapter(getActivity());

        binding.destinationList.setAdapter(destinationAdapter);
        binding.destinationList.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));

        binding.companyRouteList.setNestedScrollingEnabled(false);
        binding.companyRouteList.setAdapter(companyRouteAdapter);
        binding.companyRouteList.setLayoutManager(new LinearLayoutManager(requireActivity(), RecyclerView.HORIZONTAL, false));
        Skeleton companyRouteSkeleton = SkeletonLayoutUtils.applySkeleton(binding.companyRouteList, R.layout.fragment_route_preview_card_large, 5);
        companyRouteSkeleton.setMaskCornerRadius(50);
        companyRouteSkeleton.showSkeleton();

        binding.recommendRouteList.setNestedScrollingEnabled(false);
        binding.recommendRouteList.setAdapter(recommendRouteAdapter);
        binding.recommendRouteList.setLayoutManager(new LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false));
        Skeleton recommendRouteSkeleton = SkeletonLayoutUtils.applySkeleton(binding.recommendRouteList, R.layout.search_route_regular_item, 5);
        recommendRouteSkeleton.setMaskCornerRadius(60);
        recommendRouteSkeleton.showSkeleton();

        binding.backBtn.setOnClickListener(v -> Navigation.findNavController(requireActivity(), R.id.home_wrapper).navigate(R.id.action_detailTourFragment_to_homeFragment));


        CommentAdapter adapter = new CommentAdapter(this);

        binding.commentList.setAdapter(adapter);
        binding.commentList.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
        Skeleton commentSkeleton = SkeletonLayoutUtils.applySkeleton(binding.commentList, R.layout.item_comment, 3);
        commentSkeleton.setMaskCornerRadius(60);
        commentSkeleton.showSkeleton();

        binding.addFavouriteBtn.setOnClickListener(v -> {
            TouristRoute t = route.getValue();
            if (t == null) return;

            if (t.isSaved()) {
                JSONObject object = new JSONObject();
                try {
                    object.put("routeId", routeId);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
                SocketManager.getInstance(getActivity()).emit("remove-route-from-saved", object);
            } else {
                JSONObject object = new JSONObject();
                try {
                    object.put("routeId", routeId);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
                SocketManager.getInstance(getActivity()).emit("save-route", object);
            }

            t.setSaved(!t.isSaved());
            route.postValue(t);
        });

        binding.followBtn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("UseCompatTextViewDrawableApis")
            @Override
            public void onClick(View v) {
                TouristRoute t = route.getValue();
                if (t == null) return;

                if (t.isFollowing()) {
                    JSONObject object = new JSONObject();
                    try {
                        object.put("routeId", routeId);
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                    SocketManager.getInstance(getActivity()).emit("unfollow-tourist-route", object);
                } else {
                    JSONObject object = new JSONObject();
                    try {
                        object.put("routeId", routeId);
                        object.put("notificationType", "all");
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                    SocketManager.getInstance(getActivity()).emit("follow-tourist-route", object);
                }

                t.setFollowing(!t.isFollowing());
                route.postValue(t);
            }
        });

        binding.contactSupportBtn.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putString("routeId", routeId);
            Navigation.findNavController(requireActivity(), R.id.home_wrapper).navigate(R.id.contactSupportFragment, bundle);
        });

        ratingManager.getRating().observe(getViewLifecycleOwner(), ratings -> {
            if (ratings == null) return;
            adapter.setRatings(ratings);
            commentSkeleton.showOriginal();
        });

        detailRouteManager.getRouteInfo().observe(getViewLifecycleOwner(), touristRoute -> {
            if (touristRoute != null && touristRoute.get_id().equals(routeId))
                route.postValue(touristRoute);
        });

        originalLayout = binding.detailRouteWrapper;
        ViewGroup parent = (ViewGroup) originalLayout.getParent();
        viewIndex = parent.indexOfChild(originalLayout);
        parent.removeAllViews();

        View loadingView = LayoutInflater.from(requireActivity()).inflate(R.layout.loading_fragment, parent, false);
        ((TextView) loadingView.findViewById(R.id.loading_text)).setText(R.string.loading_route);
        parent.addView(loadingView, viewIndex);

        route.observe(getViewLifecycleOwner(), new Observer<TouristRoute>() {
            @SuppressLint({"UseCompatTextViewDrawableApis", "UseCompatLoadingForDrawables"})
            @Override
            public void onChanged(TouristRoute touristRoute) {
                if (touristRoute == null) {
                    return;
                }

                fetchData();

                parent.removeAllViews();
                parent.addView(originalLayout, viewIndex);

                destinationAdapter.setDestinations(touristRoute.getRoute());

                binding.routeName.setText(touristRoute.getName());
                SpannableString content = new SpannableString(touristRoute.getCompany().name);
                content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
                binding.companyName.setText(content);
                binding.companyNameBelow.setText(touristRoute.getCompany().name);
                binding.routeType.setText(touristRoute.getType().equals("country") ? "Domestic" : "International");
                binding.reservationFee.setText(Formatter.toCurrency(touristRoute.getReservationFee()));
                binding.description.setText(touristRoute.getDescription());

                binding.companyTitle.setOnClickListener(v -> {
                    Bundle bundle = new Bundle();
                    bundle.putString("id", Objects.requireNonNull(route.getValue()).getCompanyId());
                    Navigation.findNavController(v).navigate(R.id.companyDetail, bundle);
                });

                binding.companyName.setOnClickListener(v -> {
                    Bundle bundle = new Bundle();
                    bundle.putString("id", touristRoute.getCompanyId());
                    Navigation.findNavController(v).navigate(R.id.action_detailTourFragment_to_companyDetail, bundle);
                });

                if (touristRoute.getImages().size() != 0) {
                    imageAdapter.setImages(touristRoute.getImages());
                    BookingDataManager.getInstance().setImageUri(touristRoute.getImages().get(0));
                }

                if (touristRoute.isFollowing()) {
                    binding.followBtn.setBackgroundColor(requireActivity().getColor(R.color.blue));
                    binding.followBtn.setCompoundDrawableTintList(ColorStateList.valueOf(requireActivity().getColor(R.color.white)));
                    binding.followBtn.setTextColor(requireActivity().getColor(R.color.white));
                    binding.followBtn.setCompoundDrawablesRelativeWithIntrinsicBounds(requireActivity().getDrawable(R.drawable.bell_active), null, null, null);
                    binding.followBtn.setText(requireActivity().getString(R.string.unfollow));
                } else {
                    binding.followBtn.setBackgroundColor(requireActivity().getColor(R.color.blue_5_percent));
                    binding.followBtn.setCompoundDrawableTintList(ColorStateList.valueOf(requireActivity().getColor(R.color.chat_blue)));
                    binding.followBtn.setTextColor(requireActivity().getColor(R.color.chat_blue));
                    binding.followBtn.setCompoundDrawablesRelativeWithIntrinsicBounds(requireActivity().getDrawable(R.drawable.bell), null, null, null);
                    binding.followBtn.setText(requireActivity().getString(R.string.follow));
                }

                if (touristRoute.isSaved()) {
                    binding.addFavouriteBtn.setCompoundDrawableTintList(ColorStateList.valueOf(requireActivity().getColor(R.color.white)));
                    binding.addFavouriteBtn.setBackgroundColor(requireActivity().getColor(R.color.blue));
//                    binding.addFavouriteBtn.setCompoundDrawablesRelativeWithIntrinsicBounds(requireActivity().getDrawable(R.drawable.save_selected), null, null, null);
                } else {
                    binding.addFavouriteBtn.setCompoundDrawableTintList(ColorStateList.valueOf(requireActivity().getColor(R.color.blue)));
                    binding.addFavouriteBtn.setBackgroundColor(requireActivity().getColor(R.color.white));
//                    binding.addFavouriteBtn.setCompoundDrawablesRelativeWithIntrinsicBounds(requireActivity().getDrawable(R.drawable.save), null, null, null);
                }

                binding.bookTicketBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        assert getArguments() != null;
                        String routeId = getArguments().getString("id");

                        Bundle bundle = new Bundle();
                        bundle.putString("id", routeId);
                        bundle.putString("name", touristRoute.getName());
                        NavHostFragment.findNavController(DetailRouteFragment.this).navigate(R.id.action_detailTourFragment_to_tourListFragment, bundle);
                    }
                });
            }
        });

        companyRoute.observe(getViewLifecycleOwner(), touristRoutes -> {
            if (touristRoutes == null) return;
            companyRouteAdapter.setRouteList(touristRoutes);
            companyRouteSkeleton.showOriginal();
        });

        recommendRoute.observe(getViewLifecycleOwner(), touristRoutes -> {
            if (touristRoutes == null) return;
            recommendRouteAdapter.setRouteList(touristRoutes);
            recommendRouteSkeleton.showOriginal();
        });

        return binding.getRoot();
    }

    void fetchData() {
        TouristRouteApi.api.getRecommendRouteOfCompany(Objects.requireNonNull(route.getValue()).getCompany()._id).enqueue(new Callback<TouristRouteApi.GetTouristRouteListResponse>() {
            @Override
            public void onResponse(@NonNull Call<TouristRouteApi.GetTouristRouteListResponse> call, @NonNull Response<TouristRouteApi.GetTouristRouteListResponse> response) {
                if (response.code() == 200) {
                    assert response.body() != null;
                    companyRoute.postValue(response.body().data);
                } else {
                    if (getActivity() == null) return;
                    Toast.makeText(requireActivity(), "Error: " + response.code() + " - " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<TouristRouteApi.GetTouristRouteListResponse> call, @NonNull Throwable t) {
                if (getActivity() == null) return;
                Toast.makeText(requireActivity(), "App error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        TouristRouteApi.api.getRecommendRouteOfSimilarity(routeId, CredentialToken.getInstance(requireActivity()).getBearerAccessToken()).enqueue(new Callback<TouristRouteApi.GetTouristRouteListResponse>() {
            @Override
            public void onResponse(@NonNull Call<TouristRouteApi.GetTouristRouteListResponse> call, @NonNull Response<TouristRouteApi.GetTouristRouteListResponse> response) {
                if (response.code() == 200) {
                    assert response.body() != null;
                    recommendRoute.postValue(response.body().data);
                } else {
                    if (getActivity() == null) return;
                    Toast.makeText(requireActivity(), "Error: " + response.code() + " - " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<TouristRouteApi.GetTouristRouteListResponse> call, @NonNull Throwable t) {
                if (getActivity() == null) return;
                Toast.makeText(requireActivity(), "App error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}