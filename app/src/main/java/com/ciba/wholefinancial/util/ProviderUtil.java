package com.ciba.wholefinancial.util;

import android.content.Context;

public class ProviderUtil {

    public static String getFileProviderName(Context context){
        return context.getPackageName()+".provider";
    }
}
