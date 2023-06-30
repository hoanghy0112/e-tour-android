package com.teamone.e_tour.fragments;

import static androidx.core.content.ContextCompat.getSystemService;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.transition.ChangeBounds;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.teamone.e_tour.R;
import com.teamone.e_tour.adapters.RouteListAdapter;
import com.teamone.e_tour.api.route.ViewPopularRoute;
import com.teamone.e_tour.databinding.FragmentInputSearchBinding;
import com.teamone.e_tour.entities.TouristRoute;
import com.teamone.e_tour.utils.SocketManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import io.socket.emitter.Emitter;

public class InputSearch extends Fragment {
    FragmentInputSearchBinding binding;
    ViewGroup parent;
    int viewIndex;
    View originalLayout;
    MutableLiveData<ArrayList<TouristRoute>> routes = new MutableLiveData<>();

    public InputSearch() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentInputSearchBinding.inflate(inflater);

        setSharedElementEnterTransition(new ChangeBounds());
        binding.editText.requestFocus();

        requireActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        RouteListAdapter routeListAdapter = new RouteListAdapter(this, R.layout.search_route_regular_item);
        binding.routeList.setAdapter(routeListAdapter);
        binding.routeList.setLayoutManager(new LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false));
        binding.routeList.setNestedScrollingEnabled(false);

        binding.editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) return;
                InputMethodManager imm = (InputMethodManager)  requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                requireActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
            }
        });

        binding.editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    performSearch(binding.editText.getText().toString());
                    return true;
                }
                return false;
            }
        });

        SocketManager.getInstance(requireActivity()).on("search-route-result", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                Gson gson = new GsonBuilder().create();
                ViewPopularRoute.ResponseData response = gson.fromJson(String.valueOf(args[0]), ViewPopularRoute.ResponseData.class);
                if (response.status == 200) {
                    routes.postValue(response.data);
                }
            }
        });

        routes.observe(getViewLifecycleOwner(), new Observer<ArrayList<TouristRoute>>() {
            @Override
            public void onChanged(ArrayList<TouristRoute> touristRoutes) {
                if (touristRoutes == null) return;
                parent.removeAllViews();
                parent.addView(originalLayout, viewIndex);
                routeListAdapter.setRouteList(touristRoutes);
            }
        });

        return binding.getRoot();
    }

    public void performSearch(String keyword) {
        binding.editText.clearFocus();
        InputMethodManager in = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        in.hideSoftInputFromWindow(binding.editText.getWindowToken(), 0);

        JSONObject object = new JSONObject();
        try {
            object.put("keyword", keyword);
        } catch (JSONException e) {
            //
        }
        SocketManager.getInstance(requireActivity()).emit("search-route", object);

        originalLayout = binding.routeList;
        parent = (ViewGroup) originalLayout.getParent();
        viewIndex = parent.indexOfChild(originalLayout);
        parent.removeAllViews();

        View loadingView = LayoutInflater.from(requireActivity()).inflate(R.layout.loading_find, parent, false);
        ((TextView) loadingView.findViewById(R.id.loading_text)).setText(R.string.search_for_route);
        parent.addView(loadingView, viewIndex);
    }
}