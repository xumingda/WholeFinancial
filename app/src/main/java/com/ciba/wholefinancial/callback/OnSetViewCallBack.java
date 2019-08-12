package com.ciba.wholefinancial.callback;

import com.ciba.wholefinancial.bean.TypeBean;

import java.util.List;

public interface OnSetViewCallBack {
    public void CallBackSuccess(int type);
    public void CallBackDate(List<TypeBean> typeBeanList);
}
