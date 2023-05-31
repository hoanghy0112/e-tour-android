package com.teamone.e_tour.models;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.teamone.e_tour.api.route.ViewNewVoucherApi;
import com.teamone.e_tour.api.route.ViewRecommendedRoute;
import com.teamone.e_tour.entities.TouristRoute;
import com.teamone.e_tour.entities.Voucher;
import com.teamone.e_tour.utils.SocketManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import io.socket.emitter.Emitter;

public class HotVoucherManager {
    private static HotVoucherManager instance;
    private Context context;
    private SocketManager socket;

    private MutableLiveData<ArrayList<Voucher>> vouchers = new MutableLiveData<ArrayList<Voucher>>(new ArrayList<>());

    public MutableLiveData<ArrayList<Voucher>> getVouchers() {
        return vouchers;
    }

    public HotVoucherManager(Context context) {
        this.context = context;
        socket = new SocketManager(context);
    }

    public static HotVoucherManager getInstance(Context context) {
        if (instance == null) {
            instance = new HotVoucherManager(context);
        }
        return instance;
    }

    public void fetchData(int num) {
        JSONObject object = new JSONObject();
        try {
            object.put("num", num);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        socket.emit(ViewNewVoucherApi.emitEvent, object);

        socket.on(ViewNewVoucherApi.serverResponseEvent, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                Gson gson = new GsonBuilder().create();
                ViewNewVoucherApi.ResponseData response = gson.fromJson(String.valueOf(args[0]), ViewNewVoucherApi.ResponseData.class);
                if (response.status == 200) {
                    vouchers.postValue(response.data);
                }
            }
        });
    }
}
