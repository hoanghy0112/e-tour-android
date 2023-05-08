package com.teamone.e_tour.api.tour;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.teamone.e_tour.entities.Tour;
import com.teamone.e_tour.utils.SocketManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import io.socket.emitter.Emitter;

public class ViewTourListOfRouteApi {
    public static final String emitEvent = "filter-tour";
    public static final String serverResponseEvent = "list-tour";

    public class ResponseData {
        public String statusCode;
        public String message;
        public int status;
        public ArrayList<Tour> data;
    }


    private Context context;
    private SocketManager socket;

    public MutableLiveData<ArrayList<Tour>> getTourList() {
        return tourList;
    }

    private MutableLiveData<ArrayList<Tour>> tourList = new MutableLiveData<ArrayList<Tour>>();


    public ViewTourListOfRouteApi(Context context) {
        this.context = context;
    }


    public void fetchData(String id) {
        JSONObject object = new JSONObject();

        try {
            object.put("touristRoute", id);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        socket = SocketManager.getInstance(context);
        socket.emit(ViewTourListOfRouteApi.emitEvent, object);

        socket.on(ViewTourListOfRouteApi.serverResponseEvent, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").create();
                ViewTourListOfRouteApi.ResponseData response = gson.fromJson(String.valueOf(args[0]), ViewTourListOfRouteApi.ResponseData.class);
                if (response.status == 200) {
                    tourList.postValue(response.data);
                }
            }
        });
    }

    public void finish() {
//        if (socket != null && socket.getSocket() != null)
//            socket.getSocket().disconnect();
    }
}
