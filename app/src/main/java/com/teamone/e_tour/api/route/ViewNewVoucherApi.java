package com.teamone.e_tour.api.route;

import com.teamone.e_tour.entities.TouristRoute;
import com.teamone.e_tour.entities.Voucher;

import java.util.ArrayList;

public class ViewNewVoucherApi {
    public static final String emitEvent = "view-new-voucher";
    public static final String serverResponseEvent = "new-voucher-list";

    public class ResponseData {
        public String statusCode;
        public String message;
        public int status;
        public ArrayList<Voucher> data;
    }
}
