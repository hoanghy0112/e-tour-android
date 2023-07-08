package com.teamone.e_tour.models;

import androidx.lifecycle.MutableLiveData;

import com.teamone.e_tour.entities.Voucher;

import java.util.ArrayList;

public class VoucherManager {
    static VoucherManager instance;
    MutableLiveData<ArrayList<Voucher>> savedList = new MutableLiveData<>();
    MutableLiveData<Voucher> selectVoucher = new MutableLiveData<>();

    public static VoucherManager getInstance() {
        if (instance == null) {
            instance = new VoucherManager();
        }
        return instance;
    }

    public MutableLiveData<ArrayList<Voucher>> getSavedList() {
        return savedList;
    }

    public void setSavedList(ArrayList<Voucher> savedList) {
        this.savedList.postValue(savedList);
    }

    public MutableLiveData<Voucher> getSelectVoucher() {
        return selectVoucher;
    }

    public void setSelectVoucher(Voucher selectVoucher) {
        this.selectVoucher.postValue(selectVoucher);
    }
}
