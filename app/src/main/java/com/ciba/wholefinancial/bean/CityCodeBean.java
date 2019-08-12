package com.ciba.wholefinancial.bean;

import android.graphics.Color;

import com.ciba.wholefinancial.R;

import java.util.List;
import java.util.Map;

public class CityCodeBean {
    private String cityCode;
    private String cityName;

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    @Override
    public String toString() {
        return "CityCodeBean{" +
                "cityCode='" + cityCode + '\'' +
                ", cityName='" + cityName + '\'' +
                '}';
    }
}
