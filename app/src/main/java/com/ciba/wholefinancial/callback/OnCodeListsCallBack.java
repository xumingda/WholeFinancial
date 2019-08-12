package com.ciba.wholefinancial.callback;

import com.ciba.wholefinancial.bean.CityCodeBean;

import java.util.List;

public interface OnCodeListsCallBack {
    public void CallBackCityCodeList(List<CityCodeBean> list);
}
